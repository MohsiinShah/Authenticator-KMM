package com.mohsin.auth

// androidMain
import android.content.Context
import com.mohsin.auth.domain.ParsedUri
import java.io.File
import java.net.URI

actual object UriParser {
    actual fun parse(raw: String): ParsedUri {
        val uri = URI(raw)
        val queryPairs = mutableMapOf<String, String>()
        uri.query?.split("&")?.forEach {
            val parts = it.split("=")
            if (parts.size == 2) {
                queryPairs[parts[0]] = parts[1]
            }
        }
        return ParsedUri(
            scheme = uri.scheme ?: "",
            host = uri.host ?: "",
            path = uri.path ?: "",
            args = queryPairs
        )
    }
}

actual object PlatformContext {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    actual fun filesDirPath(): String = appContext.filesDir.absolutePath
}

actual class FileStore actual constructor(fileName: String) {
    private val file: File = File(
        // use Android’s app files dir
        PlatformContext.filesDirPath(),
        fileName
    )

    actual fun exists(): Boolean = file.exists()

    actual fun readText(): String = file.readText()

    actual fun writeText(text: String) {
        file.writeText(text)
    }
}
