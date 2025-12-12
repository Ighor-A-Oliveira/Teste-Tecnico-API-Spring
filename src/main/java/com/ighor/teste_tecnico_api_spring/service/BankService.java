package com.ighor.teste_tecnico_api_spring.service;

import com.ighor.teste_tecnico_api_spring.dto.external.Bank;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class BankService {

    private final RestTemplate restTemplate;

    public BankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Verifica se o código do banco é válido
     */
    public boolean isBankValid(String bankCode) {
        String url = "https://brasilapi.com.br/api/banks/v1";
        Bank[] banks = restTemplate.getForObject(url, Bank[].class);

        if (banks == null) {
            return false;
        }

        return Arrays.stream(banks)
                .filter(bank -> bank.getCode() != null && !bank.getCode().isBlank())
                .anyMatch(bank -> {
                    try {
                        // Converte para int e formata para 3 dígitos (001, 033...)
                        String formattedCode = String.format("%03d", Integer.parseInt(bank.getCode()));
                        return formattedCode.equals(bankCode);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
    }
}
