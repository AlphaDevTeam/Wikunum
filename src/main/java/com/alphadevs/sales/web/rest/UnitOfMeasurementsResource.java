package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.UnitOfMeasurements;
import com.alphadevs.sales.service.UnitOfMeasurementsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.UnitOfMeasurementsCriteria;
import com.alphadevs.sales.service.UnitOfMeasurementsQueryService;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.sales.domain.UnitOfMeasurements}.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasurementsResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasurementsResource.class);

    private static final String ENTITY_NAME = "unitOfMeasurements";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasurementsService unitOfMeasurementsService;

    private final UnitOfMeasurementsQueryService unitOfMeasurementsQueryService;

    public UnitOfMeasurementsResource(UnitOfMeasurementsService unitOfMeasurementsService, UnitOfMeasurementsQueryService unitOfMeasurementsQueryService) {
        this.unitOfMeasurementsService = unitOfMeasurementsService;
        this.unitOfMeasurementsQueryService = unitOfMeasurementsQueryService;
    }

    /**
     * {@code POST  /unit-of-measurements} : Create a new unitOfMeasurements.
     *
     * @param unitOfMeasurements the unitOfMeasurements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasurements, or with status {@code 400 (Bad Request)} if the unitOfMeasurements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measurements")
    public ResponseEntity<UnitOfMeasurements> createUnitOfMeasurements(@Valid @RequestBody UnitOfMeasurements unitOfMeasurements) throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasurements : {}", unitOfMeasurements);
        if (unitOfMeasurements.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasurements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitOfMeasurements result = unitOfMeasurementsService.save(unitOfMeasurements);
        return ResponseEntity.created(new URI("/api/unit-of-measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measurements} : Updates an existing unitOfMeasurements.
     *
     * @param unitOfMeasurements the unitOfMeasurements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasurements,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasurements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasurements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measurements")
    public ResponseEntity<UnitOfMeasurements> updateUnitOfMeasurements(@Valid @RequestBody UnitOfMeasurements unitOfMeasurements) throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasurements : {}", unitOfMeasurements);
        if (unitOfMeasurements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnitOfMeasurements result = unitOfMeasurementsService.save(unitOfMeasurements);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitOfMeasurements.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unit-of-measurements} : get all the unitOfMeasurements.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasurements in body.
     */
    @GetMapping("/unit-of-measurements")
    public ResponseEntity<List<UnitOfMeasurements>> getAllUnitOfMeasurements(UnitOfMeasurementsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UnitOfMeasurements by criteria: {}", criteria);
        Page<UnitOfMeasurements> page = unitOfMeasurementsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /unit-of-measurements/count} : count all the unitOfMeasurements.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/unit-of-measurements/count")
    public ResponseEntity<Long> countUnitOfMeasurements(UnitOfMeasurementsCriteria criteria) {
        log.debug("REST request to count UnitOfMeasurements by criteria: {}", criteria);
        return ResponseEntity.ok().body(unitOfMeasurementsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /unit-of-measurements/:id} : get the "id" unitOfMeasurements.
     *
     * @param id the id of the unitOfMeasurements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasurements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measurements/{id}")
    public ResponseEntity<UnitOfMeasurements> getUnitOfMeasurements(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasurements : {}", id);
        Optional<UnitOfMeasurements> unitOfMeasurements = unitOfMeasurementsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasurements);
    }

    /**
     * {@code DELETE  /unit-of-measurements/:id} : delete the "id" unitOfMeasurements.
     *
     * @param id the id of the unitOfMeasurements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measurements/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasurements(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasurements : {}", id);
        unitOfMeasurementsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
