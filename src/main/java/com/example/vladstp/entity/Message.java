package com.example.vladstp.entity;
import javax.persistence.*;

@Entity //даёт знать спрингу, что это сущность, которую нужно сохранять в БД

public class Message {
/*этот кусок кода объясняетполе фрейворку, что id будет индентификатором (для отличия записей в одной табличке)*/
    @Id //обязательная аннотация
    @GeneratedValue(strategy= GenerationType.AUTO) //фреймворк с БД сами разбираются в каком порядке будут генерировтаься id
//далее мы задаём поля в которых у нас будут храниться id текст и тэги
    private long id;
    private String text;
    private String tag;
    @ManyToOne(fetch = FetchType.EAGER) //4й. указываем, что одному пользователю соответствует множество сообщений
    // (fetch = FetchType.EAGER) fetch - режим, означает, что каждый раз как мы получаем сообщение, мы хотим получать информацию об авторе
    @JoinColumn(name = "user_id") //4й это нужно чтобы в БД это поле называлось use_id а не author ID как было бы по дефолту
    private User author; //4й добавляем автора сообщения

    private String filename; //7й привязываем файл к сообщению и даём ему имя для поиска файла на жестком диске

    public Message() {
    /*Создали пустой конструктор иначе спринг и стоящие за ним фрейворки не смогут создать данные классы.
    Если у нас есть @Entity, то должен быть конструктор без параметров */
    }
//далее создаём аксесуары, поля геттеры и сеттеры
    public Message(String text, String tag, User user) {
        this.author = user; //4й Добавляем автора
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author !=null ? author.getUsername() : "<none>"; //4й проверяем есть ли у нас автор у сообщений
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
