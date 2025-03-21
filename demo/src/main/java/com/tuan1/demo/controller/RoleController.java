package com.tuan1.demo.controller;

import com.tuan1.demo.model.Role;
import com.tuan1.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin/role/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/role/create"; // Hiển thị trang tạo role
    }

    @PostMapping("/create")
    public String createRole(@RequestParam String roleName, @RequestParam String description) {
        roleService.createRole(roleName, description);
        return "redirect:/admin/role";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleService.save(role);
        return "redirect:/admin/role";
    }

    @GetMapping("/edit/{roleId}")
    public String showUpdateForm(@PathVariable Long roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "admin/role/edit";
    }


    @PostMapping("/edit")
    public String updateRole(@RequestParam Long roleId, @RequestParam String newRoleName, @RequestParam String description) {
        roleService.updateRole(roleId, newRoleName, description);
        return "redirect:/admin/role";
    }


    @PostMapping("/delete/{roleId}")
    public String deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return "redirect:/admin/role";
    }

}
