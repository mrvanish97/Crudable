package io.github.uonagent.crudable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class MapCrudable implements EventedCrudable, Serializable {

  private static class DummyEventHandler implements EventHandler {

    @Override
    public <E> void onAdd(@NotNull Class<? extends E> rowClass, @NotNull E added) {

    }

    @Override
    public <E> void onUpdate(@NotNull Class<? extends E> rowClass, @NotNull E oldRow, @NotNull E newRow) {

    }

    @Override
    public <E> void onDelete(@NotNull Class<? extends E> rowClass, @NotNull E row) {

    }
  }

  private final @NotNull Supplier<Map<Class<?>, Map<Object, ?>>> tables;
  private final @NotNull EventHandler eventHandler;

  private static @NotNull Supplier<Map<Class<?>, Map<Object, ?>>> createDefaultTableSupplier() {
    final Map<Class<?>, Map<Object, ?>> localTable = new ConcurrentHashMap<>();
    return () -> localTable;
  }

  public MapCrudable() {
    eventHandler = new DummyEventHandler();
    tables = createDefaultTableSupplier();
  }

  public MapCrudable(@NotNull Supplier<Map<Class<?>, Map<Object, ?>>> tables) {
    this.tables = tables;
    eventHandler = new DummyEventHandler();
  }

  public MapCrudable(@NotNull EventHandler eventHandler) {
    this.eventHandler = eventHandler;
    tables = createDefaultTableSupplier();
  }

  public MapCrudable(@NotNull Supplier<Map<Class<?>, Map<Object, ?>>> tables, @NotNull EventHandler eventHandler) {
    this.tables = tables;
    this.eventHandler = eventHandler;
  }

  @SuppressWarnings("unchecked")
  protected <E> @NotNull Map<Object, E> getTable(@NotNull Class<? extends E> rowClass) {
    return (Map<Object, E>) tables.get().computeIfAbsent(rowClass, k -> new ConcurrentHashMap<Object, E>());
  }

  @Override
  public <E> boolean doAdd(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    if (rowClass.isAssignableFrom(row.getClass())) {
      final Map<Object, ? super E> table = getTable(rowClass);
      final Optional<?> optionalUnique = getUniqueValue(row, Object.class);
      if (optionalUnique.isPresent() && !table.containsKey(optionalUnique.get())) {
        table.put(optionalUnique.get(), row);
        return true;
      }
    }
    return false;
  }

  @Override
  public <E> Optional<? extends E> doUpdate(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    if (rowClass.isAssignableFrom(row.getClass())) {
      final Map<Object, E> table = getTable(rowClass);
      final Optional<?> optionalUnique = getUniqueValue(row, Object.class);
      if (optionalUnique.isPresent() && table.containsKey(optionalUnique.get())) {
        Optional<? extends E> optionalOld = Optional.of(table.get(optionalUnique.get()));
        optionalOld.ifPresent(old -> table.put(optionalUnique.get(), row));
        return optionalOld;
      }
    }
    return Optional.empty();
  }

  @Override
  public @NotNull <E> Optional<? extends E> doDelete(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    if (rowClass.isAssignableFrom(row.getClass())) {
      final Map<Object, ? extends E> table = getTable(rowClass);
      final Optional<?> optionalUnique = getUniqueValue(row, Object.class);
      if (optionalUnique.isPresent() && table.containsKey(optionalUnique.get())) {
        return Optional.of(table.remove(optionalUnique.get()));
      }
    }
    return Optional.empty();
  }

  @Override
  public @NotNull EventHandler getEventHandler() {
    return eventHandler;
  }

  @Override
  public @NotNull <E> Stream<? extends E> getAll(@NotNull Class<? extends E> rowClass) {
    return getTable(rowClass).values()
        .stream();
  }

}
