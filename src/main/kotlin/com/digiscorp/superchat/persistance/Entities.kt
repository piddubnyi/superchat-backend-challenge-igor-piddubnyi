package com.digiscorp.superchat.persistance

import java.time.Instant
import javax.persistence.*

@Embeddable
class ContactId(
    val src: String,
    val dst: String
) : java.io.Serializable

@Entity
class ContactEntity(
    @EmbeddedId
    val id: ContactId,
    @Column
    val name: String,

    @Column(nullable = true)
    @ManyToMany
    val messages: MutableList<MsgEntity>? = mutableListOf()
)

@Entity
class MsgEntity(
    @Column
    val ts: Instant,
    @Column
    val content: String,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int? = null,
)