package com.everis.debitcardafiliation.map;

import java.time.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementsMapper {

    private LocalDateTime dateCreated;
    private String type;
    private double amount;
    private String accountEmisor;
    private String accountRecep;

    public MovementsMapper(String accountEmisor, double amount) {
        this.dateCreated = LocalDateTime.now(ZoneId.of("America/Lima"));
        this.accountEmisor = accountEmisor;
        this.amount = amount;
        this.type = "Retiro";
    }
}
