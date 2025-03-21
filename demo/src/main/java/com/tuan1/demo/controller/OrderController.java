package com.tuan1.demo.controller;

import com.tuan1.demo.model.CartItem;
import com.tuan1.demo.model.Order;
import com.tuan1.demo.service.CartService;
import com.tuan1.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/checkout")
    public String checkout() {
        return "cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(
            @RequestParam String customerName,
            @RequestParam String shippingAddress,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            Model model) {

        List<CartItem> cartItems = cartService.getCartItems();

        // Kiểm tra nếu giỏ hàng trống
        if (cartItems.isEmpty()) {
            return "redirect:/cart?error=emptyCart";
        }

        // Kiểm tra nếu thông tin không hợp lệ
        if (customerName.trim().isEmpty() || shippingAddress.trim().isEmpty() ||
                phoneNumber.trim().isEmpty() || email.trim().isEmpty()) {
            return "redirect:/order/checkout?error=invalidInput";
        }

        // Tạo đơn hàng và lấy thông tin
        Order order = orderService.createOrder(customerName, shippingAddress, phoneNumber, email, cartItems);

        // Truyền mã đơn hàng và ngày đặt hàng đến trang xác nhận
        model.addAttribute("order", order);
        model.addAttribute("message", "Your order has been successfully placed.");
        model.addAttribute("orderStatus", order.getStatus());

        return "cart/order-confirmation";
    }


    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

}