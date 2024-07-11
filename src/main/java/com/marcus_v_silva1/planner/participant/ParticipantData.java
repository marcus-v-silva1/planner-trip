package com.marcus_v_silva1.planner.participant;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, Boolean isConfirmed) {

}