package com.digiscorp.superchat.dto

import java.time.Instant

data class ContactDto (val name: String, val email: String) {
}

open class MsgDto (val ts: Instant, val content: String) {
}

class IncomingMsgDto (val dst: String, ts: Instant, content: String): MsgDto(ts, content) {
}

