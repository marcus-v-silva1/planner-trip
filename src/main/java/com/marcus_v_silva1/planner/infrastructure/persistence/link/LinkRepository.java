package com.marcus_v_silva1.planner.infrastructure.persistence.link;


import com.marcus_v_silva1.planner.domain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    public List<Link> findByTripId(UUID tripId);
}