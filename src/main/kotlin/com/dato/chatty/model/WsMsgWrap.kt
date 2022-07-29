package com.dato.chatty.model

data class WsMsgWrap(
    val type: String,
    val payload: Any?
)