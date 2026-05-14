package com.classmate.usuarioservice.repository;

import com.classmate.usuarioservice.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByAuthUserId(Long authUserId);
}