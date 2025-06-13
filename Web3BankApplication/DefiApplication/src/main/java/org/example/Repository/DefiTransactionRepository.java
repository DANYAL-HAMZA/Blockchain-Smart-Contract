package org.example.Repository;

import org.example.Entity.DefiTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefiTransactionRepository extends JpaRepository<DefiTransaction,String> {
}
