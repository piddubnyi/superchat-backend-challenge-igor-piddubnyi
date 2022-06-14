package com.digiscorp.superchat.persistance

import com.digiscorp.superchat.dto.ChannelType
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
    @Column
    val type: ChannelType,

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
        @Column
    val isIncoming: Boolean,
        @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int? = null,
)