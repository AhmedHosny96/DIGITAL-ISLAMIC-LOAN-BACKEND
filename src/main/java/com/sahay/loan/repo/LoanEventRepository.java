package com.sahay.loan.repo;

import com.sahay.loan.entity.LoanEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanEventRepository extends JpaRepository<LoanEvent , Integer> {
}
