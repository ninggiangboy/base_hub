package dev.ngb.base_hub.common.base.event;

public interface Event {
  default String orgId() {
    return null;
  }
}
