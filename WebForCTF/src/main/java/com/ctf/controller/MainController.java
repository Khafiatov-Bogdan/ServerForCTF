package com.ctf.controller;

import com.ctf.model.User;
import com.ctf.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);


        List<User> topUsers = userService.getTopUsers();
        model.addAttribute("topUsers", topUsers);

        return "index";
    }


    @GetMapping("/auth")
    public String authPage(@RequestParam(value = "register", required = false) String register,
                           @RequestParam(value = "error", required = false) String error,
                           Model model) {
        model.addAttribute("isLogin", register == null);
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        return "auth";
    }


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            logger.info("LOGIN ATTEMPT: username={}", username);

            Map<String, String> payload = Map.of(
                    "login", username,
                    "password", password
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://backend:8080/api/auth/login",
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                session.setAttribute("username", username);
                logger.info("LOGIN SUCCESS: username={}", username);
                return "redirect:/";
            } else {
                logger.warn("LOGIN FAILED: username={} status={}", username, response.getStatusCode());
                model.addAttribute("error", "Неверный логин или пароль");
                model.addAttribute("isLogin", true); // показать форму входа
                return "auth";
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("LOGIN FAILED: username={} reason=401 Unauthorized", username);
            model.addAttribute("error", "Неверный логин или пароль");
            model.addAttribute("isLogin", true); // показать форму входа
            return "auth";
        } catch (Exception e) {
            logger.error("LOGIN ERROR: username={} exception={}", username, e.getMessage());
            model.addAttribute("error", "Ошибка при входе: " + e.getMessage());
            model.addAttribute("isLogin", true);
            return "auth";
        }
    }












    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String email,
            HttpSession session,
            Model model) {

        try {

            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Имя пользователя обязательно");
                return showRegisterForm(model);
            }

            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("error", "Email обязателен");
                return showRegisterForm(model);
            }

            if (password == null || password.isEmpty()) {
                model.addAttribute("error", "Пароль обязателен");
                return showRegisterForm(model);
            }


            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Пароли не совпадают");
                return showRegisterForm(model);
            }


            if (password.length() < 6) {
                model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
                return showRegisterForm(model);
            }


            User user = userService.registerUser(username.trim(), password, email.trim());


            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());

            return "redirect:/?registration=success";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            return showRegisterForm(model);
        }
    }


    private String showRegisterForm(Model model) {
        model.addAttribute("isLogin", false);
        return "auth";
    }


    @GetMapping("/check-username")
    @ResponseBody
    public String checkUsername(@RequestParam String username) {
        if (username == null || username.trim().length() < 3) {
            return "invalid";
        }
        return userService.usernameExists(username.trim()) ? "exists" : "available";
    }


    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return "invalid";
        }
        return userService.emailExists(email.trim().toLowerCase()) ? "exists" : "available";
    }


    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/debug")
    public String debugPage(Model model) {
        String backendUrl = "http://backend:8080/debug/public/ping"; // контейнерное имя backend
        RestTemplate restTemplate = new RestTemplate();
        String backendResponse;
        try {
            backendResponse = restTemplate.getForObject(backendUrl, String.class);
        } catch (Exception e) {
            backendResponse = "Ошибка при обращении к бэку: " + e.getMessage();
        }

        model.addAttribute("pingResponse", backendResponse);
        return "debug"; // имя Thymeleaf-шаблона debug.html
    }



    @GetMapping("/category/{category}")
    public String showCategory(@PathVariable String category, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("category", category);

        switch (category.toLowerCase()) {
            case "pwn":
                return "pwn";
            case "web":
                return "web";
            case "crypto":
                return "crypto";
            default:
                return "redirect:/";
        }
    }
}