package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.security.SecurityUtils;
import com.alphadevs.sales.service.LocationService;
import com.alphadevs.sales.service.UserService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.LocationCriteria;
import com.alphadevs.sales.service.LocationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alphadevs.sales.security.AuthoritiesConstants.ADMIN;
import static com.alphadevs.sales.security.AuthoritiesConstants.USER;

/**
 * REST controller for managing {@link com.alphadevs.sales.domain.Location}.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationService locationService;
    private final UserService userService;
    private final LocationQueryService locationQueryService;

    public LocationResource(LocationService locationService, UserService userService, LocationQueryService locationQueryService) {
        this.locationService = locationService;
        this.userService = userService;
        this.locationQueryService = locationQueryService;
    }

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param location the location to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new location, or with status {@code 400 (Bad Request)} if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if (location.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Location result = locationService.save(location);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locations} : Updates an existing location.
     *
     * @param location the location to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated location,
     * or with status {@code 400 (Bad Request)} if the location is not valid,
     * or with status {@code 500 (Internal Server Error)} if the location couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations")
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        if (location.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Location result = locationService.save(location);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, location.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(LocationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Locations by criteria: {}", criteria);
        List<Location> locationBasedOnRole = userService.getUserLocations();

        Page<Location> page = locationQueryService.findByCriteria(criteria, pageable);
        List<Location> locationBasedOnCriteria = locationQueryService.findByCriteria(criteria, pageable).getContent();
        List<Location> filteredList = new ArrayList<>();
        if(SecurityUtils.isCurrentUserInRole(ADMIN)){
            filteredList = locationBasedOnCriteria;
        }
        else if (SecurityUtils.isCurrentUserInRole(USER) && locationBasedOnRole != null && locationBasedOnCriteria != null){
            for (Location location : locationBasedOnRole) {
                if(location != null && locationBasedOnCriteria.contains(location)){
                    filteredList.add(location);
                }
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(filteredList);

    }

    /**
    * {@code GET  /locations/count} : count all the locations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/locations/count")
    public ResponseEntity<Long> countLocations(LocationCriteria criteria) {
        log.debug("REST request to count Locations by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the location to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the location, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<Location> location = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(location);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the location to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
