package com.example.stomp.handler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@SneakyThrows
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// CONNECT하는 상황이라면, 토큰을 검증해줍니다.

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if(accessor.getCommand() == StompCommand.CONNECT) {
			if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token")))
				throw new AccessDeniedException("");
		}
		return message;
	}
}