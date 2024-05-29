package com.sahay.loan.repo;

import com.sahay.loan.entity.Guarantor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuarantorRepo extends JpaRepository<Guarantor, Long> {

    Optional<Guarantor> findByCustomerAccount(String accountNumber);

    Optional<Guarantor> findGuarantorByGuarantorAccount(String accountNumber);
    
    Optional<List<Guarantor>> findGuarantorByStatus(int status);
}
