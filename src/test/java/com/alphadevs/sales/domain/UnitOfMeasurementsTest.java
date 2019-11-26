package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class UnitOfMeasurementsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasurements.class);
        UnitOfMeasurements unitOfMeasurements1 = new UnitOfMeasurements();
        unitOfMeasurements1.setId(1L);
        UnitOfMeasurements unitOfMeasurements2 = new UnitOfMeasurements();
        unitOfMeasurements2.setId(unitOfMeasurements1.getId());
        assertThat(unitOfMeasurements1).isEqualTo(unitOfMeasurements2);
        unitOfMeasurements2.setId(2L);
        assertThat(unitOfMeasurements1).isNotEqualTo(unitOfMeasurements2);
        unitOfMeasurements1.setId(null);
        assertThat(unitOfMeasurements1).isNotEqualTo(unitOfMeasurements2);
    }
}
