package com.ata.bankapp.repo;

import com.ata.bankapp.model.Bill;
import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.model.BillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAccountIdAndBillTypeAndStatus(Long accountId, BillType billType, BillStatus status);
}
