package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.Character;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class Base {

    @Autowired
    private CharactersService charactersService;

    @Autowired
    private AbilitiesService abilitiesService;

    @Autowired
    private MobsService mobsService;

    @Autowired
    private MobsAbilityService mobsAbilityService;





    @GetMapping("/A/abilities")
    public ResponseEntity<List<Abilities>> getAllAbilities() {
        List<Abilities> abilities = abilitiesService.findAllAbilities();
        return ResponseEntity.ok(abilities);
    }

    @GetMapping("/A/names")
    public ResponseEntity<List<Abilities.AbilitiesIdName>> getAllAbilitiesName() {
        List<Abilities.AbilitiesIdName> abilities = abilitiesService.findNames();
        return ResponseEntity.ok(abilities);
    }

    @PostMapping("/A")
    public ResponseEntity<Abilities> createAbility(@RequestBody Abilities ability) {
        Abilities created = abilitiesService.appAbility(ability);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @DeleteMapping("/A/{id}")
    public ResponseEntity<Void> deleteAbility(@PathVariable Long id) {
        try {
            abilitiesService.deletAbility(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/C/hell")
    public ResponseEntity<List<com.example.demo.Character>> getAllCharacters() {
        List<com.example.demo.Character> characters = charactersService.getAllCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/C/names")
    public ResponseEntity<List<com.example.demo.Character.CharacterIdName>> getAllName() {
        List<com.example.demo.Character.CharacterIdName> characters = charactersService.getAllNames();
        return ResponseEntity.ok(characters);
    }

    @PostMapping("/C")
    public ResponseEntity<com.example.demo.Character> createUser(@RequestBody com.example.demo.Character user) {
        com.example.demo.Character createdUser = charactersService.appCharacter(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/C/{id}")
    public ResponseEntity<com.example.demo.Character> updateUser(@PathVariable Long id, @RequestBody com.example.demo.Character userDetails) {
        try {
            Character updatedUser = charactersService.updateCharacter(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/C/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            charactersService.deletCharacter(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/M/mobs")
    public ResponseEntity<List<Mobs>> getAllMobs() {
        List<Mobs> mobs = mobsService.findAllMobs();
        return ResponseEntity.ok(mobs);
    }

    @GetMapping("/M/names")
    public ResponseEntity<List<Mobs.MobsIdName>> getAllMobsName() {
        List<Mobs.MobsIdName> mobs = mobsService.findNames();
        return ResponseEntity.ok(mobs);
    }

    @PostMapping("/M")
    public ResponseEntity<Mobs> createMobs(@RequestBody Mobs mobs) {
        Mobs created = mobsService.appMobs(mobs);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @DeleteMapping("/M/{id}")
    public ResponseEntity<Void> deleteMobs(@PathVariable Long id) {
        try {
            mobsService.deletMobs(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/MA")
    public ResponseEntity<List<Mobs_Ability>> getAllMobsAbilities() {
        return ResponseEntity.ok(mobsAbilityService.getAll());
    }

    // Получить связь по ID
    @GetMapping("/MA/{id}")
    public ResponseEntity<?> getMobsAbilityById(@PathVariable Long id) {
        return mobsAbilityService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobs_Ability not found"));
    }

    // Создать новую связь
    @PostMapping("/MA")
    public ResponseEntity<Mobs_Ability> createMobsAbility(@RequestBody Mobs_Ability mobsAbility) {
        Mobs_Ability created = mobsAbilityService.save(mobsAbility);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Обновить существующую связь
    @PutMapping("/MA/{id}")
    public ResponseEntity<Mobs_Ability> updateMobsAbility(@PathVariable Long id, @RequestBody Mobs_Ability mobsAbilityDetails) {
        return mobsAbilityService.getById(id).map(existing -> {
            existing.setMobs(mobsAbilityDetails.getMobs());
            existing.setAbility(mobsAbilityDetails.getAbility());
            Mobs_Ability updated = mobsAbilityService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Удалить связь
    @DeleteMapping("/MA/{id}")
    public ResponseEntity<Void> deleteMobsAbility(@PathVariable Long id) {
        try {
            mobsAbilityService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Найти связи по ID моба
    @GetMapping("/MA/mobs/{mobsId}")
    public ResponseEntity<List<Mobs_Ability>> getByMobsId(@PathVariable Long mobsId) {
        List<Mobs_Ability> list = mobsAbilityService.findByMobsId(mobsId);
        return ResponseEntity.ok(list);
    }

    // Найти связи по ID способности
    @GetMapping("/MA/ability/{abilityId}")
    public ResponseEntity<List<Mobs_Ability>> getByAbilityId(@PathVariable Long abilityId) {
        List<Mobs_Ability> list = mobsAbilityService.findByAbilityId(abilityId);
        return ResponseEntity.ok(list);
    }

}
