package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cohort;
import com.mycompany.myapp.repository.CohortRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cohort}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CohortResource {

    private final Logger log = LoggerFactory.getLogger(CohortResource.class);

    private static final String ENTITY_NAME = "cohort";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CohortRepository cohortRepository;

    public CohortResource(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

    /**
     * {@code POST  /cohorts} : Create a new cohort.
     *
     * @param cohort the cohort to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cohort, or with status {@code 400 (Bad Request)} if the cohort has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cohorts")
    public ResponseEntity<Cohort> createCohort(@RequestBody Cohort cohort) throws URISyntaxException {
        log.debug("REST request to save Cohort : {}", cohort);
        if (cohort.getId() != null) {
            throw new BadRequestAlertException("A new cohort cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cohort result = cohortRepository.save(cohort);
        return ResponseEntity.created(new URI("/api/cohorts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cohorts} : Updates an existing cohort.
     *
     * @param cohort the cohort to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cohort,
     * or with status {@code 400 (Bad Request)} if the cohort is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cohort couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cohorts")
    public ResponseEntity<Cohort> updateCohort(@RequestBody Cohort cohort) throws URISyntaxException {
        log.debug("REST request to update Cohort : {}", cohort);
        if (cohort.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cohort result = cohortRepository.save(cohort);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cohort.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cohorts} : get all the cohorts.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cohorts in body.
     */
    @GetMapping("/cohorts")
    public ResponseEntity<List<Cohort>> getAllCohorts(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Cohorts");
        Page<Cohort> page;
        if (eagerload) {
            page = cohortRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = cohortRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cohorts/:id} : get the "id" cohort.
     *
     * @param id the id of the cohort to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cohort, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cohorts/{id}")
    public ResponseEntity<Cohort> getCohort(@PathVariable Long id) {
        log.debug("REST request to get Cohort : {}", id);
        Optional<Cohort> cohort = cohortRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cohort);
    }

    /**
     * {@code DELETE  /cohorts/:id} : delete the "id" cohort.
     *
     * @param id the id of the cohort to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cohorts/{id}")
    public ResponseEntity<Void> deleteCohort(@PathVariable Long id) {
        log.debug("REST request to delete Cohort : {}", id);
        cohortRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
