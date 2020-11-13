package io.github.uonagent.crudable;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface EventedCrudable extends Crudable {

  @Override
  default <E> boolean add(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    if (doAdd(rowClass, row)) {
      getEventHandler().onAdd(rowClass, row);
      return true;
    }
    return false;
  }

  @Override
  default <E> Optional<? extends E> update(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    final Optional<? extends E> oldOptional = doUpdate(rowClass, row);
    oldOptional.ifPresent(old -> getEventHandler().onUpdate(rowClass, old, row));
    return oldOptional;
  }

  @Override
  default @NotNull <E> Optional<? extends E> delete(@NotNull Class<? extends E> rowClass, @NotNull E row) {
    final Optional<? extends E> optionalDeleted = doDelete(rowClass, row);
    optionalDeleted.ifPresent(d -> getEventHandler().onDelete(rowClass, d));
    return optionalDeleted;
  }

  <E> boolean doAdd(@NotNull Class<? extends E> rowClass, @NotNull E row);

  <E> Optional<? extends E> doUpdate(@NotNull Class<? extends E> rowClass, @NotNull E row);

  <E> @NotNull Optional<? extends E> doDelete(@NotNull Class<? extends E> rowClass, @NotNull E row);

  @NotNull EventHandler getEventHandler();

}
