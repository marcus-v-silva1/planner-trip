package com.marcus_v_silva1.planner.activity;

import com.marcus_v_silva1.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;

    public ActivityResponce saveActivity(ActivityRequestPayload payload, Trip trip){
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(),trip);
        this.repository.save(newActivity);

        return new ActivityResponce(newActivity.getId());

    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId){
        return this.repository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
