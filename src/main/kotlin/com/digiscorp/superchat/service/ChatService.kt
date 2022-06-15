package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.ConversationMsgDto
import com.digiscorp.superchat.dto.MsgDto
import com.digiscorp.superchat.dto.OutgoingMsgDto
import com.digiscorp.superchat.persistance.*
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ChatService(
        val contactRepository: ContactRepository,
        val msgRepository: MsgRepository,
        val msgPropRepository: MsgPropRepository,
        val placeholderReplacer: PlaceholderReplacer
) {

    fun conversations(user: String): Map<String, List<ConversationMsgDto>?> {
        return contactRepository.findAllByIdSrc(user)
            .associate {
                it.name to it.messages?.map {
                    msgEntity -> ConversationMsgDto(
                        msgEntity.ts,
                        msgEntity.content,
                        msgEntity.properties?.associate { it.key to it.value } ?: mapOf(),
                        msgEntity.isIncoming
                    )
                }
            }
    }

    @Transactional
    fun processMessage(me: String, msg: OutgoingMsgDto): MsgDto {
        val updatedMsg = placeholderReplacer.replacePlaceholders(me, msg)
        sendAndSaveMessageForPresentContact(me, msg.dst, updatedMsg, true)
        sendAndSaveMessageForPresentContact(msg.dst, me, updatedMsg, false)
        return updatedMsg
    }

    private fun sendAndSaveMessageForPresentContact(src: String, dst: String, msg: OutgoingMsgDto, isOutgoing: Boolean) {
        val contact = contactRepository.findById(ContactId(src, dst))
        if (contact.isPresent) {
            doSendMsg(contact.get(), msg)
            val props = msg.properties.map { msgPropRepository.save(MsgProperty(it.key, it.value)) }.toMutableList()
            val savedMsg = msgRepository.save(MsgEntity(msg.ts, msg.content, isOutgoing, props))
            contact.get().messages?.add(savedMsg)
        }
    }

    private fun doSendMsg(contact: ContactEntity, msg: OutgoingMsgDto) {
       // Stub implementation
       when(contact){
           is SmsContactEntity -> println("Sending sms to "+ contact.number)
           is EmailContactEntity -> println("Sending email to "+ contact.email + " with subject: " + msg.properties["header"])
       }
    }
}