package com.example.vladstp.controller;

import com.example.vladstp.entity.Role;
import com.example.vladstp.entity.User;
import com.example.vladstp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user") //6й пометили на уровне класса чтобы не подписывать у каждого метода, что он держить путь /user
@PreAuthorize("hasAuthority('ADMIN')") //6й аннотация будет для каждого из методов перед выполнением проверять наличие прав
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping //6й отображаем перечень юзеров в списке пользователей на страничке userList
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}") //6й Метод для редактора пользователя на страничке userEdit. Отображает данные
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }
    @PostMapping //6й Метод для сохранения выбранных пользователем родей и имени
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form, //будем получать Map в котором ключи значения являются строками
            @RequestParam("userId" ) User user
    ){
        user.setUsername(username); //6й меняет имя пользователя, даём новое имя
//6й запрашиваем Роли и переводим из enum в строковый вид :
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
//6й проверяем что данная форма содержит роли для нашего пользователя
        user.getRoles().clear(); //6й перед проверкой очишаем роли пользователя
        for (String key : form.keySet()) {
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user); //6й сохраняем пользователя

        return "redirect:/user";
    }

}
