package com.digiscorp.superchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SuperchatApplication

fun main(args: Array<String>) {
    runApplication<SuperchatApplication>(*args)
}
