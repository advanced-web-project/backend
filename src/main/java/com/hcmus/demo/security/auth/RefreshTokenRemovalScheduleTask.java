package com.hcmus.demo.security.auth;

import com.hcmus.demo.token.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled task for removing expired refresh tokens from the database.
 * This class is annotated with @EnableScheduling to enable scheduling support.
 * It uses a fixed delay interval specified in the application properties to periodically delete expired tokens.
 */
@Component
@EnableScheduling
public class RefreshTokenRemovalScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenRemovalScheduleTask.class);

    @Autowired
    private RefreshTokenRepository repo;

    /**
     * Deletes expired refresh tokens from the database.
     * This method is scheduled to run at a fixed delay interval specified in the application properties.
     * It logs the number of deleted refresh tokens.
     */
    @Scheduled(fixedDelayString  = "${app.refresh-token.removal.interval}", initialDelay = 5000)
    @Transactional
    public void deleteExpireRefreshToken() {
        int rows = repo.deleteByExpiryTime();
        LOGGER.info("Number of deleted refresh tokens : " + rows);
    }
}