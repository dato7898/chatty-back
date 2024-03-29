package com.dato.chatty.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotAllowedException(message: String): AbstractGraphQLException(message)