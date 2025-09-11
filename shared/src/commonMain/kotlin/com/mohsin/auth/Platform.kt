package com.mohsin.auth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
