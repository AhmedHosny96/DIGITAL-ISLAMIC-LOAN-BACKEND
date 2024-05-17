package com.sahay.customer.repo;

import com.sahay.customer.model.CustomerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {


    List<CustomerDocument> findCustomerDocumentByStatus(int status);

    List<CustomerDocument> findCustomerDocumentByCustomerId(int customerId);
}
