package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;

import java.util.Objects;

public class Residence {

  @Column(unique = true)
  private String address;

  public Residence(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Residence{" +
        "address='" + address + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Residence residence = (Residence) o;
    return Objects.equals(address, residence.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address);
  }

}
