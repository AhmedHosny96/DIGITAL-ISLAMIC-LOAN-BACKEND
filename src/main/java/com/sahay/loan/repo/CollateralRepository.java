package com.sahay.loan.repo;

import com.sahay.loan.entity.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Long> {

    Optional<Collateral> findByCustomerId(int customerId);
    Optional<Collateral> findByCollateralNumber(String collateralNumber);


}
