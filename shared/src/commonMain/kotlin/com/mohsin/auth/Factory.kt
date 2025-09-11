package com.mohsin.auth

import com.mohsin.auth.domain.ParsedUri

// commonMain
expect object UriParser {
    fun parse(raw: String): ParsedUri
}

expect object PlatformContext {
    fun filesDirPath(): String
}

// commonMain
expect class FileStore(fileName: String) {
    fun exists(): Boolean
    fun readText(): String
    fun writeText(text: String)
}
