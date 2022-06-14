package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.ConversationMsgDto
import com.digiscorp.superchat.dto.OutgoingMsgDto
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

    fun conversations(user: String): Map<String, List<ConversationMsgDto>?> {
        return contactRepository.findAllByIdSrc(user)
            .associate { it.name to it.messages?.map { msgEntity -> ConversationMsgDto(msgEntity.ts, msgEntity.content, msgEntity.isIncoming) } }
    }

    @Transactional
    fun processMessage(me: String, msg: OutgoingMsgDto): MsgDto {
        val updatedMsg = placeholderReplacer.replacePlaceholders(me, msg)
        saveMessageForPresentContact(me, msg.dst, updatedMsg, true)
        saveMessageForPresentContact(msg.dst, me, updatedMsg, false)
        return updatedMsg
    }

    private fun saveMessageForPresentContact(src: String, dst: String, msg: OutgoingMsgDto, isOutgoing: Boolean) {
        val contact = contactRepository.findById(ContactId(src, dst))
        if (contact.isPresent) {
            val savedMsg = msgRepository.save(MsgEntity(msg.ts, msg.content, isOutgoing))
            contact.get().messages?.add(savedMsg)
        }
    }
}