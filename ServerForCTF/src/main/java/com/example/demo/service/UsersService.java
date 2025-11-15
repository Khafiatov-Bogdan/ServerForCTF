package com.example.demo.service;

import com.example.demo.Users;
import com.example.demo.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users registerUser(String login, String rawPassword) {
        if (usersRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        Users user = new Users();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(rawPassword)); // хешируем пароль
        return usersRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserByLogin(String login) {
        return usersRepository.findByLogin(login);
    }

    public boolean checkPassword(String login, String rawPassword) {
        Optional<Users> userOpt = usersRepository.findByLogin(login);
        return userOpt.map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    public Users createOrUpdateUser(String login, String rawPassword) {
        Optional<Users> userOpt = usersRepository.findByLogin(login);
        Users user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            user.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            user = new Users();
            user.setLogin(login);
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        return usersRepository.save(user);
    }

}
