package com.example.demo.controller;

import com.example.demo.service.CharactersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping()
public class Base {
    @Autowired
    private CharactersService charactersService;

    @GetMapping("/hell")
    public ResponseEntity<List<Character>> getAllCharacters(){
        List<Character> characters = charactersService.getAllCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/names")
    public ResponseEntity<List<Character.CharacterIdName>> getAllName(){
        List<Character.CharacterIdName> characters = charactersService.getAllNames();
        return ResponseEntity.ok(characters);
    }

    @PostMapping
    public ResponseEntity<Character> createUser(@RequestBody Character user) {
        Character createdUser = charactersService.appCharacter(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Character> updateUser(@PathVariable Long id, @RequestBody Character userDetails) {
        try {
            Character updatedUser = charactersService.updateCharacter(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            charactersService.deletCharacter(id);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }

}
