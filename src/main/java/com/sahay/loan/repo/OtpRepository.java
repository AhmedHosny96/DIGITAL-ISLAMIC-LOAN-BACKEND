package com.sahay.loan.repo;

import com.sahay.loan.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Request, Integer> {


    Optional<Request> findByReferenceAndOtp(String accountNumber, String otp);

    Optional<Request> findByReference(String reference);

    @Query("SELECT e.parkReference FROM Request e WHERE e.reference = :reference")
    Optional<String> findParkReferenceByReference(@Param("reference") String reference);


    Optional<Request> findByStatusAndReference(int status, String reference);
    Optional<Request> findByStatusAndAccountNumber(int status, String accountNumber);



}
