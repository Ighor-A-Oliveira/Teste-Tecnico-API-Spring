package com.ighor.teste_tecnico_api_spring.config;

import lombok.Builder;
import lombok.Data;

@Builder
public record JWTUserData(Long userId, String email) {
}
