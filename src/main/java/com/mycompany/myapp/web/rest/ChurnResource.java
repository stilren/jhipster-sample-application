package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Churn;
import com.mycompany.myapp.repository.ChurnRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Churn}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChurnResource {

    private final Logger log = LoggerFactory.getLogger(ChurnResource.class);

    private static final String ENTITY_NAME = "churn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurnRepository churnRepository;

    public ChurnResource(ChurnRepository churnRepository) {
        this.churnRepository = churnRepository;
    }

    /**
     * {@code POST  /churns} : Create a new churn.
     *
     * @param churn the churn to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churn, or with status {@code 400 (Bad Request)} if the churn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/churns")
    public ResponseEntity<Churn> createChurn(@RequestBody Churn churn) throws URISyntaxException {
        log.debug("REST request to save Churn : {}", churn);
        if (churn.getId() != null) {
            throw new BadRequestAlertException("A new churn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Churn result = churnRepository.save(churn);
        return ResponseEntity.created(new URI("/api/churns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /churns} : Updates an existing churn.
     *
     * @param churn the churn to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churn,
     * or with status {@code 400 (Bad Request)} if the churn is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churn couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/churns")
    public ResponseEntity<Churn> updateChurn(@RequestBody Churn churn) throws URISyntaxException {
        log.debug("REST request to update Churn : {}", churn);
        if (churn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Churn result = churnRepository.save(churn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, churn.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /churns} : get all the churns.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churns in body.
     */
    @GetMapping("/churns")
    public List<Churn> getAllChurns(@RequestParam(required = false) String filter) {
        if ("cohort-is-null".equals(filter)) {
            log.debug("REST request to get all Churns where cohort is null");
            return StreamSupport
                .stream(churnRepository.findAll().spliterator(), false)
                .filter(churn -> churn.getCohort() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Churns");
        return churnRepository.findAll();
    }

    /**
     * {@code GET  /churns/:id} : get the "id" churn.
     *
     * @param id the id of the churn to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churn, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/churns/{id}")
    public ResponseEntity<Churn> getChurn(@PathVariable Long id) {
        log.debug("REST request to get Churn : {}", id);
        Optional<Churn> churn = churnRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(churn);
    }

    /**
     * {@code DELETE  /churns/:id} : delete the "id" churn.
     *
     * @param id the id of the churn to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/churns/{id}")
    public ResponseEntity<Void> deleteChurn(@PathVariable Long id) {
        log.debug("REST request to delete Churn : {}", id);
        churnRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
