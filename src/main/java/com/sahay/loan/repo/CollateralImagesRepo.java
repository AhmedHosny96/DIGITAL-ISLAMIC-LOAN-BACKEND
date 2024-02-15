package com.sahay.loan.repo;

import com.sahay.loan.entity.CollateralImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollateralImagesRepo extends JpaRepository<CollateralImages , Long> {
}
