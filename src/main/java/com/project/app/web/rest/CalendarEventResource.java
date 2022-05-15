package com.project.app.web.rest;

import com.project.app.domain.CalendarEvent;
import com.project.app.repository.CalendarEventRepository;
import com.project.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.project.app.domain.CalendarEvent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CalendarEventResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);

    private static final String ENTITY_NAME = "calendarEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarEventRepository calendarEventRepository;

    public CalendarEventResource(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    /**
     * {@code POST  /calendar-events} : Create a new calendarEvent.
     *
     * @param calendarEvent the calendarEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarEvent, or with status {@code 400 (Bad Request)} if the calendarEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendar-events")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@Valid @RequestBody CalendarEvent calendarEvent) throws URISyntaxException {
        log.debug("REST request to save CalendarEvent : {}", calendarEvent);
        if (calendarEvent.getId() != null) {
            throw new BadRequestAlertException("A new calendarEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarEvent result = calendarEventRepository.save(calendarEvent);
        return ResponseEntity
            .created(new URI("/api/calendar-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendar-events/:id} : Updates an existing calendarEvent.
     *
     * @param id the id of the calendarEvent to save.
     * @param calendarEvent the calendarEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarEvent,
     * or with status {@code 400 (Bad Request)} if the calendarEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendar-events/{id}")
    public ResponseEntity<CalendarEvent> updateCalendarEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CalendarEvent calendarEvent
    ) throws URISyntaxException {
        log.debug("REST request to update CalendarEvent : {}, {}", id, calendarEvent);
        if (calendarEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CalendarEvent result = calendarEventRepository.save(calendarEvent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calendarEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calendar-events/:id} : Partial updates given fields of an existing calendarEvent, field will ignore if it is null
     *
     * @param id the id of the calendarEvent to save.
     * @param calendarEvent the calendarEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarEvent,
     * or with status {@code 400 (Bad Request)} if the calendarEvent is not valid,
     * or with status {@code 404 (Not Found)} if the calendarEvent is not found,
     * or with status {@code 500 (Internal Server Error)} if the calendarEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calendar-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalendarEvent> partialUpdateCalendarEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CalendarEvent calendarEvent
    ) throws URISyntaxException {
        log.debug("REST request to partial update CalendarEvent partially : {}, {}", id, calendarEvent);
        if (calendarEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalendarEvent> result = calendarEventRepository
            .findById(calendarEvent.getId())
            .map(existingCalendarEvent -> {
                if (calendarEvent.getTitle() != null) {
                    existingCalendarEvent.setTitle(calendarEvent.getTitle());
                }
                if (calendarEvent.getStartDate() != null) {
                    existingCalendarEvent.setStartDate(calendarEvent.getStartDate());
                }
                if (calendarEvent.getEndDate() != null) {
                    existingCalendarEvent.setEndDate(calendarEvent.getEndDate());
                }
                if (calendarEvent.getPublicationDate() != null) {
                    existingCalendarEvent.setPublicationDate(calendarEvent.getPublicationDate());
                }
                if (calendarEvent.getDescription() != null) {
                    existingCalendarEvent.setDescription(calendarEvent.getDescription());
                }
                if (calendarEvent.getLink() != null) {
                    existingCalendarEvent.setLink(calendarEvent.getLink());
                }
                if (calendarEvent.getImageUrl() != null) {
                    existingCalendarEvent.setImageUrl(calendarEvent.getImageUrl());
                }
                if (calendarEvent.getStatus() != null) {
                    existingCalendarEvent.setStatus(calendarEvent.getStatus());
                }

                return existingCalendarEvent;
            })
            .map(calendarEventRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calendarEvent.getId().toString())
        );
    }

    /**
     * {@code GET  /calendar-events} : get all the calendarEvents.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendarEvents in body.
     */
    @GetMapping("/calendar-events")
    public List<CalendarEvent> getAllCalendarEvents(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CalendarEvents");
        return calendarEventRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /calendar-events/:id} : get the "id" calendarEvent.
     *
     * @param id the id of the calendarEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendar-events/{id}")
    public ResponseEntity<CalendarEvent> getCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to get CalendarEvent : {}", id);
        Optional<CalendarEvent> calendarEvent = calendarEventRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(calendarEvent);
    }

    /**
     * {@code DELETE  /calendar-events/:id} : delete the "id" calendarEvent.
     *
     * @param id the id of the calendarEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendar-events/{id}")
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to delete CalendarEvent : {}", id);
        calendarEventRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
