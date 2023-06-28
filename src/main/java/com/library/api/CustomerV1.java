package com.library.api;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.library.api.model.customer.CustomersResponse;
import com.library.api.model.customer.FilterCustomersEnum;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("customer")
public interface CustomerV1 {

  @GetMapping(value = "/customers/{filter}", produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<CustomersResponse> getAllCustomers(
      @PathVariable(name = "filter", required = true) FilterCustomersEnum filter);
}
