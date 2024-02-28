package com.sahay.loan.repo;

import com.sahay.loan.entity.CbsPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CbsPostingRepo extends JpaRepository<CbsPosting, Integer> {


    Optional<CbsPosting> findTopByStatus(int status);

}
