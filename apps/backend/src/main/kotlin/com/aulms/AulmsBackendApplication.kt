package com.aulms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AulmsBackendApplication

fun main(args: Array<String>) {
    runApplication<AulmsBackendApplication>(*args)
}

