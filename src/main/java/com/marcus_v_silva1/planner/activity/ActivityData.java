package com.marcus_v_silva1.planner.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(UUID id, String title ,LocalDateTime  occursAt) {
}
