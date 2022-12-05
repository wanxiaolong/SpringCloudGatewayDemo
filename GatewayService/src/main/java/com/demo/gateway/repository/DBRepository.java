package com.demo.gateway.repository;

import com.demo.gateway.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface DBRepository extends JpaRepository<Request, Integer> {

    @Modifying
    @Query("UPDATE Request r SET r.respTime=:respTime, r.respBody=:respBody, r.httpStatus=:httpStatus WHERE id=:id")
    public int updateRequestById(@Param("respTime")Timestamp respTime,
                                 @Param("respBody")String respBody,
                                 @Param("httpStatus")Integer httpStatus,
                                 @Param("id")Integer id);
}
