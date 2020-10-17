package ru.otus.war.core.dto;

import ru.otus.war.core.model.User;

public class UserDto {
    private long id;
    private String name;
    private String password;

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
}
