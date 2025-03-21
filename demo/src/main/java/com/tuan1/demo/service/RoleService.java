package com.tuan1.demo.service;

import com.tuan1.demo.model.Role;
import com.tuan1.demo.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final IRoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createRole(String roleName, String description) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Quyền đã tồn tại!");
        }
        Role role = new Role();
        role.setName(roleName);
        role.setDescription(description);
        return roleRepository.save(role);
    }


    public Role save(Role role) {  // ✅ Thêm phương thức này
        return roleRepository.save(role);
    }

    public void updateRole(Long roleId, String newRoleName, String description) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Quyền không tồn tại!"));
        role.setName(newRoleName);
        role.setDescription(description);
        roleRepository.save(role);
    }


    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Quyền không tồn tại!");
        }
        roleRepository.deleteById(roleId);
    }
}
