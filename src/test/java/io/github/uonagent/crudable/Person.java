package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;
import io.github.uonagent.crudable.annotations.ForeignKey;

import java.util.Objects;

public class Person {

  @Column
  private String name;

  @Column
  private int age;

  @Column(unique = true)
  private String id;

  @Column
  @ForeignKey(foreignClass = Residence.class)
  private String address;

  public Person() {}

  public Person(String name, int age, String id, String address) {
    this.name = name;
    this.age = age;
    this.id = id;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Person{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", id='" + id + '\'' +
        ", address='" + address + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return age == person.age &&
        Objects.equals(name, person.name) &&
        Objects.equals(id, person.id) &&
        Objects.equals(address, person.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, id, address);
  }
}
