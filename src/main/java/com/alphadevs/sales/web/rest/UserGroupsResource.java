package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.UserGroups;
import com.alphadevs.sales.service.UserGroupsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.UserGroupsCriteria;
import com.alphadevs.sales.service.UserGroupsQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.UserGroups}.
 */
@RestController
@RequestMapping("/api")
public class UserGroupsResource {

    private final Logger log = LoggerFactory.getLogger(UserGroupsResource.class);

    private static final String ENTITY_NAME = "userGroups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserGroupsService userGroupsService;

    private final UserGroupsQueryService userGroupsQueryService;

    public UserGroupsResource(UserGroupsService userGroupsService, UserGroupsQueryService userGroupsQueryService) {
        this.userGroupsService = userGroupsService;
        this.userGroupsQueryService = userGroupsQueryService;
    }

    /**
     * {@code POST  /user-groups} : Create a new userGroups.
     *
     * @param userGroups the userGroups to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userGroups, or with status {@code 400 (Bad Request)} if the userGroups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-groups")
    public ResponseEntity<UserGroups> createUserGroups(@Valid @RequestBody UserGroups userGroups) throws URISyntaxException {
        log.debug("REST request to save UserGroups : {}", userGroups);
        if (userGroups.getId() != null) {
            throw new BadRequestAlertException("A new userGroups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserGroups result = userGroupsService.save(userGroups);
        return ResponseEntity.created(new URI("/api/user-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-groups} : Updates an existing userGroups.
     *
     * @param userGroups the userGroups to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGroups,
     * or with status {@code 400 (Bad Request)} if the userGroups is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userGroups couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-groups")
    public ResponseEntity<UserGroups> updateUserGroups(@Valid @RequestBody UserGroups userGroups) throws URISyntaxException {
        log.debug("REST request to update UserGroups : {}", userGroups);
        if (userGroups.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserGroups result = userGroupsService.save(userGroups);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userGroups.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-groups} : get all the userGroups.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userGroups in body.
     */
    @GetMapping("/user-groups")
    public ResponseEntity<List<UserGroups>> getAllUserGroups(UserGroupsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserGroups by criteria: {}", criteria);
        Page<UserGroups> page = userGroupsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /user-groups/count} : count all the userGroups.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-groups/count")
    public ResponseEntity<Long> countUserGroups(UserGroupsCriteria criteria) {
        log.debug("REST request to count UserGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(userGroupsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-groups/:id} : get the "id" userGroups.
     *
     * @param id the id of the userGroups to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGroups, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-groups/{id}")
    public ResponseEntity<UserGroups> getUserGroups(@PathVariable Long id) {
        log.debug("REST request to get UserGroups : {}", id);
        Optional<UserGroups> userGroups = userGroupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userGroups);
    }

    /**
     * {@code DELETE  /user-groups/:id} : delete the "id" userGroups.
     *
     * @param id the id of the userGroups to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-groups/{id}")
    public ResponseEntity<Void> deleteUserGroups(@PathVariable Long id) {
        log.debug("REST request to delete UserGroups : {}", id);
        userGroupsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
