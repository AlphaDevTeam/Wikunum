package com.alphadevs.sales.repository;
import com.alphadevs.sales.domain.UnitOfMeasurements;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UnitOfMeasurements entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasurementsRepository extends JpaRepository<UnitOfMeasurements, Long>, JpaSpecificationExecutor<UnitOfMeasurements> {

}
