package com.example.vladstp.repository;

import com.example.vladstp.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//создали базовый репозиторий, который позволяет получить либо полный список полей либо найти по id
public interface MessageRepository extends CrudRepository<Message, Long> {
/* создали новый метод для поиска в БД по тэгу. название метода взято на основе документации спринга,
   чтобы БД понимала какие команды нужно дать БД для поиска, в нашем случае, по тэгу */
    List<Message> findByTag(String tag);
}