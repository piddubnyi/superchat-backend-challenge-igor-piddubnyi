package com.digiscorp.superchat.persistance

import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<ContactEntity, ContactId> {
    fun findAllByIdSrc(src: String): List<ContactEntity>
}

interface MsgRepository : JpaRepository<MsgEntity, Int> {

}