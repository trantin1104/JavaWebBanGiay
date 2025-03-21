package com.tuan1.demo.controller;


import com.tuan1.demo.model.Order;
import com.tuan1.demo.model.OrderStatus;
import com.tuan1.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();

        // Debug: In ra danh sách đơn hàng để kiểm tra thứ tự
        System.out.println("Danh sách đơn hàng:");
        orders.forEach(order -> System.out.println(order.getOrderDate() + " - " + order.getOrderCode()));

        model.addAttribute("orders", orders);
        return "admin/order/index";
    }




    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/admin/orders?error=notfound";
        }
        model.addAttribute("order", order);
        return "admin/order/detail"; // Tên file Thymeleaf
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order != null) {
                order.setStatus(OrderStatus.valueOf(status)); // Cập nhật trạng thái
                orderService.saveOrder(order); // Lưu vào database
                redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật trạng thái!");
        }
        return "redirect:/admin/order"; // Quay lại danh sách đơn hàng
    }




}
