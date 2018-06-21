package com.hckk.sgl.orderservice.service;

import com.hckk.sgl.orderservice.entity.Order;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    int createOrder(Order order);

    int deleteOrder(int id);

    List<Order> findOrdersInDate(HashMap<String, String> params);

    Workbook exportExcel(HashMap<String, String> params, String departmentPName);

    List<Order> findOrdersInDateByAddress(int address);

    int addRemark(Order order);

    Order findOrderById(int id);
}
