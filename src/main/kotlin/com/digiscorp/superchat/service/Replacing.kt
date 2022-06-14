package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.IncomingMsgDto
import com.digiscorp.superchat.persistance.ContactId
import com.digiscorp.superchat.persistance.ContactRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL

@Component
class PlaceholderReplacer(val replacers: List<Replacer>) {

    fun replacePlaceholders(src: String, msg: IncomingMsgDto): IncomingMsgDto {
        var content: String = msg.content
        for (repl in replacers){
            content = repl.update(content, src, msg)
        }
        return IncomingMsgDto(msg.dst, msg.ts, content)
    }
}

interface Replacer{
    fun update(content: String, src: String, msg: IncomingMsgDto): String
}

@Component
class NameReplacer(var repository: ContactRepository) : Replacer{
    override fun update(content: String, src: String, msg: IncomingMsgDto): String {
        return content.replace("@name", repository.findById(ContactId(src, msg.dst)).get().name)
    }
}

@Component
class BitPayPriceReplacer(
        @Value("\${bitpay.uri}") val bitPayUri: String,
        val mapper: ObjectMapper
) : Replacer{

    var currentPrice: Double = fetchPrice()

    @Scheduled(fixedDelay = 60_000)
    fun refreshPrice() {
        currentPrice = fetchPrice()
    }
    private fun fetchPrice(): Double {
        val typeRef = object : TypeReference<List<CoinPrice>>() {}
        val priceList: List<CoinPrice> = mapper.readValue(URL(bitPayUri), typeRef)
        return priceList.filter { it.code.equals("USD") }.map { it.rate }.first()
    }

    override fun update(content: String, src: String, msg: IncomingMsgDto): String {
        return content.replace("@price", "$currentPrice$")
    }
}

data class CoinPrice (
        val code: String,
        val name: String,
        val rate: Double
)

