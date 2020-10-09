package ru.otus.cache.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "T_ADDRESS")
public class AddressDataSet {


    @Id
    @GeneratedValue
    private long id;

    @Column(name = "street")
    private String street;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AddressDataSet that = (AddressDataSet) object;
        return id == that.id &&
                Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street);
    }

}



