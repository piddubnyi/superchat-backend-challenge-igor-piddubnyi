package com.digiscorp.superchat.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import java.time.Instant

@JsonTypeInfo(include = As.EXISTING_PROPERTY, property = "type", use = Id.NAME, visible = true)
@JsonSubTypes(
    Type(name = "SMS", value = SmsContact::class),
    Type(name = "EMAIL", value = EmailContact::class)
)
open class ContactDto (val name: String, val type: ChannelType) {
}

class SmsContact (val number: String, name: String, type: ChannelType): ContactDto(name, type) {
}
class EmailContact (val email: String, name: String, type: ChannelType): ContactDto(name, type) {
}
enum class ChannelType{
    SMS,
    EMAIL
}
open class MsgDto (val ts: Instant, var content: String, val properties: Map<String, String>) {
}

class OutgoingMsgDto (val dst: String, ts: Instant, content: String, properties: Map<String, String>)
    : MsgDto(ts, content, properties) {
}

class ConversationMsgDto (ts: Instant, content: String, properties: Map<String, String>, val isOutgoing: Boolean)
    : MsgDto(ts, content, properties) {
}

