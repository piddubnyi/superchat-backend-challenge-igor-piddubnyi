package com.digiscorp.superchat.persistance

import java.time.Instant
import javax.persistence.*

@Embeddable
class ContactId(
    val src: String,
    val dst: String
) : java.io.Serializable

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class ContactEntity(
    @EmbeddedId
    open val id: ContactId,
    @Column
    open val name: String,
    @Column(nullable = true)
    @ManyToMany
    open val messages: MutableList<MsgEntity>? = mutableListOf()
)

@Entity
class SmsContactEntity(
        id: ContactId,
        name: String,
        @Column
        val number: String,
        messages: MutableList<MsgEntity>
) : ContactEntity(id, name, messages)

@Entity
class EmailContactEntity(
    id: ContactId,
    name: String,
    @Column
    val email: String,
    messages: MutableList<MsgEntity>
) : ContactEntity(id, name, messages)

@Entity
class MsgEntity(
    @Column
    val ts: Instant,
    @Column
    val content: String,
    @Column
    val isIncoming: Boolean,
    @Column(nullable = true)
    @OneToMany
    val properties: MutableList<MsgProperty>? = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int? = null
)
@Entity
class MsgProperty (
    @Column
    val key: String,
    @Column
    val value: String,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int? = null
)