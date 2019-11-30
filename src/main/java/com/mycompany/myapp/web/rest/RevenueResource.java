package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Revenue;
import com.mycompany.myapp.repository.RevenueRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Revenue}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RevenueResource {

    private final Logger log = LoggerFactory.getLogger(RevenueResource.class);

    private static final String ENTITY_NAME = "revenue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RevenueRepository revenueRepository;

    public RevenueResource(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    /**
     * {@code POST  /revenues} : Create a new revenue.
     *
     * @param revenue the revenue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revenue, or with status {@code 400 (Bad Request)} if the revenue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revenues")
    public ResponseEntity<Revenue> createRevenue(@RequestBody Revenue revenue) throws URISyntaxException {
        log.debug("REST request to save Revenue : {}", revenue);
        if (revenue.getId() != null) {
            throw new BadRequestAlertException("A new revenue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Revenue result = revenueRepository.save(revenue);
        return ResponseEntity.created(new URI("/api/revenues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /revenues} : Updates an existing revenue.
     *
     * @param revenue the revenue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revenue,
     * or with status {@code 400 (Bad Request)} if the revenue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revenue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revenues")
    public ResponseEntity<Revenue> updateRevenue(@RequestBody Revenue revenue) throws URISyntaxException {
        log.debug("REST request to update Revenue : {}", revenue);
        if (revenue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Revenue result = revenueRepository.save(revenue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, revenue.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /revenues} : get all the revenues.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revenues in body.
     */
    @GetMapping("/revenues")
    public List<Revenue> getAllRevenues(@RequestParam(required = false) String filter) {
        if ("cohort-is-null".equals(filter)) {
            log.debug("REST request to get all Revenues where cohort is null");
            return StreamSupport
                .stream(revenueRepository.findAll().spliterator(), false)
                .filter(revenue -> revenue.getCohort() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Revenues");
        return revenueRepository.findAll();
    }

    /**
     * {@code GET  /revenues/:id} : get the "id" revenue.
     *
     * @param id the id of the revenue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revenue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revenues/{id}")
    public ResponseEntity<Revenue> getRevenue(@PathVariable Long id) {
        log.debug("REST request to get Revenue : {}", id);
        Optional<Revenue> revenue = revenueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(revenue);
    }

    /**
     * {@code DELETE  /revenues/:id} : delete the "id" revenue.
     *
     * @param id the id of the revenue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revenues/{id}")
    public ResponseEntity<Void> deleteRevenue(@PathVariable Long id) {
        log.debug("REST request to delete Revenue : {}", id);
        revenueRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
