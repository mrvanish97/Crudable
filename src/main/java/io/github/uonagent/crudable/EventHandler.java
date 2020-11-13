package io.github.uonagent.crudable;

import org.jetbrains.annotations.NotNull;

public interface EventHandler {

  <E> void onAdd(@NotNull Class<? extends E> rowClass, @NotNull E added);

  <E> void  onUpdate(@NotNull Class<? extends E> rowClass, @NotNull E oldRow, @NotNull E newRow);

  <E> void onDelete(@NotNull Class<? extends E> rowClass, @NotNull E row);

}
