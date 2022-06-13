package com.digiscorp.superchat.service

import com.digiscorp.superchat.dto.ContactDto
import com.digiscorp.superchat.persistance.ContactEntity
import com.digiscorp.superchat.persistance.ContactId
import com.digiscorp.superchat.persistance.ContactRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ContactService(val contactRepository: ContactRepository) {

    @Transactional
    fun createContact(me: String, contact: ContactDto): ContactDto {
        contactRepository.save(ContactEntity(ContactId(me, contact.email), contact.name))
        contactRepository.save(ContactEntity(ContactId(contact.email, me), me))
        return contact
    }

    fun listContacts(me: String): List<ContactDto> {
        return contactRepository.findAllByIdSrc(me).map { ContactDto(it.name, it.id.dst) }
    }
}