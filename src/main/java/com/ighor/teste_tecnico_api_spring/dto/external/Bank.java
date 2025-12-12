package com.ighor.teste_tecnico_api_spring.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    private String ispb;
    private String name;
    private String code;
    private String fullName;
}
