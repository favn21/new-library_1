package com.library.api.model.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
@AllArgsConstructor
public class CustomersResponse {

  private String firstName;

  private String familyName;

  private String sex;

  private String status;

  private LocalDate birthDate;
}