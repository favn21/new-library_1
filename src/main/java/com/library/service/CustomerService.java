package com.library.service;

import com.library.api.model.ValidationErrorResponse;
import com.library.api.model.customer.CustomersResponse;
import com.library.api.model.customer.FilterCustomersEnum;
import com.library.api.model.exceptions.CustomBadRequestException;
import com.library.db.entity.CustomerEntity;
import com.library.db.repository.CustomerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  @Autowired
  CustomerRepository customerRepository;

  public List<CustomersResponse> getAllCustomers(FilterCustomersEnum filter) {
    List<CustomerEntity> entities = customerRepository.findAll();

    List<CustomersResponse> customers = entities.stream()
        .map(entity -> new CustomersResponse(entity.getFirstName(), entity.getFamilyName(),
            entity.getSex(), entity.getStatus(), entity.getBirthDate()))
        .collect(Collectors.toList());

    switch (filter) {
      case SEX_M -> {
        return customers.stream()
            .filter(customer -> customer.getSex().equals("М"))
            .collect(Collectors.toList());
      }
      case SEX_W -> {
        return customers.stream()
            .filter(customer -> customer.getSex().equals("Ж"))
            .collect(Collectors.toList());
      }
      case MARRIED -> {
        return customers.stream()
            .filter(customer -> customer.getStatus().equals("Free"))
            .collect(Collectors.toList());
      }
      default -> throw new CustomBadRequestException(
          new ValidationErrorResponse("100", "Выбранный тип фильтрации не поддерживается")
      );
    }
  }
}