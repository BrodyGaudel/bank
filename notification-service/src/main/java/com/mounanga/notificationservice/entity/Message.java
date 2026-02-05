package com.mounanga.notificationservice.entity;

import com.mounanga.notificationservice.enums.Template;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100,  nullable = false, updatable = false)
    private String subject;

    @Column(length = 250,  nullable = false, updatable = false)
    private String body;

    @Column(length = 100,  nullable = false, updatable = false)
    private String sender;

    @Column(length = 100,  nullable = false, updatable = false)
    private String receiver;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Template template;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;
}
