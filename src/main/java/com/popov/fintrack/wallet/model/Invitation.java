package com.popov.fintrack.wallet.model;

import com.popov.fintrack.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Invitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @OneToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    private String token = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
