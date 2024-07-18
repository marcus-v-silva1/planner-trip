package com.marcus_v_silva1.planner.infrastructure.persistence.trip;

import com.marcus_v_silva1.planner.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

}
