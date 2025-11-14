package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_user")
@Entity
public class User implements Serializable, UserDetails { 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String password;

    @Column(name = "telefone", nullable = false)
    private String phone;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "dataCriacao", nullable = false)
    private LocalDateTime registrationDate;

    @UpdateTimestamp
    @Column(name = "ultimoAcesso")
    private LocalDateTime lastAccess;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "matricula", nullable = false)
    private String registration;

    @Column(name = "especialidade")
    private String specialty;

    @Column(name = "registroProf")
    private String teacherRegistration;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inscricao> inscricoes;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Curso> cursosLecionados;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "token_expiry_time")
    private LocalDateTime tokenExpiryTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}