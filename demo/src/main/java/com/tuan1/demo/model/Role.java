package com.tuan1.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements GrantedAuthority {

    public static final Long USER_ID = 1L; // Thêm hằng số USER_ID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", length = 50, nullable = false, unique = true)
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @Size(max = 250, message = "Description must be less than 250 characters")
    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }
}