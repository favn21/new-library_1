package com.library.api.model.getbooks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
@AllArgsConstructor
public class AuthorResponse {

  private Long id;

  @JacksonXmlProperty(localName = "first_name")
  private String firstName;

  @JacksonXmlProperty(localName = "second_name")
  private String secondName;

  @JacksonXmlProperty(localName = "family_name")
  private String familyName;

  @JacksonXmlProperty(localName = "birth_date")
  private String birthDate;
}