package com.library.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "customer")
@Accessors(chain = true)
public class CustomerEntity {

  @Id
  @Column(name = "id")
  private long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "family_name")
  private String familyName;

  @Column(name = "sex")
  private String sex;

  @Column(name = "status")
  private String status;

  @Column(name = "birth_date")
  private LocalDate birthDate;
}