package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.UserGroups;
import com.alphadevs.sales.repository.UserGroupsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserGroups}.
 */
@Service
@Transactional
public class UserGroupsService {

    private final Logger log = LoggerFactory.getLogger(UserGroupsService.class);

    private final UserGroupsRepository userGroupsRepository;

    public UserGroupsService(UserGroupsRepository userGroupsRepository) {
        this.userGroupsRepository = userGroupsRepository;
    }

    /**
     * Save a userGroups.
     *
     * @param userGroups the entity to save.
     * @return the persisted entity.
     */
    public UserGroups save(UserGroups userGroups) {
        log.debug("Request to save UserGroups : {}", userGroups);
        return userGroupsRepository.save(userGroups);
    }

    /**
     * Get all the userGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserGroups> findAll(Pageable pageable) {
        log.debug("Request to get all UserGroups");
        return userGroupsRepository.findAll(pageable);
    }

    /**
     * Get all the userGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserGroups> findAllWithEagerRelationships(Pageable pageable) {
        return userGroupsRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one userGroups by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserGroups> findOne(Long id) {
        log.debug("Request to get UserGroups : {}", id);
        return userGroupsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userGroups by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserGroups : {}", id);
        userGroupsRepository.deleteById(id);
    }
}
