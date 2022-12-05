package com.demo.gateway.service;

import com.demo.gateway.config.AsyncConfig;
import com.demo.gateway.entity.Request;
import com.demo.gateway.repository.DBRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Future;

@Slf4j
@Service
@Transactional
public class DBService {

    @Autowired
    private DBRepository dbRepository;

    /**
     * save request body asynchronously
     */
    @SneakyThrows
    @Async(value = AsyncConfig.ASYNC_EXECUTOR_NAME)
    public Future<Integer> saveRequestAsync(String url, String body) {
        log.info("Async logging transaction request...");
        Timestamp now = Timestamp.from(Instant.now());
        String uuid = UUID.randomUUID().toString();
        Request r = new Request();
        r.setUrl(url);
        r.setUuid(uuid);
        r.setReqBody(body);
        r.setReqTime(now);
        dbRepository.save(r);
        log.info("Async logging transaction request completed. Saved id: {}", r.getId());
        return new AsyncResult<Integer>(r.getId());
    }

    /**
     * save response body asynchronously
     */
    @SneakyThrows
    @Async(value = AsyncConfig.ASYNC_EXECUTOR_NAME)
    public Future<Integer> saveResponseAsync(
            Future<Integer> idFuture, int httpStatus, String body) {
        log.info("Async logging transaction response...");
        Integer id = idFuture.get();
        Timestamp now = Timestamp.from(Instant.now());
        Integer rows = dbRepository.updateRequestById(now, body, httpStatus, id);
        if (rows > 0) {
            log.info("Async logging transaction response completed. Updated id: {}", id);
        }
        return new AsyncResult<>(rows);
    }
}
