package com.everis.debitcardafiliation.dto;

import com.everis.debitcardafiliation.consumer.Webclient;
import java.time.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovementFrom {

  @NotBlank(message = "Debe ingresar su número de cuenta.")
  private String numberDebitCard;

  @NotBlank(message = "Debe ingresar su contraseña.")
  private String password;

  private LocalDateTime dateCreated = LocalDateTime.now(ZoneId.of("America/Lima"));
  private String numberAccount;

  private Double amount;

  public MovementFrom(String numberDebitCard, String password, Double amount, String state) {
    this.numberDebitCard = numberDebitCard;
    this.password = Webclient.logic.get().uri("/encriptBySha1/" + password).retrieve().bodyToMono(String.class).block();
    this.amount = amount;
  }

  public MovementFrom(String numberDebitCard, String numberAccount, Double amount) {
    this.numberDebitCard = numberDebitCard;
    this.numberAccount = numberAccount;
    this.amount = amount;
  }
}
