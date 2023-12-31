package com.practice.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.practice.chat.handler.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	SocketHandler socketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/chatting");
	}
	
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
	ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
	container.setMaxTextMessageBufferSize(50000000);
	container.setMaxBinaryMessageBufferSize(50000000);
	return container;
	}
}