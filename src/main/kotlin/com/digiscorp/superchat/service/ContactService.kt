package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.ChannelType
import com.digiscorp.superchat.dto.ContactDto
import com.digiscorp.superchat.dto.EmailContact
import com.digiscorp.superchat.dto.SmsContact
import com.digiscorp.superchat.persistance.*
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ContactService(val contactRepository: ContactRepository) {
    @Transactional
    fun createContact(me: String, contact: ContactDto): ContactDto {
        when(contact){
            is SmsContact -> contactRepository.save(SmsContactEntity(ContactId(me, contact.number), contact.name, contact.number, mutableListOf()))
            is EmailContact -> contactRepository.save(EmailContactEntity(ContactId(me, contact.email), contact.name, contact.email, mutableListOf()))
        }
        return contact
    }

    fun listContacts(me: String): List<ContactDto?> {
        return contactRepository.findAllByIdSrc(me).map {
            when(it){
                is SmsContactEntity -> SmsContact(it.number, it.name, ChannelType.SMS)
                is EmailContactEntity -> EmailContact(it.email, it.name, ChannelType.EMAIL)
                else -> null
            }
        }
    }
}