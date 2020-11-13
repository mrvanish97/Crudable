package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;
import io.github.uonagent.crudable.annotations.ForeignKey;

import java.util.Objects;

public class Friendship {

  @Column(unique = true)
  int id;

  @Column(name = "first")
  @ForeignKey(foreignClass = Person.class)
  String personOneId;

  @Column(name = "second")
  @ForeignKey(foreignClass = Person.class)
  String personTwoId;

  public Friendship() {
  }

  public Friendship(int id, String personOneId, String personTwoId) {
    this.id = id;
    this.personOneId = personOneId;
    this.personTwoId = personTwoId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPersonOneId() {
    return personOneId;
  }

  public void setPersonOneId(String personOneId) {
    this.personOneId = personOneId;
  }

  public String getPersonTwoId() {
    return personTwoId;
  }

  public void setPersonTwoId(String personTwoId) {
    this.personTwoId = personTwoId;
  }

  @Override
  public String toString() {
    return "Friendship{" +
        "id=" + id +
        ", personOneId='" + personOneId + '\'' +
        ", personTwoId='" + personTwoId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Friendship that = (Friendship) o;
    return id == that.id &&
        Objects.equals(personOneId, that.personOneId) &&
        Objects.equals(personTwoId, that.personTwoId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, personOneId, personTwoId);
  }

}
