package com.hckk.sgl.orderservice.service.Impl;

import com.hckk.sgl.orderservice.common.DateUtils;
import com.hckk.sgl.orderservice.common.ExcelUtils;
import com.hckk.sgl.orderservice.entity.Order;
import com.hckk.sgl.orderservice.mapper.OrderMapper;
import com.hckk.sgl.orderservice.service.OrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sun Guolei 2018/6/12 19:44
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public int createOrder(Order order) {
        // 订餐时间从下午 12 点到 16 点 15
        String start = "12:00:00";
        String end = "16:15:00";
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 组装 order 对象
        order.setOrderTime(now);

        if (DateUtils.betweenTime(now, start, end)) {

            // 查询当前用户是否已经订过餐
            HashMap<String, String> params = new HashMap<>();
            params.put("nickname", order.getNickname());
            int num = findOrdersInDate(params).size();

            if (num > 0) {
                logger.debug("今天你已定过餐了，请不要重新订购！");
                return 2;
            }
            logger.debug("time is {}", now);

            return orderMapper.createOrder(order);
        } else {
            logger.debug("订餐时间未到或已经截止！请在合适时间订餐！");
            return 0;
        }
    }

    @Override
    public int deleteOrder(int id) {
        return orderMapper.deleteOrder(id);
    }

    @Override
    public List<Order> findOrdersInDate(HashMap<String, String> params) {

        HashMap<String, Object> transferParams = new HashMap<String, Object>();

        LocalDateTime today = LocalDateTime.now();
        String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        transferParams.put("date", date);
        transferParams.put("address", params.get("address"));
        transferParams.put("nickname", params.get("nickname"));

        List<Order> orderList = orderMapper.findOrdersInDate(transferParams);

        // 时间格式转换，前端展示不支持 LocalDateTime
        orderList = orderList.stream().peek(order -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh:mm a");
            String time = order.getOrderTime().format(formatter);
            order.setFrontOrderTime(time);
        }).collect(Collectors.toList());

        return orderList;
    }

    @Override
    public Workbook exportExcel(HashMap<String, String> params, String departmentPName) {
        // 查找当天的订餐名单
        List<Order> orderList = findOrdersInDate(params);
        // 返回拼接好的 Excel 表
        return ExcelUtils.getExcel(orderList, departmentPName);
    }

    @Override
    public List<Order> findOrdersInDateByAddress(int address) {
        LocalDateTime today = LocalDateTime.now();
        String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, Object> transferParams = new HashMap<String, Object>();
        transferParams.put("date", date);
        transferParams.put("address", String.valueOf(address));

        return orderMapper.findOrdersInDate(transferParams);
    }

    @Override
    public int addRemark(Order order) {
        return orderMapper.addRemark(order);
    }

    @Override
    public Order findOrderById(int id) {
        return orderMapper.findOrderById(id);
    }
}
