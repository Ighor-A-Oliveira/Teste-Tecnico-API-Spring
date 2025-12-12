package com.ighor.teste_tecnico_api_spring.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
