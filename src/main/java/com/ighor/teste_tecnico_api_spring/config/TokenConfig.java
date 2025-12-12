package com.ighor.teste_tecnico_api_spring.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ighor.teste_tecnico_api_spring.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    private String secret = "secret";

    Algorithm algorithm = Algorithm.HMAC256(secret);

    public String  generateToken(User user) {
        //Cria objeto JWT
        return JWT.create()
                //Adiciona uma claim chamada "userId" dentro do JWT: "userID":123
                .withClaim("userId", user.getId())
                //Define quem é o usuário
                .withSubject(user.getEmail())
                //O token vai expirar em 24 horas (86400 segundos)
                .withExpiresAt(Instant.now().plusSeconds(86400))
                //Define quando o token foi emitido.
                .withIssuedAt(Instant.now())
                //Assina o token com o algoritmo HMAC256 usando uma senha
                .sign(algorithm);
    }

    //Valida o token JWT
    public Optional<JWTUserData> validateToken(String token) {

        try {
            //Recria o algoritmo usado na assinatura do token
            //Se o token tiver sido alterado, a validação vai falhar.
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //Valida e decodifica o token
            DecodedJWT decode = JWT.require(algorithm)
                    .build().verify(token);

            //Constrói o objeto com os dados do usuário extraídos do token
            return Optional.of(JWTUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject())
                    .build());

        } catch (JWTVerificationException ex) {
            //Se o token for inválido → Retorna vazio
            return Optional.empty();
        }
    }
}
