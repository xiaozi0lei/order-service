package com.hckk.sgl.orderservice.entity.dto;

import com.hckk.sgl.orderservice.entity.Order;
import lombok.Data;

import java.util.List;

/**
 * @author Sun Guolei 2018/6/21 14:54
 */
@Data
public class OrderListDto {
    // 订单列表
    private List<Order> orderList;
    // 订单总数
    private int orderTotal;
    // 腾达订单
    private int orderTD;
    // 日航订单
    private int orderRH;
}
