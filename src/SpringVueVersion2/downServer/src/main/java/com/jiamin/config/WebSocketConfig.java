package com.jiamin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //无需特殊处理，直接广播给所有客户端
        //simple程序中用于对所有用户广播,user用于单点消息发送
        registry.enableSimpleBroker("/simple","/user");
        //需要处理器方法特殊处理
        registry.setApplicationDestinationPrefixes("/complex");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //浏览器通过/chat申请websocket连接
        registry.addEndpoint("/chat").withSockJS();
    }
}
