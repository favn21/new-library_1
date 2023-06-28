package com.library.db.repository;

import com.library.db.entity.CustomerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  List<CustomerEntity> findAll();
}