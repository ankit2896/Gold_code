package com.freecharge.financial.dao.repositories;


import com.freecharge.financial.dao.entities.common.ClientCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCredentialRepository extends JpaRepository<ClientCredentials, Long> {

    ClientCredentials findByClientKey(String key);
}
