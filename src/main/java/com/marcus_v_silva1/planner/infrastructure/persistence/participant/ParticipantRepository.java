package com.marcus_v_silva1.planner.infrastructure.persistence.participant;

import java.util.List;
import java.util.UUID;

import com.marcus_v_silva1.planner.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, UUID>{
    List<Participant> findByTripId(UUID tripId);
}