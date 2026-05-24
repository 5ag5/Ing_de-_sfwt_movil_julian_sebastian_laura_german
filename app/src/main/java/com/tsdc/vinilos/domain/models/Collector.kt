package com.tsdc.vinilos.domain.models

data class Collector(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val albumCount: Int = 0
)
