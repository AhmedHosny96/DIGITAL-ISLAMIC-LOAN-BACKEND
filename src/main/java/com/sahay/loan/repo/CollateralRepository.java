package com.sahay.loan.repo;

import com.sahay.loan.entity.Collateral;
import com.sahay.loan.entity.CollateralImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Integer> {

    Optional<Collateral> findByCustomerId(int customerId);

    Optional<Collateral> findByCollateralNumber(String collateralNumber);

}
