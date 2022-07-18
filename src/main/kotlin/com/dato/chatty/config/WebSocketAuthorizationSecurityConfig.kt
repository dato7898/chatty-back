package com.dato.chatty.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketAuthorizationSecurityConfig : AbstractSecurityWebSocketMessageBrokerConfigurer() {

    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry) {
        messages.anyMessage().authenticated()
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }

}