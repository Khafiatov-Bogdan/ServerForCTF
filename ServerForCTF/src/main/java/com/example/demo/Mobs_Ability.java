package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "mobs_abilities")
public class Mobs_Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mobs_id")
    private Mobs mobs;

    @ManyToOne
    @JoinColumn(name = "ability_id")
    private Abilities ability;

    // ======= Геттеры и сеттеры =======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mobs getMobs() {
        return mobs;
    }

    public void setMobs(Mobs mobs) {
        this.mobs = mobs;
    }

    public Abilities getAbility() {
        return ability;
    }

    public void setAbility(Abilities ability) {
        this.ability = ability;
    }
}
