package com.classmate.notificacionservice.repository;

import com.classmate.notificacionservice.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository
        extends JpaRepository<Notificacion, Long> {
}