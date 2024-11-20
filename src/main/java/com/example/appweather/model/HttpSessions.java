package com.example.appweather.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "http_sessions")
public class HttpSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "sessionId")
    private String sessionId;
    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;
    @Column(name = "expiresAt")
    private Date date;
}
