package com.dato.chatty.model

import com.fasterxml.jackson.annotation.JsonProperty

data class HelloMessage(@JsonProperty("name") val name: String)
