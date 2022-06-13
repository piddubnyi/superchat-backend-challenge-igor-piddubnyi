package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.IncomingMsgDto
import com.digiscorp.superchat.dto.MsgDto
import com.digiscorp.superchat.persistance.ContactId
import com.digiscorp.superchat.persistance.ContactRepository
import com.digiscorp.superchat.persistance.MsgEntity
import com.digiscorp.superchat.persistance.MsgRepository
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ChatService(
    val contactRepository: ContactRepository,
    val msgRepository: MsgRepository,
    val placeholderReplacer: PlaceholderReplacer
) {

    fun conversations(user: String): Map<String, List<MsgDto>?> {
        return contactRepository.findAllByIdSrc(user).associate { it.id.dst to it.messages?.map { MsgDto(it.ts, it.content) } }
    }

    @Transactional
    fun processMessage(me: String, msg: IncomingMsgDto): MsgDto {
        val updatedMsg = placeholderReplacer.replacePlaceholders(me, msg)
        val forwardContact = contactRepository.findById(ContactId(me, updatedMsg.dst))
        val backwardContact = contactRepository.findById(ContactId(updatedMsg.dst, me))
        if(forwardContact.isPresent && backwardContact.isPresent){
            val savedMsg = msgRepository.save(MsgEntity(updatedMsg.ts, updatedMsg.content))
            forwardContact.get().messages?.add(savedMsg)
            backwardContact.get().messages?.add(savedMsg)
            return updatedMsg
        } else {
            throw IllegalArgumentException("Not in contacts : " + updatedMsg.dst)
        }
    }
}