package com.marcus_v_silva1.planner.infrastructure.persistence.activity;

import com.marcus_v_silva1.planner.domain.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByTripId(UUID tripId);

}

