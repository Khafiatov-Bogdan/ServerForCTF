package com.example.demo.service;

import com.example.demo.Mobs_Ability;
import com.example.demo.repository.MobsAbilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MobsAbilityService {

    private final MobsAbilityRepository mobsAbilityRepository;

    public MobsAbilityService(MobsAbilityRepository mobsAbilityRepository) {
        this.mobsAbilityRepository = mobsAbilityRepository;
    }

    // Получить все связи
    public List<Mobs_Ability> getAll() {
        return mobsAbilityRepository.findAll();
    }

    // Получить по ID
    public Optional<Mobs_Ability> getById(Long id) {
        return mobsAbilityRepository.findById(id);
    }

    // Сохранить или обновить
    public Mobs_Ability save(Mobs_Ability mobsAbility) {
        return mobsAbilityRepository.save(mobsAbility);
    }

    // Удалить по ID
    public void deleteById(Long id) {
        mobsAbilityRepository.deleteById(id);
    }

    // Найти все способности конкретного моба
    public List<Mobs_Ability> findByMobsId(Long mobsId) {
        return mobsAbilityRepository.findByMobsId(mobsId);
    }

    // Найти всех мобов с конкретной способностью
    public List<Mobs_Ability> findByAbilityId(Long abilityId) {
        return mobsAbilityRepository.findByAbilityId(abilityId);
    }
}
