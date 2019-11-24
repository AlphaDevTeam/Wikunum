package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.UserGroups;
import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.repository.UserGroupsRepository;
import com.alphadevs.sales.service.UserGroupsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.UserGroupsCriteria;
import com.alphadevs.sales.service.UserGroupsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserGroupsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class UserGroupsResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @Mock
    private UserGroupsRepository userGroupsRepositoryMock;

    @Mock
    private UserGroupsService userGroupsServiceMock;

    @Autowired
    private UserGroupsService userGroupsService;

    @Autowired
    private UserGroupsQueryService userGroupsQueryService;

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

    private MockMvc restUserGroupsMockMvc;

    private UserGroups userGroups;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserGroupsResource userGroupsResource = new UserGroupsResource(userGroupsService, userGroupsQueryService);
        this.restUserGroupsMockMvc = MockMvcBuilders.standaloneSetup(userGroupsResource)
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
    public static UserGroups createEntity(EntityManager em) {
        UserGroups userGroups = new UserGroups()
            .groupName(DEFAULT_GROUP_NAME);
        // Add required entity
        UserPermissions userPermissions;
        if (TestUtil.findAll(em, UserPermissions.class).isEmpty()) {
            userPermissions = UserPermissionsResourceIT.createEntity(em);
            em.persist(userPermissions);
            em.flush();
        } else {
            userPermissions = TestUtil.findAll(em, UserPermissions.class).get(0);
        }
        userGroups.getUserPermissions().add(userPermissions);
        return userGroups;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroups createUpdatedEntity(EntityManager em) {
        UserGroups userGroups = new UserGroups()
            .groupName(UPDATED_GROUP_NAME);
        // Add required entity
        UserPermissions userPermissions;
        if (TestUtil.findAll(em, UserPermissions.class).isEmpty()) {
            userPermissions = UserPermissionsResourceIT.createUpdatedEntity(em);
            em.persist(userPermissions);
            em.flush();
        } else {
            userPermissions = TestUtil.findAll(em, UserPermissions.class).get(0);
        }
        userGroups.getUserPermissions().add(userPermissions);
        return userGroups;
    }

    @BeforeEach
    public void initTest() {
        userGroups = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserGroups() throws Exception {
        int databaseSizeBeforeCreate = userGroupsRepository.findAll().size();

        // Create the UserGroups
        restUserGroupsMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroups)))
            .andExpect(status().isCreated());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        UserGroups testUserGroups = userGroupsList.get(userGroupsList.size() - 1);
        assertThat(testUserGroups.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    public void createUserGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userGroupsRepository.findAll().size();

        // Create the UserGroups with an existing ID
        userGroups.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGroupsMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroups)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userGroupsRepository.findAll().size();
        // set the field null
        userGroups.setGroupName(null);

        // Create the UserGroups, which fails.

        restUserGroupsMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroups)))
            .andExpect(status().isBadRequest());

        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        UserGroupsResource userGroupsResource = new UserGroupsResource(userGroupsServiceMock, userGroupsQueryService);
        when(userGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserGroupsMockMvc = MockMvcBuilders.standaloneSetup(userGroupsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserGroupsMockMvc.perform(get("/api/user-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(userGroupsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserGroupsResource userGroupsResource = new UserGroupsResource(userGroupsServiceMock, userGroupsQueryService);
            when(userGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserGroupsMockMvc = MockMvcBuilders.standaloneSetup(userGroupsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserGroupsMockMvc.perform(get("/api/user-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(userGroupsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get the userGroups
        restUserGroupsMockMvc.perform(get("/api/user-groups/{id}", userGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userGroups.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME));
    }


    @Test
    @Transactional
    public void getUserGroupsByIdFiltering() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        Long id = userGroups.getId();

        defaultUserGroupsShouldBeFound("id.equals=" + id);
        defaultUserGroupsShouldNotBeFound("id.notEquals=" + id);

        defaultUserGroupsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserGroupsShouldNotBeFound("id.greaterThan=" + id);

        defaultUserGroupsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserGroupsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName equals to DEFAULT_GROUP_NAME
        defaultUserGroupsShouldBeFound("groupName.equals=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupsList where groupName equals to UPDATED_GROUP_NAME
        defaultUserGroupsShouldNotBeFound("groupName.equals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName not equals to DEFAULT_GROUP_NAME
        defaultUserGroupsShouldNotBeFound("groupName.notEquals=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupsList where groupName not equals to UPDATED_GROUP_NAME
        defaultUserGroupsShouldBeFound("groupName.notEquals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName in DEFAULT_GROUP_NAME or UPDATED_GROUP_NAME
        defaultUserGroupsShouldBeFound("groupName.in=" + DEFAULT_GROUP_NAME + "," + UPDATED_GROUP_NAME);

        // Get all the userGroupsList where groupName equals to UPDATED_GROUP_NAME
        defaultUserGroupsShouldNotBeFound("groupName.in=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName is not null
        defaultUserGroupsShouldBeFound("groupName.specified=true");

        // Get all the userGroupsList where groupName is null
        defaultUserGroupsShouldNotBeFound("groupName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserGroupsByGroupNameContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName contains DEFAULT_GROUP_NAME
        defaultUserGroupsShouldBeFound("groupName.contains=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupsList where groupName contains UPDATED_GROUP_NAME
        defaultUserGroupsShouldNotBeFound("groupName.contains=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameNotContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where groupName does not contain DEFAULT_GROUP_NAME
        defaultUserGroupsShouldNotBeFound("groupName.doesNotContain=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupsList where groupName does not contain UPDATED_GROUP_NAME
        defaultUserGroupsShouldBeFound("groupName.doesNotContain=" + UPDATED_GROUP_NAME);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByUserPermissionsIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserPermissions userPermissions = userGroups.getUserPermissions();
        userGroupsRepository.saveAndFlush(userGroups);
        Long userPermissionsId = userPermissions.getId();

        // Get all the userGroupsList where userPermissions equals to userPermissionsId
        defaultUserGroupsShouldBeFound("userPermissionsId.equals=" + userPermissionsId);

        // Get all the userGroupsList where userPermissions equals to userPermissionsId + 1
        defaultUserGroupsShouldNotBeFound("userPermissionsId.equals=" + (userPermissionsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserGroupsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);
        ExUser users = ExUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        userGroups.addUsers(users);
        userGroupsRepository.saveAndFlush(userGroups);
        Long usersId = users.getId();

        // Get all the userGroupsList where users equals to usersId
        defaultUserGroupsShouldBeFound("usersId.equals=" + usersId);

        // Get all the userGroupsList where users equals to usersId + 1
        defaultUserGroupsShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserGroupsShouldBeFound(String filter) throws Exception {
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)));

        // Check, that the count call also returns 1
        restUserGroupsMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserGroupsShouldNotBeFound(String filter) throws Exception {
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserGroupsMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserGroups() throws Exception {
        // Get the userGroups
        restUserGroupsMockMvc.perform(get("/api/user-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserGroups() throws Exception {
        // Initialize the database
        userGroupsService.save(userGroups);

        int databaseSizeBeforeUpdate = userGroupsRepository.findAll().size();

        // Update the userGroups
        UserGroups updatedUserGroups = userGroupsRepository.findById(userGroups.getId()).get();
        // Disconnect from session so that the updates on updatedUserGroups are not directly saved in db
        em.detach(updatedUserGroups);
        updatedUserGroups
            .groupName(UPDATED_GROUP_NAME);

        restUserGroupsMockMvc.perform(put("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserGroups)))
            .andExpect(status().isOk());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeUpdate);
        UserGroups testUserGroups = userGroupsList.get(userGroupsList.size() - 1);
        assertThat(testUserGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserGroups() throws Exception {
        int databaseSizeBeforeUpdate = userGroupsRepository.findAll().size();

        // Create the UserGroups

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGroupsMockMvc.perform(put("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroups)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserGroups() throws Exception {
        // Initialize the database
        userGroupsService.save(userGroups);

        int databaseSizeBeforeDelete = userGroupsRepository.findAll().size();

        // Delete the userGroups
        restUserGroupsMockMvc.perform(delete("/api/user-groups/{id}", userGroups.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
