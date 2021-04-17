package com.example.vladstp.controller;

import com.example.vladstp.entity.Role;
import com.example.vladstp.entity.User;
import com.example.vladstp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
//    @GetMapping("/login")
//    public String login(){
//        return "login";
//    }

//этот GetMapping просто отображает страничку регистрации
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDb = userRepository.findByUsername(user.getUsername());
//если пользователь уже есть в БД, то мы сообщаем об этом на страничке регистрации
        if (userFromDb != null){
            model.put("message", "User exists!");
            return "registration";
        }
//сохраняет нового пользователя
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER)); //ЭТО НЕ ПОНЯЛ. что это такое???????
        userRepository.save(user);
        return "redirect:/login";
    }
}
