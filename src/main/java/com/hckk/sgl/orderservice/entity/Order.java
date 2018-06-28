package com.hckk.sgl.orderservice.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Sun Guolei 2018/6/21 10:52
 */
@Data
public class Order {
    private int id;
    private int address;

    private String username;
    private String nickname;
    private String reason;
    private String remark;

    private LocalDateTime orderTime;

    private int departmentId;
    private String departmentName;
    private int firstDepartmentId;
    private String firstDepartmentName;

    private String frontOrderTime;
}
