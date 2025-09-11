package com.mohsin.auth.domain.time

interface Clock {

    fun epochSeconds(): Long

}