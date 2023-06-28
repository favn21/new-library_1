package com.library.controller;

import com.library.api.CustomerV1;
import com.library.api.model.customer.CustomersResponse;
import com.library.api.model.customer.FilterCustomersEnum;
import com.library.api.model.getbooks.BookResponse;
import com.library.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerV1 {

  private final CustomerService customerService;

  @Override
  public List<CustomersResponse> getAllCustomers(FilterCustomersEnum filter) {
    return customerService.getAllCustomers(filter);
  }
}