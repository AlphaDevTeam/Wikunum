package com.alphadevs.sales.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.sales.domain.UserGroups;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.UserGroupsRepository;
import com.alphadevs.sales.service.dto.UserGroupsCriteria;

/**
 * Service for executing complex queries for {@link UserGroups} entities in the database.
 * The main input is a {@link UserGroupsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserGroups} or a {@link Page} of {@link UserGroups} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserGroupsQueryService extends QueryService<UserGroups> {

    private final Logger log = LoggerFactory.getLogger(UserGroupsQueryService.class);

    private final UserGroupsRepository userGroupsRepository;

    public UserGroupsQueryService(UserGroupsRepository userGroupsRepository) {
        this.userGroupsRepository = userGroupsRepository;
    }

    /**
     * Return a {@link List} of {@link UserGroups} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserGroups> findByCriteria(UserGroupsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserGroups} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserGroups> findByCriteria(UserGroupsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserGroupsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsRepository.count(specification);
    }

    /**
     * Function to convert {@link UserGroupsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserGroups> createSpecification(UserGroupsCriteria criteria) {
        Specification<UserGroups> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserGroups_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), UserGroups_.groupName));
            }
            if (criteria.getUserPermissionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserPermissionsId(),
                    root -> root.join(UserGroups_.userPermissions, JoinType.LEFT).get(UserPermissions_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(UserGroups_.users, JoinType.LEFT).get(ExUser_.id)));
            }
        }
        return specification;
    }
}
