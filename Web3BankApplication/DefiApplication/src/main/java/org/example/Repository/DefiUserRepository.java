package org.example.Repository;

import org.example.Entity.DefiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DefiUserRepository extends JpaRepository<DefiUser, Long> {
    Boolean existsByEmail(String email);
     Boolean existsByWalletAddress(String walletAddress);

     DefiUser findByWalletAddress(String walletAddress);

    Optional<DefiUser> findByEmail(String email);
}
