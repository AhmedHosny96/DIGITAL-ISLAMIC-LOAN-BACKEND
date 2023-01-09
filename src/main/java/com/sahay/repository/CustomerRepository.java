package com.sahay.repository;

import com.sahay.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT contact FROM CUSTOMER_CONTACT_MODE WHERE contact =?1", nativeQuery = true)
    String findCustomerByContact(String mobileNumber);

    @Query(value = "select IDENT_NO FROM CUSTOMER_IDENTIFICATION$AUD WHERE IDENT_NO =?1" , nativeQuery = true)
    String findCustomerById(String idNo);
}
