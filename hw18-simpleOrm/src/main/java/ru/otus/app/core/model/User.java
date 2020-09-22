package ru.otus.app.core.model;

import ru.otus.app.core.model.annotation.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {

    @Id
    private final long id;
    private final String name;
    private final int age;


    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
