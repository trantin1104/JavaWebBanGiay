package com.tuan1.demo.controller;

import com.tuan1.demo.model.User;
import com.tuan1.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/index";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        userService.createUser(username, password, role);
        return "redirect:/admin/user/index";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getUserProfile(Model model, Principal principal) {
        Optional<User> user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user.orElse(null));
        return "user_profile"; // Trả về trang user_profile.html
    }
}
