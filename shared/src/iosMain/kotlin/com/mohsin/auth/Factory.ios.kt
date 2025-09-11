package com.mohsin.auth

// iosMain

import com.mohsin.auth.domain.ParsedUri
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL

actual object UriParser {
    actual fun parse(raw: String): ParsedUri {
        val components = NSURLComponents(string = raw)
        val queryPairs = mutableMapOf<String, String>()

        val items = components?.queryItems as? List<NSURLQueryItem>
        items?.forEach { item ->
            val key = item.name
            val value = item.value?.toString() ?: ""
            queryPairs[key] = value
        }

        return ParsedUri(
            scheme = components?.scheme ?: "",
            host = components?.host ?: "",
            path = components?.path ?: "",
            args = queryPairs
        )
    }
}



actual class FileStore actual constructor(fileName: String) {
    @OptIn(ExperimentalForeignApi::class)
    private val fileUrl: NSURL? =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )?.URLByAppendingPathComponent(fileName)

    actual fun exists(): Boolean =
        fileUrl != null && NSFileManager.defaultManager.fileExistsAtPath(fileUrl!!.path!!)

    actual fun readText(): String {
        val data = NSData.dataWithContentsOfURL(fileUrl!!)
        return NSString.create(data!!, NSUTF8StringEncoding) as String
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun writeText(text: String) {
        val nsString = text as NSString
        nsString.writeToURL(fileUrl!!, true, NSUTF8StringEncoding, null)
    }
}


actual object PlatformContext {
    actual fun filesDirPath(): String {
       return ""
    }
}