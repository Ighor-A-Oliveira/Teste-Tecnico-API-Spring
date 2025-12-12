package com.ighor.teste_tecnico_api_spring.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    private String ispb;
    private String name;
    private String code;
    private String fullName;
}
