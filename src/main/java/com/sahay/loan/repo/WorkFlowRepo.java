package com.sahay.loan.repo;

import com.sahay.loan.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowRepo extends JpaRepository<WorkFlow, Integer> {
    
}
