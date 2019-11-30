package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserAcquisition;
import com.mycompany.myapp.repository.UserAcquisitionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserAcquisition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserAcquisitionResource {

    private final Logger log = LoggerFactory.getLogger(UserAcquisitionResource.class);

    private static final String ENTITY_NAME = "userAcquisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAcquisitionRepository userAcquisitionRepository;

    public UserAcquisitionResource(UserAcquisitionRepository userAcquisitionRepository) {
        this.userAcquisitionRepository = userAcquisitionRepository;
    }

    /**
     * {@code POST  /user-acquisitions} : Create a new userAcquisition.
     *
     * @param userAcquisition the userAcquisition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAcquisition, or with status {@code 400 (Bad Request)} if the userAcquisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-acquisitions")
    public ResponseEntity<UserAcquisition> createUserAcquisition(@RequestBody UserAcquisition userAcquisition) throws URISyntaxException {
        log.debug("REST request to save UserAcquisition : {}", userAcquisition);
        if (userAcquisition.getId() != null) {
            throw new BadRequestAlertException("A new userAcquisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAcquisition result = userAcquisitionRepository.save(userAcquisition);
        return ResponseEntity.created(new URI("/api/user-acquisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-acquisitions} : Updates an existing userAcquisition.
     *
     * @param userAcquisition the userAcquisition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAcquisition,
     * or with status {@code 400 (Bad Request)} if the userAcquisition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAcquisition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-acquisitions")
    public ResponseEntity<UserAcquisition> updateUserAcquisition(@RequestBody UserAcquisition userAcquisition) throws URISyntaxException {
        log.debug("REST request to update UserAcquisition : {}", userAcquisition);
        if (userAcquisition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserAcquisition result = userAcquisitionRepository.save(userAcquisition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAcquisition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-acquisitions} : get all the userAcquisitions.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAcquisitions in body.
     */
    @GetMapping("/user-acquisitions")
    public List<UserAcquisition> getAllUserAcquisitions(@RequestParam(required = false) String filter) {
        if ("cohort-is-null".equals(filter)) {
            log.debug("REST request to get all UserAcquisitions where cohort is null");
            return StreamSupport
                .stream(userAcquisitionRepository.findAll().spliterator(), false)
                .filter(userAcquisition -> userAcquisition.getCohort() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all UserAcquisitions");
        return userAcquisitionRepository.findAll();
    }

    /**
     * {@code GET  /user-acquisitions/:id} : get the "id" userAcquisition.
     *
     * @param id the id of the userAcquisition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAcquisition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-acquisitions/{id}")
    public ResponseEntity<UserAcquisition> getUserAcquisition(@PathVariable Long id) {
        log.debug("REST request to get UserAcquisition : {}", id);
        Optional<UserAcquisition> userAcquisition = userAcquisitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAcquisition);
    }

    /**
     * {@code DELETE  /user-acquisitions/:id} : delete the "id" userAcquisition.
     *
     * @param id the id of the userAcquisition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-acquisitions/{id}")
    public ResponseEntity<Void> deleteUserAcquisition(@PathVariable Long id) {
        log.debug("REST request to delete UserAcquisition : {}", id);
        userAcquisitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
