package com.hckk.sgl.orderservice.mapper;

import com.hckk.sgl.orderservice.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    int createOrder(Order order);

    int deleteOrder(int id);

    List<Order> findOrdersInDate(Map<String, Object> params);

    int addRemark(Order order);

    Order findOrderById(int id);
}
