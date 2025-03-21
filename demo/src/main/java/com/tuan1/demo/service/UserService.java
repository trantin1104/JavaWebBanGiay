package com.tuan1.demo.service;

import com.tuan1.demo.model.Role;
import com.tuan1.demo.model.User;
import com.tuan1.demo.repository.IRoleRepository;
import com.tuan1.demo.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository,
                       IRoleRepository roleRepository, RoleService roleService,

                       @Lazy PasswordEncoder passwordEncoder) { // Thay thế BCryptPasswordEncoder
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Lưu người dùng mới vào DB sau khi mã hóa mật khẩu.
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Lấy danh sách tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Gán vai trò mặc định cho người dùng dựa trên tên đăng nhập.
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    Optional<Role> role = roleRepository.findById(2L);

                    role.ifPresentOrElse(
                            user.getRoles()::add, // Gán role cho user
                            () -> { throw new RuntimeException("Role ID = 2 not found"); }
                    );
                    userRepository.save(user); // Lưu user sau khi gán role
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );
    }


    // Tải thông tin chi tiết người dùng để xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    // Tìm kiếm người dùng theo username.
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) { // Thêm phương thức này
        return userRepository.findByEmail(email);
    }

    public User createUser(String username, String password, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }
        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            throw new RuntimeException("Role not found!");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
