package com.mohsin.auth.domain

data class ParsedUri(
    val scheme: String,
    val host: String,
    val path: String,
    val args: Map<String, String>
)