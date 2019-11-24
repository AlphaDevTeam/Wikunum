package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.UnitOfMeasurements;
import com.alphadevs.sales.repository.UnitOfMeasurementsRepository;
import com.alphadevs.sales.service.UnitOfMeasurementsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.UnitOfMeasurementsCriteria;
import com.alphadevs.sales.service.UnitOfMeasurementsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UnitOfMeasurementsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class UnitOfMeasurementsResourceIT {

    private static final String DEFAULT_UOM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UOM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UOM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_UOM_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private UnitOfMeasurementsRepository unitOfMeasurementsRepository;

    @Autowired
    private UnitOfMeasurementsService unitOfMeasurementsService;

    @Autowired
    private UnitOfMeasurementsQueryService unitOfMeasurementsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUnitOfMeasurementsMockMvc;

    private UnitOfMeasurements unitOfMeasurements;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnitOfMeasurementsResource unitOfMeasurementsResource = new UnitOfMeasurementsResource(unitOfMeasurementsService, unitOfMeasurementsQueryService);
        this.restUnitOfMeasurementsMockMvc = MockMvcBuilders.standaloneSetup(unitOfMeasurementsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasurements createEntity(EntityManager em) {
        UnitOfMeasurements unitOfMeasurements = new UnitOfMeasurements()
            .uomCode(DEFAULT_UOM_CODE)
            .uomDescription(DEFAULT_UOM_DESCRIPTION);
        return unitOfMeasurements;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasurements createUpdatedEntity(EntityManager em) {
        UnitOfMeasurements unitOfMeasurements = new UnitOfMeasurements()
            .uomCode(UPDATED_UOM_CODE)
            .uomDescription(UPDATED_UOM_DESCRIPTION);
        return unitOfMeasurements;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasurements = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnitOfMeasurements() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasurementsRepository.findAll().size();

        // Create the UnitOfMeasurements
        restUnitOfMeasurementsMockMvc.perform(post("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurements)))
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasurements in the database
        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasurements testUnitOfMeasurements = unitOfMeasurementsList.get(unitOfMeasurementsList.size() - 1);
        assertThat(testUnitOfMeasurements.getUomCode()).isEqualTo(DEFAULT_UOM_CODE);
        assertThat(testUnitOfMeasurements.getUomDescription()).isEqualTo(DEFAULT_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createUnitOfMeasurementsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasurementsRepository.findAll().size();

        // Create the UnitOfMeasurements with an existing ID
        unitOfMeasurements.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasurementsMockMvc.perform(post("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurements)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurements in the database
        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUomCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasurementsRepository.findAll().size();
        // set the field null
        unitOfMeasurements.setUomCode(null);

        // Create the UnitOfMeasurements, which fails.

        restUnitOfMeasurementsMockMvc.perform(post("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurements)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUomDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasurementsRepository.findAll().size();
        // set the field null
        unitOfMeasurements.setUomDescription(null);

        // Create the UnitOfMeasurements, which fails.

        restUnitOfMeasurementsMockMvc.perform(post("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurements)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurements() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasurements.getId().intValue())))
            .andExpect(jsonPath("$.[*].uomCode").value(hasItem(DEFAULT_UOM_CODE)))
            .andExpect(jsonPath("$.[*].uomDescription").value(hasItem(DEFAULT_UOM_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getUnitOfMeasurements() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get the unitOfMeasurements
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements/{id}", unitOfMeasurements.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasurements.getId().intValue()))
            .andExpect(jsonPath("$.uomCode").value(DEFAULT_UOM_CODE))
            .andExpect(jsonPath("$.uomDescription").value(DEFAULT_UOM_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getUnitOfMeasurementsByIdFiltering() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        Long id = unitOfMeasurements.getId();

        defaultUnitOfMeasurementsShouldBeFound("id.equals=" + id);
        defaultUnitOfMeasurementsShouldNotBeFound("id.notEquals=" + id);

        defaultUnitOfMeasurementsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUnitOfMeasurementsShouldNotBeFound("id.greaterThan=" + id);

        defaultUnitOfMeasurementsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUnitOfMeasurementsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode equals to DEFAULT_UOM_CODE
        defaultUnitOfMeasurementsShouldBeFound("uomCode.equals=" + DEFAULT_UOM_CODE);

        // Get all the unitOfMeasurementsList where uomCode equals to UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.equals=" + UPDATED_UOM_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode not equals to DEFAULT_UOM_CODE
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.notEquals=" + DEFAULT_UOM_CODE);

        // Get all the unitOfMeasurementsList where uomCode not equals to UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldBeFound("uomCode.notEquals=" + UPDATED_UOM_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode in DEFAULT_UOM_CODE or UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldBeFound("uomCode.in=" + DEFAULT_UOM_CODE + "," + UPDATED_UOM_CODE);

        // Get all the unitOfMeasurementsList where uomCode equals to UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.in=" + UPDATED_UOM_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode is not null
        defaultUnitOfMeasurementsShouldBeFound("uomCode.specified=true");

        // Get all the unitOfMeasurementsList where uomCode is null
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode contains DEFAULT_UOM_CODE
        defaultUnitOfMeasurementsShouldBeFound("uomCode.contains=" + DEFAULT_UOM_CODE);

        // Get all the unitOfMeasurementsList where uomCode contains UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.contains=" + UPDATED_UOM_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomCodeNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomCode does not contain DEFAULT_UOM_CODE
        defaultUnitOfMeasurementsShouldNotBeFound("uomCode.doesNotContain=" + DEFAULT_UOM_CODE);

        // Get all the unitOfMeasurementsList where uomCode does not contain UPDATED_UOM_CODE
        defaultUnitOfMeasurementsShouldBeFound("uomCode.doesNotContain=" + UPDATED_UOM_CODE);
    }


    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription equals to DEFAULT_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.equals=" + DEFAULT_UOM_DESCRIPTION);

        // Get all the unitOfMeasurementsList where uomDescription equals to UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.equals=" + UPDATED_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription not equals to DEFAULT_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.notEquals=" + DEFAULT_UOM_DESCRIPTION);

        // Get all the unitOfMeasurementsList where uomDescription not equals to UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.notEquals=" + UPDATED_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription in DEFAULT_UOM_DESCRIPTION or UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.in=" + DEFAULT_UOM_DESCRIPTION + "," + UPDATED_UOM_DESCRIPTION);

        // Get all the unitOfMeasurementsList where uomDescription equals to UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.in=" + UPDATED_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription is not null
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.specified=true");

        // Get all the unitOfMeasurementsList where uomDescription is null
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription contains DEFAULT_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.contains=" + DEFAULT_UOM_DESCRIPTION);

        // Get all the unitOfMeasurementsList where uomDescription contains UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.contains=" + UPDATED_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasurementsByUomDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasurementsRepository.saveAndFlush(unitOfMeasurements);

        // Get all the unitOfMeasurementsList where uomDescription does not contain DEFAULT_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldNotBeFound("uomDescription.doesNotContain=" + DEFAULT_UOM_DESCRIPTION);

        // Get all the unitOfMeasurementsList where uomDescription does not contain UPDATED_UOM_DESCRIPTION
        defaultUnitOfMeasurementsShouldBeFound("uomDescription.doesNotContain=" + UPDATED_UOM_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUnitOfMeasurementsShouldBeFound(String filter) throws Exception {
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasurements.getId().intValue())))
            .andExpect(jsonPath("$.[*].uomCode").value(hasItem(DEFAULT_UOM_CODE)))
            .andExpect(jsonPath("$.[*].uomDescription").value(hasItem(DEFAULT_UOM_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUnitOfMeasurementsShouldNotBeFound(String filter) throws Exception {
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUnitOfMeasurements() throws Exception {
        // Get the unitOfMeasurements
        restUnitOfMeasurementsMockMvc.perform(get("/api/unit-of-measurements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnitOfMeasurements() throws Exception {
        // Initialize the database
        unitOfMeasurementsService.save(unitOfMeasurements);

        int databaseSizeBeforeUpdate = unitOfMeasurementsRepository.findAll().size();

        // Update the unitOfMeasurements
        UnitOfMeasurements updatedUnitOfMeasurements = unitOfMeasurementsRepository.findById(unitOfMeasurements.getId()).get();
        // Disconnect from session so that the updates on updatedUnitOfMeasurements are not directly saved in db
        em.detach(updatedUnitOfMeasurements);
        updatedUnitOfMeasurements
            .uomCode(UPDATED_UOM_CODE)
            .uomDescription(UPDATED_UOM_DESCRIPTION);

        restUnitOfMeasurementsMockMvc.perform(put("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnitOfMeasurements)))
            .andExpect(status().isOk());

        // Validate the UnitOfMeasurements in the database
        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasurements testUnitOfMeasurements = unitOfMeasurementsList.get(unitOfMeasurementsList.size() - 1);
        assertThat(testUnitOfMeasurements.getUomCode()).isEqualTo(UPDATED_UOM_CODE);
        assertThat(testUnitOfMeasurements.getUomDescription()).isEqualTo(UPDATED_UOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingUnitOfMeasurements() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementsRepository.findAll().size();

        // Create the UnitOfMeasurements

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasurementsMockMvc.perform(put("/api/unit-of-measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurements)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurements in the database
        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnitOfMeasurements() throws Exception {
        // Initialize the database
        unitOfMeasurementsService.save(unitOfMeasurements);

        int databaseSizeBeforeDelete = unitOfMeasurementsRepository.findAll().size();

        // Delete the unitOfMeasurements
        restUnitOfMeasurementsMockMvc.perform(delete("/api/unit-of-measurements/{id}", unitOfMeasurements.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasurements> unitOfMeasurementsList = unitOfMeasurementsRepository.findAll();
        assertThat(unitOfMeasurementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
