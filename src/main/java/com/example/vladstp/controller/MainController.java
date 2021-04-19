package com.example.vladstp.controller;

import com.example.vladstp.entity.Message;
import com.example.vladstp.entity.User;
import com.example.vladstp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

// Контроллер - программный модуль, который слушает от клиента запросы и возвращает какие-то данные
@Controller
public class MainController {

    @Autowired //инциализирует в конструкторе текущего класса какой-либо тип данных
    // конструкция спринга, которая помогает получать значения из репозитория
    private MessageRepository messageRepository;
/*  3й урок. Убрали эту часть когда, тк переделываем эту тестовую страницу под регистрацию пользователей
    @GetMapping("/greeting") //общаемся со страничкой greeting
    //метод ожидает на вход параметр name с дефолтным значением World (он не обязательный)
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World")
                    //далее в поле name кладём то что получили из RequestParam
            String name, Map<String, Object> model) {
        model.put("name", name); //model.addAttribute поменяли на put. Model мы складываем данные, которые хотим вернуть клиенту
        return "greeting";
    }
 */
    @Value("${upload.path}") //7й Спринг ищет в пропертях значение переменной по указанному в {} имени и ...
    private String uploadPath; //7й ... Вставляем в эту переменную

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    //В файл main возвращаем заданное значение
    //3е занятие. Делаем main под регистрацию пользователей
    @GetMapping("/main") //общаемся со главной страничкой - main
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages; // = messageRepository.findAll(); //получаем из репозитория данные (сообщения)
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model,
            @RequestParam("file") MultipartFile file //7й
    ) throws IOException { //7й добавили через alt+ins встроке  Exception в file.transferTo(new File ...
        if (text != null && !text.isEmpty() && tag != null && !tag.isEmpty()) {
            Message message = new Message(text, tag, user);
            if (file !=null && !file.getOriginalFilename().isEmpty()){
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()){ // 7й проверяем есть ли директория для загрузки
                    uploadDir.mkdir(); // 7й если нет директории то создаём
                }

                String uuidFile = UUID.randomUUID().toString(); //7й создаём уникальное имя файла с помощью UUID
                String resultFilename = uuidFile + "." + file.getOriginalFilename(); //7й склеиваем UUID и имя файла = получаем уникальное имя файла
                file.transferTo(new File( uploadPath + "/" + resultFilename)); //7й загружаем файл
                message.setFilename(resultFilename);
            }
            //первым шагом мы сохраняем в БД
            messageRepository.save(message);
        }
        //далее, вторым шагом, возвращаем список сообщений, который лежит в БД
        Iterable<Message> messages = messageRepository.findAll(); //4й Добавили для "автора"
        model.put("messages", messages);
        //отдали пользователю
        return "main";
    }
    /* перенесли отработку фильтра через if в в Getmapping для работы фильтра на страничке main
    * @PostMapping("filter")
    * public String filter(@RequestParam String filter, Map<String, Object> model) {
    *   Iterable<Message> messages;
    *   if (filter !=null && !filter.isEmpty()) {
    *       messages = messageRepository.findByTag(filter);
    *   } else {
    *       messages = messageRepository.findAll();
    *   }
    *   model.put("messages", message);
    *   return "main";
    * }
    * */

}