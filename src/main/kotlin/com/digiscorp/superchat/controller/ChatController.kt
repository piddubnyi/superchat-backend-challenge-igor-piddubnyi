package com.digiscorp.superchat.controller

import com.digiscorp.superchat.dto.ContactDto
import com.digiscorp.superchat.dto.ConversationMsgDto
import com.digiscorp.superchat.dto.OutgoingMsgDto
import com.digiscorp.superchat.dto.MsgDto
import com.digiscorp.superchat.service.ChatService
import com.digiscorp.superchat.service.ContactService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class ChatController(val chatService: ChatService, val contactService: ContactService) {
    @PostMapping("/contacts")
    fun createContact(@RequestBody req: ContactDto, @RequestHeader me: String): ResponseEntity<ContactDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(me, req))
    }

    @GetMapping("/contacts")
    fun listContacts(@RequestHeader me: String): ResponseEntity<List<ContactDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.listContacts(me))
    }

    @PostMapping("/msgs")
    fun sendMsg(@RequestBody msg: OutgoingMsgDto, @RequestHeader me: String): ResponseEntity<MsgDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.processMessage(me, msg))
    }

    @GetMapping("/msgs")
    fun listConversations(@RequestHeader me: String): ResponseEntity<Map<String, List<ConversationMsgDto>?>> {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.conversations(me))
    }
}