package io.github.uonagent.crudable;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.Stream;

class CrudableTest {

  @Test
  void superTest() {
    final Crudable crudable = new MapCrudable(new EventHandler() {
      @Override
      public <E> void onAdd(@NotNull Class<? extends E> rowClass, @NotNull E added) {
        System.out.printf("Added: %s\t%s\n", added, rowClass.getSimpleName());
      }

      @Override
      public <E> void onUpdate(@NotNull Class<? extends E> rowClass, @NotNull E oldRow, @NotNull E newRow) {
        System.out.printf("Updated from: %s\nto: %s\t%s\n", oldRow, newRow, rowClass.getSimpleName());
      }

      @Override
      public <E> void onDelete(@NotNull Class<? extends E> rowClass, @NotNull E row) {
        System.out.printf("Deleted: %s\t%s\n", row, rowClass.getSimpleName());
      }
    });

    crudable.add(new Person("Vasia", 13, "0", "ibas"));
    crudable.add(new Person("KYRIL", 23, "1", "ibas"));
    crudable.add(new Person("vmushtsincom", 23, "2", "doma"));
    crudable.add(new Person("sssss", 49, "3", "ghetto"));
    crudable.add(new Person("homeless", 49, "4", "none"));

    crudable.add(new Residence("ibas"));
    crudable.add(new Residence("doma"));
    crudable.add(new Residence("ghetto"));

    crudable.add(new Friendship(1, "0", "1"));
    crudable.add(new Friendship(2, "1", "2"));
    crudable.add(new Friendship(3, "3", "2"));
    crudable.add(new Friendship(4, "2", "0"));
    crudable.add(Person.class, new Relationships(5, "4", "1", "loh"));

    crudable.update(new Person("KYRIL", 22, "1", "iba"));

    crudable.deleteIf(Person.class, person -> person.getAge() < 18);

    crudable.addAll(Stream.of(new Person("Debil", 22, "0", "iba")));

    crudable.getByForeignKey(new Friendship(1, "0", "1"), "first", Person.class)
        .ifPresent(System.out::println);

    crudable.getAll(Person.class)
        .max((person1, person2) -> {
          final Function<Person, Long> getFriendsCount = person -> Stream.concat(
              crudable.getBy(Friendship.class, "first", person.getId()),
              crudable.getBy(Friendship.class, "second", person.getId())
          ).count();
          return getFriendsCount.apply(person1).compareTo(getFriendsCount.apply(person2));
        })
        .flatMap((Person person) -> crudable.<Person, String>getUniqueValue(person, String.class))
        .ifPresent(id -> crudable.deleteByUniqueKey(Person.class, id));
    crudable.getAll(Person.class).forEach(System.out::println);

  }

}