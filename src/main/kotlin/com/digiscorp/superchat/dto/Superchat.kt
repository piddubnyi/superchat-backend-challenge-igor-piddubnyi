package com.digiscorp.superchat.dto

import java.time.Instant

data class ContactDto (val name: String, val channelType: ChannelType, val channelId: String) {
}

enum class ChannelType{
    SMS,
    EMAIL,
    GOOGLE_CHAT,
    TELEGRAM
}
open class MsgDto (val ts: Instant, val content: String) {
}

class OutgoingMsgDto (val dst: String, ts: Instant, content: String): MsgDto(ts, content) {
}

class ConversationMsgDto (ts: Instant, content: String, val isIncoming: Boolean): MsgDto(ts, content) {
}

