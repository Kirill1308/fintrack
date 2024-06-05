package com.popov.fintrack.wallet.model;

import com.popov.fintrack.user.model.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
public class Wallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Member> members;

//    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
//    private Set<Budget> budgets;

    private String name;
    private Double balance;
    private String currency;
}
