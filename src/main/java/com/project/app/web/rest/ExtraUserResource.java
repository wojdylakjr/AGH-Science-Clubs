package com.project.app.web.rest;

import com.project.app.domain.ExtraUser;
import com.project.app.repository.ExtraUserRepository;
import com.project.app.security.AuthoritiesConstants;
import com.project.app.service.ExtraUserService;
import com.project.app.service.MailService;
import com.project.app.service.UserService;
import com.project.app.service.dto.ExtraUserDAO;
import com.project.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.project.app.domain.ExtraUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExtraUserResource {

    private final Logger log = LoggerFactory.getLogger(com.project.app.web.rest.ExtraUserResource.class);

    private static final String ENTITY_NAME = "extraUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraUserRepository extraUserRepository;
    private final ExtraUserService extraUserService;
    private final MailService mailService;
    private final UserService userService;

    public ExtraUserResource(
        ExtraUserRepository extraUserRepository,
        ExtraUserService extraUserService,
        MailService mailService,
        UserService userService
    ) {
        this.extraUserRepository = extraUserRepository;
        this.extraUserService = extraUserService;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * {@code POST  /extra-users} : Create a new extraUser.
     *
     * @param extraUserDAO the extraUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extraUser, or with status {@code 400 (Bad Request)} if the extraUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extra-users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ExtraUser> createExtraUser(@RequestBody ExtraUserDAO extraUserDAO) throws URISyntaxException {
        log.debug("REST request to save ExtraUser : {}", extraUserDAO);
        if (extraUserDAO.getId() != null) {
            throw new BadRequestAlertException("A new extraUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtraUser result = extraUserService.createExtraUser(extraUserDAO.getUser(), extraUserDAO.getBlock(), extraUserDAO.getField());
        mailService.sendCreationEmail(result.getUser());
        return ResponseEntity
            .created(new URI("/api/extra-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extra-users/:id} : Updates an existing extraUser.
     *
     * @param id        the id of the extraUser to save.
     * @param extraUser the extraUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraUser,
     * or with status {@code 400 (Bad Request)} if the extraUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extraUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extra-users/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ExtraUser> updateExtraUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtraUserDAO extraUserDAO
    ) throws URISyntaxException {
        log.debug("REST request to update ExtraUser : {}, {}", id, extraUserDAO);
        if (extraUserDAO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraUserDAO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExtraUser result = extraUserService.update(extraUserDAO.getUser(), extraUserDAO.getBlock(), extraUserDAO.getField());
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extraUserDAO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /extra-users/:id} : Partial updates given fields of an existing extraUser, field will ignore if it is null
     *
     * @param id        the id of the extraUser to save.
     * @param extraUser the extraUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraUser,
     * or with status {@code 400 (Bad Request)} if the extraUser is not valid,
     * or with status {@code 404 (Not Found)} if the extraUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the extraUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/extra-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ExtraUser> partialUpdateExtraUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtraUser extraUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExtraUser partially : {}, {}", id, extraUser);
        if (extraUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtraUser> result = extraUserRepository
            .findById(extraUser.getId())
            .map(existingExtraUser -> {
                if (extraUser.getBlock() != null) {
                    existingExtraUser.setBlock(extraUser.getBlock());
                }
                if (extraUser.getField() != null) {
                    existingExtraUser.setField(extraUser.getField());
                }

                return existingExtraUser;
            })
            .map(extraUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extraUser.getId().toString())
        );
    }

    /**
     * {@code GET  /extra-users} : get all the extraUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extraUsers in body.
     */
    @GetMapping("/extra-users")
    public List<ExtraUser> getAllExtraUsers() {
        log.debug("REST request to get all ExtraUsers");
        return extraUserRepository.findAll();
    }

    @GetMapping("/extra-users/current")
    public ExtraUser getCurrentExtraUser() {
        log.debug("REST request to get current ExtraUser");
        //        ExtraUser extraUser = extraUserService.getExtraUserByUserId(userService.getUserWithAuthorities().get().getId());
        return extraUserService.getExtraUserByUserId(userService.getUserWithAuthorities().get().getId());
    }

    /**
     * {@code GET  /extra-users/:id} : get the "id" extraUser.
     *
     * @param id the id of the extraUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extraUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extra-users/{id}")
    public ResponseEntity<ExtraUser> getExtraUser(@PathVariable Long id) {
        log.debug("REST request to get ExtraUser : {}", id);
        Optional<ExtraUser> extraUser = extraUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extraUser);
    }

    /**
     * {@code DELETE  /extra-users/:id} : delete the "id" extraUser.
     *
     * @param id the id of the extraUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extra-users/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteExtraUser(@PathVariable Long id) {
        log.debug("REST request to delete ExtraUser : {}", id);
        extraUserService.deleteUser(id);
        //        extraUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
