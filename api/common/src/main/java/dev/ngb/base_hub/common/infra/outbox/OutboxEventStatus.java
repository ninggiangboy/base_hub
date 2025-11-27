package dev.ngb.base_hub.common.infra.outbox;

public enum OutboxEventStatus {
  PENDING,
  PROCESSED,
  FAILED
}
