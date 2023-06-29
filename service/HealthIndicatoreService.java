package com.freecharge.financial.service;

import com.freecharge.financial.dto.response.HealthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
public class HealthIndicatoreService {

    @Autowired
    private DataSource dataSource;

    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("OK 200", HttpStatus.OK);
    }

    // TODO : All integrated service health must check under deep health
    public ResponseEntity<HealthResponse> getDeepHealth() {

        if (isDBConnected()) {
            return new ResponseEntity<>(new HealthResponse("200", "UP"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new HealthResponse("500", "DOWN"), HttpStatus.OK);
    }

    private boolean isDBConnected() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Connection catalog : {}", connection.getCatalog());
            return true;
        } catch (SQLException ex) {
            log.error("SQLException caught while doing deep health check", ex);
            return false;
        }
    }
}
