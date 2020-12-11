package ru.otus.ms.app.core.dto;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.ms.app.core.model.User;

public class UserDto extends ResultDataType {
    private long id;
    private String name;
    private String password;
    private int age;


    public UserDto(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public UserDto() {
    }

    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getName(), user.getPassword());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getAge(), userDto.getPassword(), false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
