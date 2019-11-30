package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.UserAcquisition;
import com.mycompany.myapp.repository.UserAcquisitionRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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
import java.time.Duration;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserAcquisitionResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class UserAcquisitionResourceIT {

    private static final String DEFAULT_USERS_ACQUIRED_PER_TICK = "AAAAAAAAAA";
    private static final String UPDATED_USERS_ACQUIRED_PER_TICK = "BBBBBBBBBB";

    private static final String DEFAULT_CRON_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_CRON_SCHEDULE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REPEAT = 1;
    private static final Integer UPDATED_REPEAT = 2;

    private static final Duration DEFAULT_DELAY = Duration.ofHours(6);
    private static final Duration UPDATED_DELAY = Duration.ofHours(12);

    @Autowired
    private UserAcquisitionRepository userAcquisitionRepository;

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

    private MockMvc restUserAcquisitionMockMvc;

    private UserAcquisition userAcquisition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserAcquisitionResource userAcquisitionResource = new UserAcquisitionResource(userAcquisitionRepository);
        this.restUserAcquisitionMockMvc = MockMvcBuilders.standaloneSetup(userAcquisitionResource)
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
    public static UserAcquisition createEntity(EntityManager em) {
        UserAcquisition userAcquisition = new UserAcquisition()
            .usersAcquiredPerTick(DEFAULT_USERS_ACQUIRED_PER_TICK)
            .cronSchedule(DEFAULT_CRON_SCHEDULE)
            .repeat(DEFAULT_REPEAT)
            .delay(DEFAULT_DELAY);
        return userAcquisition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAcquisition createUpdatedEntity(EntityManager em) {
        UserAcquisition userAcquisition = new UserAcquisition()
            .usersAcquiredPerTick(UPDATED_USERS_ACQUIRED_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .repeat(UPDATED_REPEAT)
            .delay(UPDATED_DELAY);
        return userAcquisition;
    }

    @BeforeEach
    public void initTest() {
        userAcquisition = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserAcquisition() throws Exception {
        int databaseSizeBeforeCreate = userAcquisitionRepository.findAll().size();

        // Create the UserAcquisition
        restUserAcquisitionMockMvc.perform(post("/api/user-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAcquisition)))
            .andExpect(status().isCreated());

        // Validate the UserAcquisition in the database
        List<UserAcquisition> userAcquisitionList = userAcquisitionRepository.findAll();
        assertThat(userAcquisitionList).hasSize(databaseSizeBeforeCreate + 1);
        UserAcquisition testUserAcquisition = userAcquisitionList.get(userAcquisitionList.size() - 1);
        assertThat(testUserAcquisition.getUsersAcquiredPerTick()).isEqualTo(DEFAULT_USERS_ACQUIRED_PER_TICK);
        assertThat(testUserAcquisition.getCronSchedule()).isEqualTo(DEFAULT_CRON_SCHEDULE);
        assertThat(testUserAcquisition.getRepeat()).isEqualTo(DEFAULT_REPEAT);
        assertThat(testUserAcquisition.getDelay()).isEqualTo(DEFAULT_DELAY);
    }

    @Test
    @Transactional
    public void createUserAcquisitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userAcquisitionRepository.findAll().size();

        // Create the UserAcquisition with an existing ID
        userAcquisition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAcquisitionMockMvc.perform(post("/api/user-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAcquisition)))
            .andExpect(status().isBadRequest());

        // Validate the UserAcquisition in the database
        List<UserAcquisition> userAcquisitionList = userAcquisitionRepository.findAll();
        assertThat(userAcquisitionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserAcquisitions() throws Exception {
        // Initialize the database
        userAcquisitionRepository.saveAndFlush(userAcquisition);

        // Get all the userAcquisitionList
        restUserAcquisitionMockMvc.perform(get("/api/user-acquisitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].usersAcquiredPerTick").value(hasItem(DEFAULT_USERS_ACQUIRED_PER_TICK)))
            .andExpect(jsonPath("$.[*].cronSchedule").value(hasItem(DEFAULT_CRON_SCHEDULE)))
            .andExpect(jsonPath("$.[*].repeat").value(hasItem(DEFAULT_REPEAT)))
            .andExpect(jsonPath("$.[*].delay").value(hasItem(DEFAULT_DELAY.toString())));
    }
    
    @Test
    @Transactional
    public void getUserAcquisition() throws Exception {
        // Initialize the database
        userAcquisitionRepository.saveAndFlush(userAcquisition);

        // Get the userAcquisition
        restUserAcquisitionMockMvc.perform(get("/api/user-acquisitions/{id}", userAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userAcquisition.getId().intValue()))
            .andExpect(jsonPath("$.usersAcquiredPerTick").value(DEFAULT_USERS_ACQUIRED_PER_TICK))
            .andExpect(jsonPath("$.cronSchedule").value(DEFAULT_CRON_SCHEDULE))
            .andExpect(jsonPath("$.repeat").value(DEFAULT_REPEAT))
            .andExpect(jsonPath("$.delay").value(DEFAULT_DELAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAcquisition() throws Exception {
        // Get the userAcquisition
        restUserAcquisitionMockMvc.perform(get("/api/user-acquisitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAcquisition() throws Exception {
        // Initialize the database
        userAcquisitionRepository.saveAndFlush(userAcquisition);

        int databaseSizeBeforeUpdate = userAcquisitionRepository.findAll().size();

        // Update the userAcquisition
        UserAcquisition updatedUserAcquisition = userAcquisitionRepository.findById(userAcquisition.getId()).get();
        // Disconnect from session so that the updates on updatedUserAcquisition are not directly saved in db
        em.detach(updatedUserAcquisition);
        updatedUserAcquisition
            .usersAcquiredPerTick(UPDATED_USERS_ACQUIRED_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .repeat(UPDATED_REPEAT)
            .delay(UPDATED_DELAY);

        restUserAcquisitionMockMvc.perform(put("/api/user-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserAcquisition)))
            .andExpect(status().isOk());

        // Validate the UserAcquisition in the database
        List<UserAcquisition> userAcquisitionList = userAcquisitionRepository.findAll();
        assertThat(userAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        UserAcquisition testUserAcquisition = userAcquisitionList.get(userAcquisitionList.size() - 1);
        assertThat(testUserAcquisition.getUsersAcquiredPerTick()).isEqualTo(UPDATED_USERS_ACQUIRED_PER_TICK);
        assertThat(testUserAcquisition.getCronSchedule()).isEqualTo(UPDATED_CRON_SCHEDULE);
        assertThat(testUserAcquisition.getRepeat()).isEqualTo(UPDATED_REPEAT);
        assertThat(testUserAcquisition.getDelay()).isEqualTo(UPDATED_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingUserAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = userAcquisitionRepository.findAll().size();

        // Create the UserAcquisition

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAcquisitionMockMvc.perform(put("/api/user-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAcquisition)))
            .andExpect(status().isBadRequest());

        // Validate the UserAcquisition in the database
        List<UserAcquisition> userAcquisitionList = userAcquisitionRepository.findAll();
        assertThat(userAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserAcquisition() throws Exception {
        // Initialize the database
        userAcquisitionRepository.saveAndFlush(userAcquisition);

        int databaseSizeBeforeDelete = userAcquisitionRepository.findAll().size();

        // Delete the userAcquisition
        restUserAcquisitionMockMvc.perform(delete("/api/user-acquisitions/{id}", userAcquisition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAcquisition> userAcquisitionList = userAcquisitionRepository.findAll();
        assertThat(userAcquisitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
