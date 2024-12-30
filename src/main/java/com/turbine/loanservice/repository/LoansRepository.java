package com.turbine.loanservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turbine.loanservice.model.Loan;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {

}
