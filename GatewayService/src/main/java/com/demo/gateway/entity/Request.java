package com.demo.gateway.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "request")
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "url")
    private String url;

    @Column(name = "req_time")
    private Timestamp reqTime;

    @Column(name = "req_body")
    private String reqBody;

    @Column(name = "resp_time")
    private Timestamp respTime;

    @Column(name = "resp_body")
    private String respBody;

    @Column(name = "http_status")
    private int httpStatus;
}
