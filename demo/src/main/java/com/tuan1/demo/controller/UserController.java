package com.tuan1.demo.controller;

import com.tuan1.demo.model.User;
import com.tuan1.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {
        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            return "register";
        }

        // Kiểm tra username và email đã tồn tại chưa
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("errors", List.of("Username đã tồn tại!"));
            return "register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errors", List.of("Email đã được sử dụng!"));
            return "register";
        }

        // Lưu người dùng và gán vai trò mặc định
        userService.save(user);
        userService.setDefaultRole(user.getUsername());

        return "redirect:/login";
    }
}
