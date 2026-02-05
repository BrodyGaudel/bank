package com.mounanga.notificationservice.repository;

import com.mounanga.notificationservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
