package com.popov.fintrack.wallet.model;

import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
public class Wallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<Budget> budgets;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<Invitation> invitations;

    private String name;
    private Double balance;
    private String currency;
}
