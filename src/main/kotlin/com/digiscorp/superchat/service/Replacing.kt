package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.IncomingMsgDto
import com.digiscorp.superchat.persistance.ContactId
import com.digiscorp.superchat.persistance.ContactRepository
import org.springframework.stereotype.Component
import kotlin.random.Random

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
class RandomPriceReplacer() : Replacer{
    override fun update(content: String, src: String, msg: IncomingMsgDto): String {
        return content.replace("@price", Random.nextDouble().toString())
    }
}

