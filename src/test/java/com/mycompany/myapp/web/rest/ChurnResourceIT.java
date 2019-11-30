package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Churn;
import com.mycompany.myapp.repository.ChurnRepository;
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
 * Integration tests for the {@link ChurnResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class ChurnResourceIT {

    private static final String DEFAULT_CHRUN_PER_TICK = "AAAAAAAAAA";
    private static final String UPDATED_CHRUN_PER_TICK = "BBBBBBBBBB";

    private static final String DEFAULT_CRON_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_CRON_SCHEDULE = "BBBBBBBBBB";

    private static final Duration DEFAULT_DELAY = Duration.ofHours(6);
    private static final Duration UPDATED_DELAY = Duration.ofHours(12);

    @Autowired
    private ChurnRepository churnRepository;

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

    private MockMvc restChurnMockMvc;

    private Churn churn;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChurnResource churnResource = new ChurnResource(churnRepository);
        this.restChurnMockMvc = MockMvcBuilders.standaloneSetup(churnResource)
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
    public static Churn createEntity(EntityManager em) {
        Churn churn = new Churn()
            .chrunPerTick(DEFAULT_CHRUN_PER_TICK)
            .cronSchedule(DEFAULT_CRON_SCHEDULE)
            .delay(DEFAULT_DELAY);
        return churn;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Churn createUpdatedEntity(EntityManager em) {
        Churn churn = new Churn()
            .chrunPerTick(UPDATED_CHRUN_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .delay(UPDATED_DELAY);
        return churn;
    }

    @BeforeEach
    public void initTest() {
        churn = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurn() throws Exception {
        int databaseSizeBeforeCreate = churnRepository.findAll().size();

        // Create the Churn
        restChurnMockMvc.perform(post("/api/churns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churn)))
            .andExpect(status().isCreated());

        // Validate the Churn in the database
        List<Churn> churnList = churnRepository.findAll();
        assertThat(churnList).hasSize(databaseSizeBeforeCreate + 1);
        Churn testChurn = churnList.get(churnList.size() - 1);
        assertThat(testChurn.getChrunPerTick()).isEqualTo(DEFAULT_CHRUN_PER_TICK);
        assertThat(testChurn.getCronSchedule()).isEqualTo(DEFAULT_CRON_SCHEDULE);
        assertThat(testChurn.getDelay()).isEqualTo(DEFAULT_DELAY);
    }

    @Test
    @Transactional
    public void createChurnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = churnRepository.findAll().size();

        // Create the Churn with an existing ID
        churn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurnMockMvc.perform(post("/api/churns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churn)))
            .andExpect(status().isBadRequest());

        // Validate the Churn in the database
        List<Churn> churnList = churnRepository.findAll();
        assertThat(churnList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllChurns() throws Exception {
        // Initialize the database
        churnRepository.saveAndFlush(churn);

        // Get all the churnList
        restChurnMockMvc.perform(get("/api/churns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churn.getId().intValue())))
            .andExpect(jsonPath("$.[*].chrunPerTick").value(hasItem(DEFAULT_CHRUN_PER_TICK)))
            .andExpect(jsonPath("$.[*].cronSchedule").value(hasItem(DEFAULT_CRON_SCHEDULE)))
            .andExpect(jsonPath("$.[*].delay").value(hasItem(DEFAULT_DELAY.toString())));
    }
    
    @Test
    @Transactional
    public void getChurn() throws Exception {
        // Initialize the database
        churnRepository.saveAndFlush(churn);

        // Get the churn
        restChurnMockMvc.perform(get("/api/churns/{id}", churn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(churn.getId().intValue()))
            .andExpect(jsonPath("$.chrunPerTick").value(DEFAULT_CHRUN_PER_TICK))
            .andExpect(jsonPath("$.cronSchedule").value(DEFAULT_CRON_SCHEDULE))
            .andExpect(jsonPath("$.delay").value(DEFAULT_DELAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChurn() throws Exception {
        // Get the churn
        restChurnMockMvc.perform(get("/api/churns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurn() throws Exception {
        // Initialize the database
        churnRepository.saveAndFlush(churn);

        int databaseSizeBeforeUpdate = churnRepository.findAll().size();

        // Update the churn
        Churn updatedChurn = churnRepository.findById(churn.getId()).get();
        // Disconnect from session so that the updates on updatedChurn are not directly saved in db
        em.detach(updatedChurn);
        updatedChurn
            .chrunPerTick(UPDATED_CHRUN_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .delay(UPDATED_DELAY);

        restChurnMockMvc.perform(put("/api/churns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChurn)))
            .andExpect(status().isOk());

        // Validate the Churn in the database
        List<Churn> churnList = churnRepository.findAll();
        assertThat(churnList).hasSize(databaseSizeBeforeUpdate);
        Churn testChurn = churnList.get(churnList.size() - 1);
        assertThat(testChurn.getChrunPerTick()).isEqualTo(UPDATED_CHRUN_PER_TICK);
        assertThat(testChurn.getCronSchedule()).isEqualTo(UPDATED_CRON_SCHEDULE);
        assertThat(testChurn.getDelay()).isEqualTo(UPDATED_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingChurn() throws Exception {
        int databaseSizeBeforeUpdate = churnRepository.findAll().size();

        // Create the Churn

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurnMockMvc.perform(put("/api/churns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churn)))
            .andExpect(status().isBadRequest());

        // Validate the Churn in the database
        List<Churn> churnList = churnRepository.findAll();
        assertThat(churnList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChurn() throws Exception {
        // Initialize the database
        churnRepository.saveAndFlush(churn);

        int databaseSizeBeforeDelete = churnRepository.findAll().size();

        // Delete the churn
        restChurnMockMvc.perform(delete("/api/churns/{id}", churn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Churn> churnList = churnRepository.findAll();
        assertThat(churnList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
