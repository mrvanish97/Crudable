package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;

import java.util.Objects;

public class Relationships extends Friendship {

  @Column
  private String type;

  public Relationships() {}

  public Relationships(int id, String personOneId, String personTwoId, String type) {
    super(id, personOneId, personTwoId);
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Relationships{" +
        "type='" + type + '\'' +
        ", id=" + id +
        ", personOneId='" + personOneId + '\'' +
        ", personTwoId='" + personTwoId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Relationships that = (Relationships) o;
    return Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), type);
  }
}
