package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;
import io.github.uonagent.crudable.annotations.ForeignKey;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.github.uonagent.crudable.Utils.*;

public interface Crudable {

  <E> boolean add(@NotNull Class<? extends E> rowClass, @NotNull E row);

  default <E> boolean add(@NotNull E row) {
    return add(row.getClass(), row);
  }

  <E> @NotNull Stream<? extends E> getAll(@NotNull Class<? extends E> rowClass);

  <E> Optional<? extends E> update(@NotNull Class<? extends E> rowClass, @NotNull E row);

  @SuppressWarnings("unchecked")
  default <E> Optional<? extends E> update(@NotNull E row) {
    return (Optional<? extends E>) update(row.getClass(), row);
  }

  <E> @NotNull Optional<? extends E> delete(@NotNull Class<? extends E> rowClass, @NotNull E row);

  @SuppressWarnings("unchecked")
  default <E> @NotNull Optional<? extends E> delete(@NotNull E row) {
    return (Optional<? extends E>) delete(row.getClass(), row);
  }

  default <E> void addAll(@NotNull Stream<? extends E> rows) {
    rows.forEach(this::add);
  }

  default <E> void addAll(@NotNull Class<? extends E> rowClass,
                          @NotNull Stream<? extends E> rows) {
    rows.forEach(r -> add(rowClass, r));
  }

  default <E> @NotNull Stream<? extends E> find(@NotNull Class<? extends E> rowClass,
                                                @NotNull Predicate<? super E> predicate) {
    return getAll(rowClass).filter(predicate);
  }

  default <E, V> @NotNull Stream<? extends E> getBy(@NotNull Class<? extends E> rowClass,
                                                    @NotNull String columnName,
                                                    @NotNull V columnValue) {
    final Field columnField = Arrays.stream(rowClass.getDeclaredFields())
        .filter(getPredicateByColumnName(columnName))
        .findAny()
        .orElse(null);
    if (columnField != null) {
      return find(rowClass, e -> {
        try {
          columnField.setAccessible(true);
          return columnField.get(e).equals(columnValue);
        } catch (IllegalAccessException illegalAccessException) {
          return false;
        }
      });
    } else {
      return Stream.empty();
    }
  }

  default <E, U> @NotNull Optional<? extends E> getByUniqueKey(@NotNull Class<? extends E> rowClass,
                                                               @NotNull U uniqueKey) {
    final Field uniqueField = Arrays.stream(rowClass.getDeclaredFields())
        .filter(getUniqueFieldPredicate())
        .findAny()
        .orElse(null);
    if (uniqueField != null) {
      return find(rowClass, e -> {
        try {
          uniqueField.setAccessible(true);
          return uniqueField.get(e).equals(uniqueKey);
        } catch (IllegalAccessException illegalAccessException) {
          return false;
        }
      }).findAny();
    } else {
      return Optional.empty();
    }
  }

  default <E, U> @NotNull Optional<? extends E> deleteByUniqueKey(@NotNull Class<? extends E> rowClass,
                                                                  @NotNull U uniqueKey) {
    final Optional<? extends E> optional = getByUniqueKey(rowClass, uniqueKey);
    if (optional.isPresent()) {
      return delete(optional.get());
    } else {
      return Optional.empty();
    }
  }

  default <E> @NotNull Stream<? super E> deleteIf(@NotNull Class<? extends E> rowClass,
                                                  @NotNull Predicate<? super E> predicate) {
    return find(rowClass, predicate)
        .map(this::delete)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  default <E, F> @NotNull Optional<? extends F> getByForeignKey(@NotNull E row, @NotNull String columnName, @NotNull Class<? extends F> foreignRowClass) {
    final Field foreignField = Arrays.stream(row.getClass().getDeclaredFields())
        .filter(field -> {
          field.setAccessible(true);
          return field.isAnnotationPresent(ForeignKey.class)
              && field.isAnnotationPresent(Column.class)
              && (field.getName().equals(columnName) || field.getAnnotation(Column.class).name().equals(columnName));
        })
        .findAny()
        .orElse(null);
    if (foreignField != null) {
      try {
        foreignField.setAccessible(true);
        return getByUniqueKey(foreignRowClass, foreignField.get(row));
      } catch (IllegalAccessException e) {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  default <E, U> @NotNull Optional<? extends U> getUniqueValue(@NotNull E row, @NotNull Class<? extends U> uniqueValueClass) {
    return Arrays.stream(getFields(row))
        .filter(field -> {
          field.setAccessible(true);
          return field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).unique();
        })
        .findAny()
        .map(field -> {
          try {
            final Object uniqueObj = field.get(row);
            if (uniqueValueClass.isAssignableFrom(uniqueObj.getClass())) {
              return uniqueValueClass.cast(uniqueObj);
            }
          } catch (IllegalAccessException ignored) {
          }
          return null;
        });
  }

}
