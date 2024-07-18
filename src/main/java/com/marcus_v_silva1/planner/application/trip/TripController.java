package com.marcus_v_silva1.planner.application.trip;

import com.marcus_v_silva1.planner.domain.participant.ParticipantData;
import com.marcus_v_silva1.planner.application.activity.ActivityRequestPayload;
import com.marcus_v_silva1.planner.application.activity.ActivityResponce;
import com.marcus_v_silva1.planner.application.link.LinkRequestPayload;
import com.marcus_v_silva1.planner.application.link.LinkResponse;
import com.marcus_v_silva1.planner.application.participant.ParticipantCreateResponse;
import com.marcus_v_silva1.planner.application.participant.ParticipantRequestPayload;
import com.marcus_v_silva1.planner.domain.activity.ActivityData;
import com.marcus_v_silva1.planner.domain.trip.Trip;
import com.marcus_v_silva1.planner.infrastructure.persistence.trip.TripRepository;
import com.marcus_v_silva1.planner.usecase.activity.ActivityService;
import com.marcus_v_silva1.planner.usecase.link.LinkService;
import com.marcus_v_silva1.planner.usecase.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.marcus_v_silva1.planner.domain.link.LinkData;
import com.marcus_v_silva1.planner.domain.link.Link;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TripRepository repository;

    @Autowired
    private LinkService linkService;
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload){
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);

        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip );
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            this.repository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);


        }

        return ResponseEntity.notFound().build();

    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            this.repository.save(rawTrip);


            return ResponseEntity.ok(rawTrip);


        }

        return ResponseEntity.notFound().build();

    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponce> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ActivityResponce activityResponce = this.activityService.saveActivity(payload,rawTrip);

            return ResponseEntity.ok(activityResponce);
        }
        return ResponseEntity.notFound().build();

    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();


            ParticipantCreateResponse participantResponse  = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();

    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participantList);
    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            LinkResponse linkResponce = this.linkService.registerLink(payload,rawTrip);

            return ResponseEntity.ok(linkResponce);
        }
        return ResponseEntity.notFound().build();
    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }


}
