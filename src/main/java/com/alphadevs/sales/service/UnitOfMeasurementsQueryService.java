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

import com.alphadevs.sales.domain.UnitOfMeasurements;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.UnitOfMeasurementsRepository;
import com.alphadevs.sales.service.dto.UnitOfMeasurementsCriteria;

/**
 * Service for executing complex queries for {@link UnitOfMeasurements} entities in the database.
 * The main input is a {@link UnitOfMeasurementsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UnitOfMeasurements} or a {@link Page} of {@link UnitOfMeasurements} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UnitOfMeasurementsQueryService extends QueryService<UnitOfMeasurements> {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasurementsQueryService.class);

    private final UnitOfMeasurementsRepository unitOfMeasurementsRepository;

    public UnitOfMeasurementsQueryService(UnitOfMeasurementsRepository unitOfMeasurementsRepository) {
        this.unitOfMeasurementsRepository = unitOfMeasurementsRepository;
    }

    /**
     * Return a {@link List} of {@link UnitOfMeasurements} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UnitOfMeasurements> findByCriteria(UnitOfMeasurementsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UnitOfMeasurements> specification = createSpecification(criteria);
        return unitOfMeasurementsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UnitOfMeasurements} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitOfMeasurements> findByCriteria(UnitOfMeasurementsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UnitOfMeasurements> specification = createSpecification(criteria);
        return unitOfMeasurementsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UnitOfMeasurementsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UnitOfMeasurements> specification = createSpecification(criteria);
        return unitOfMeasurementsRepository.count(specification);
    }

    /**
     * Function to convert {@link UnitOfMeasurementsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UnitOfMeasurements> createSpecification(UnitOfMeasurementsCriteria criteria) {
        Specification<UnitOfMeasurements> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UnitOfMeasurements_.id));
            }
            if (criteria.getUomCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUomCode(), UnitOfMeasurements_.uomCode));
            }
            if (criteria.getUomDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUomDescription(), UnitOfMeasurements_.uomDescription));
            }
        }
        return specification;
    }
}
