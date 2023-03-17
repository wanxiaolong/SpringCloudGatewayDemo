package com.demo.first.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Log {
    private int level;
    private String aLogId;
    private String msg;
}
