package com.sahay.customer.repo;

import com.sahay.customer.model.CustomerBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<CustomerBranch, Integer> {

    Optional<CustomerBranch> findByBranchCode(String branchCode);
}
