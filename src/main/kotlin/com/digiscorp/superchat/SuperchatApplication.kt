package com.digiscorp.superchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SuperchatApplication

fun main(args: Array<String>) {
    runApplication<SuperchatApplication>(*args)
}
