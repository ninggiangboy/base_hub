package dev.ngb.base_hub.common.base.event;

public interface EventHandler<T extends Event> {
  void handle(T event);
}
