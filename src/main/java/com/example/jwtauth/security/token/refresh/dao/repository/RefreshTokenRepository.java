package com.example.jwtauth.security.token.refresh.dao.repository;


import com.example.jwtauth.security.token.refresh.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Transactional
    @Modifying
    @Query("update RefreshToken r set r.hashedToken = :hashedToken where r.id = :id")
    void updateHashedTokenById(@Param("hashedToken") String hashedToken, @Param("id") Long id);

}
