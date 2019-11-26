package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.UnitOfMeasurements;
import com.alphadevs.sales.repository.UnitOfMeasurementsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UnitOfMeasurements}.
 */
@Service
@Transactional
public class UnitOfMeasurementsService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasurementsService.class);

    private final UnitOfMeasurementsRepository unitOfMeasurementsRepository;

    public UnitOfMeasurementsService(UnitOfMeasurementsRepository unitOfMeasurementsRepository) {
        this.unitOfMeasurementsRepository = unitOfMeasurementsRepository;
    }

    /**
     * Save a unitOfMeasurements.
     *
     * @param unitOfMeasurements the entity to save.
     * @return the persisted entity.
     */
    public UnitOfMeasurements save(UnitOfMeasurements unitOfMeasurements) {
        log.debug("Request to save UnitOfMeasurements : {}", unitOfMeasurements);
        return unitOfMeasurementsRepository.save(unitOfMeasurements);
    }

    /**
     * Get all the unitOfMeasurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitOfMeasurements> findAll(Pageable pageable) {
        log.debug("Request to get all UnitOfMeasurements");
        return unitOfMeasurementsRepository.findAll(pageable);
    }


    /**
     * Get one unitOfMeasurements by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasurements> findOne(Long id) {
        log.debug("Request to get UnitOfMeasurements : {}", id);
        return unitOfMeasurementsRepository.findById(id);
    }

    /**
     * Delete the unitOfMeasurements by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasurements : {}", id);
        unitOfMeasurementsRepository.deleteById(id);
    }
}
