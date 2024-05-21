package com.sahay.customer.repo;

import com.sahay.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByCustomerAccount(String customerAccount);

    Optional<List<Customer>> findAllByCustomerAccountOrStatus(String accountNumber , Integer status);


}
