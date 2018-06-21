package com.hckk.sgl.orderservice.controller;

import com.hckk.sgl.orderservice.entity.Order;
import com.hckk.sgl.orderservice.entity.ReturnResult;
import com.hckk.sgl.orderservice.entity.dto.OrderListDto;
import com.hckk.sgl.orderservice.service.OrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Sun Guolei 2018/6/12 19:42
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/index")
    public ReturnResult<OrderListDto> index() {
        // 返回的结果实体
        ReturnResult<OrderListDto> result = new ReturnResult<>();
        // 订单列表实体
        OrderListDto orderListDto = new OrderListDto();

        HashMap<String, String> params = new HashMap<>();
        // 查询当日订单列表
        List<Order> orderList = orderService.findOrdersInDate(params);
        // 设置返回的订单列表
        orderListDto.setOrderList(orderList);
        // 订单总数
        int orderTotal = orderList.size();
        orderListDto.setOrderTotal(orderTotal);

        int orderTD = orderService.findOrdersInDateByAddress(1).size();
        // 腾达大厦数量 0:日航 1:腾达
        orderListDto.setOrderTD(orderTD);
        // 日航数量
        orderListDto.setOrderRH(orderTotal - orderTD);

        // 带上返回数据
        result.setData(orderListDto);
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public int create(Order order) {
        // mock 数据，临时解决方案
        order.setUsername("test");
        order.setDepartmentId(0);
        return orderService.createOrder(order);
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        logger.info("开始导出订餐表 Excel");
        // 获取拼接好的 Excel
        // 设置 Excel 下载的名字
        String fileName = "易鑫加班申请表-产品技术部-" + LocalDate.now() + ".xlsx";

        HashMap<String, String> params = new HashMap<>();
        exportExcelToUser(response, fileName, "产品技术部", params);

        logger.info("订餐表 Excel 导出成功 - {}", fileName);
    }

    // 未使用
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") int id) {
        //获取当前时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        int dateNow = Integer.parseInt(sdf.format(date));
        logger.debug("当前时间：" + dateNow);
        if (dateNow < 161500) {
            logger.info("取消订餐");
            orderService.deleteOrder(id);
            return "redirect:/order/index";
        } else {
            logger.info("超时不能取消订餐");
            return "redirect:/order/index";
        }

    }

    // 未使用
    @GetMapping("/remark/{id}")
    public String remark(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);
        return "order/remark";
    }

    // 未使用
    @PostMapping("/addRemark")
    public String addRemark(Order order) {

        orderService.addRemark(order);
        return "redirect:/order/index";
    }

    private void exportExcelToUser(HttpServletResponse response, String fileName,
                                   String firstDepartmentName, HashMap<String, String> params) throws IOException {
        OutputStream os = null;
        try (Workbook wb = orderService.exportExcel(params, firstDepartmentName)) {
            // 设置返回的是 Excel，提供下载
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, "utf-8"));
            os = response.getOutputStream();
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }
}
