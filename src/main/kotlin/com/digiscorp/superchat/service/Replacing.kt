package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.OutgoingMsgDto
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

    fun replacePlaceholders(src: String, msg: OutgoingMsgDto): OutgoingMsgDto {
        for (repl in replacers){
            msg.content = repl.update(msg.content, src, msg)
        }
        return msg
    }
}

interface Replacer{
    fun update(content: String, src: String, msg: OutgoingMsgDto): String
}

@Component
class NameReplacer(var repository: ContactRepository) : Replacer{
    override fun update(content: String, src: String, msg: OutgoingMsgDto): String {
        val contactEntityOptional = repository.findById(ContactId(src, msg.dst))
        if(contactEntityOptional.isPresent) {
            return content.replace("@name", contactEntityOptional.get().name)
        }
        return content
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

    override fun update(content: String, src: String, msg: OutgoingMsgDto): String {
        return content.replace("@price", "$currentPrice$")
    }
}

data class CoinPrice (
        val code: String,
        val name: String,
        val rate: Double
)

