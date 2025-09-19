//
//  QRScannerView.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import AVFoundation

struct QRScannerView: UIViewControllerRepresentable {
    class Coordinator: NSObject, AVCaptureMetadataOutputObjectsDelegate {
        var parent: QRScannerView
        var didScanOnce: Bool = false
        var session: AVCaptureSession?
        var previewLayer: AVCaptureVideoPreviewLayer?

        init(parent: QRScannerView) {
            self.parent = parent
        }

        func metadataOutput(_ output: AVCaptureMetadataOutput,
                            didOutput metadataObjects: [AVMetadataObject],
                            from connection: AVCaptureConnection) {
            guard !didScanOnce else { return }
            if let metadataObject = metadataObjects.first as? AVMetadataMachineReadableCodeObject,
               let qrValue = metadataObject.stringValue {
                didScanOnce = true
                UIImpactFeedbackGenerator(style: .light).impactOccurred()
                parent.onScan(qrValue)
                session?.stopRunning()
                output.setMetadataObjectsDelegate(nil, queue: nil)
            }
        }
    }

    var onScan: (String) -> Void

    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self)
    }

    func makeUIViewController(context: Context) -> UIViewController {
        let controller = CameraViewController()
        
        let session = AVCaptureSession()
        context.coordinator.session = session
        guard let videoCaptureDevice = AVCaptureDevice.default(for: .video) else { return controller }
        guard let videoInput = try? AVCaptureDeviceInput(device: videoCaptureDevice) else { return controller }
        if session.canAddInput(videoInput) {
            session.addInput(videoInput)
        }

        let metadataOutput = AVCaptureMetadataOutput()
        if session.canAddOutput(metadataOutput) {
            session.addOutput(metadataOutput)
            metadataOutput.setMetadataObjectsDelegate(context.coordinator, queue: DispatchQueue.main)
            metadataOutput.metadataObjectTypes = [.qr]
        }

        let previewLayer = AVCaptureVideoPreviewLayer(session: session)
        previewLayer.frame = controller.view.bounds
        previewLayer.videoGravity = .resizeAspectFill
        controller.view.layer.addSublayer(previewLayer)
        context.coordinator.previewLayer = previewLayer

        // Configure ROI
        let screen = controller.view.bounds
        let side = min(screen.width, screen.height) * 0.6
        let roi = CGRect(x: (screen.width - side) / 2,
                       y: (screen.height - side) / 2,
                       width: side,
                       height: side)
        let converted = previewLayer.metadataOutputRectConverted(fromLayerRect: roi)
        metadataOutput.rectOfInterest = converted

        // Add overlay
        let overlayView = createScanningOverlay(for: controller.view.bounds)
        controller.view.addSubview(overlayView)

        // Set session and start callback
        controller.session = session
        
        return controller
    }

    private func createScanningOverlay(for bounds: CGRect) -> UIView {
        let overlayView = UIView(frame: bounds)
        overlayView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        
        let side = min(bounds.width, bounds.height) * 0.6
        let scanRect = CGRect(x: (bounds.width - side) / 2,
                             y: (bounds.height - side) / 2,
                             width: side,
                             height: side)
        
        // Create transparent hole
        let path = UIBezierPath(rect: bounds)
        let scanPath = UIBezierPath(rect: scanRect)
        path.append(scanPath.reversing())
        
        let maskLayer = CAShapeLayer()
        maskLayer.path = path.cgPath
        overlayView.layer.mask = maskLayer
        
        // Add border
        let borderLayer = CAShapeLayer()
        borderLayer.path = UIBezierPath(rect: scanRect).cgPath
        borderLayer.fillColor = UIColor.clear.cgColor
        borderLayer.strokeColor = UIColor.white.cgColor
        borderLayer.lineWidth = 2.0
        overlayView.layer.addSublayer(borderLayer)
        
        return overlayView
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}

    static func dismantleUIViewController(_ uiViewController: UIViewController, coordinator: Coordinator) {
        coordinator.session?.stopRunning()
        if let outputs = coordinator.session?.outputs {
            for output in outputs {
                if let metadataOutput = output as? AVCaptureMetadataOutput {
                    metadataOutput.setMetadataObjectsDelegate(nil, queue: nil)
                }
            }
        }
        coordinator.session = nil
    }
}


class CameraViewController: UIViewController {
    var session: AVCaptureSession?
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DispatchQueue.global(qos: .userInitiated).async {
            self.session?.startRunning()
        }
    }
}
