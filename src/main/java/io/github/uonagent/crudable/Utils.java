package io.github.uonagent.crudable;

import io.github.uonagent.crudable.annotations.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

class Utils {

  private static @NotNull Predicate<? super Field> getFieldFilterPredicate(@Nullable String columnName,
                                                                           final boolean unique) {
    return field -> {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      Column column;
      return field.isAnnotationPresent(Column.class)
          && ((column = field.getAnnotation(Column.class)).unique() || !unique)
          && (columnName == null || column.name().equals(columnName) || field.getName().equals(columnName));

    };
  }

  public static @NotNull Predicate<? super Field> getUniqueFieldPredicate() {
    return getFieldFilterPredicate(null, true);
  }

  public static @NotNull Predicate<? super Field> getPredicateByColumnName(@NotNull String columnName) {
    return getFieldFilterPredicate(columnName, false);
  }

  public static  <T> Field[] getFields(T t) {
    final List<Field> fields = new ArrayList<>();
    Class<?> clazz = t.getClass();
    while (clazz != Object.class) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields.toArray(new Field[0]);
  }

}
