package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Revenue;
import com.mycompany.myapp.repository.RevenueRepository;
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
 * Integration tests for the {@link RevenueResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class RevenueResourceIT {

    private static final String DEFAULT_REVENUE_PER_TICK = "AAAAAAAAAA";
    private static final String UPDATED_REVENUE_PER_TICK = "BBBBBBBBBB";

    private static final String DEFAULT_CRON_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_CRON_SCHEDULE = "BBBBBBBBBB";

    private static final Duration DEFAULT_DELAY = Duration.ofHours(6);
    private static final Duration UPDATED_DELAY = Duration.ofHours(12);

    @Autowired
    private RevenueRepository revenueRepository;

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

    private MockMvc restRevenueMockMvc;

    private Revenue revenue;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevenueResource revenueResource = new RevenueResource(revenueRepository);
        this.restRevenueMockMvc = MockMvcBuilders.standaloneSetup(revenueResource)
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
    public static Revenue createEntity(EntityManager em) {
        Revenue revenue = new Revenue()
            .revenuePerTick(DEFAULT_REVENUE_PER_TICK)
            .cronSchedule(DEFAULT_CRON_SCHEDULE)
            .delay(DEFAULT_DELAY);
        return revenue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revenue createUpdatedEntity(EntityManager em) {
        Revenue revenue = new Revenue()
            .revenuePerTick(UPDATED_REVENUE_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .delay(UPDATED_DELAY);
        return revenue;
    }

    @BeforeEach
    public void initTest() {
        revenue = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevenue() throws Exception {
        int databaseSizeBeforeCreate = revenueRepository.findAll().size();

        // Create the Revenue
        restRevenueMockMvc.perform(post("/api/revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isCreated());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeCreate + 1);
        Revenue testRevenue = revenueList.get(revenueList.size() - 1);
        assertThat(testRevenue.getRevenuePerTick()).isEqualTo(DEFAULT_REVENUE_PER_TICK);
        assertThat(testRevenue.getCronSchedule()).isEqualTo(DEFAULT_CRON_SCHEDULE);
        assertThat(testRevenue.getDelay()).isEqualTo(DEFAULT_DELAY);
    }

    @Test
    @Transactional
    public void createRevenueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revenueRepository.findAll().size();

        // Create the Revenue with an existing ID
        revenue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevenueMockMvc.perform(post("/api/revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isBadRequest());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRevenues() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        // Get all the revenueList
        restRevenueMockMvc.perform(get("/api/revenues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenue.getId().intValue())))
            .andExpect(jsonPath("$.[*].revenuePerTick").value(hasItem(DEFAULT_REVENUE_PER_TICK)))
            .andExpect(jsonPath("$.[*].cronSchedule").value(hasItem(DEFAULT_CRON_SCHEDULE)))
            .andExpect(jsonPath("$.[*].delay").value(hasItem(DEFAULT_DELAY.toString())));
    }
    
    @Test
    @Transactional
    public void getRevenue() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        // Get the revenue
        restRevenueMockMvc.perform(get("/api/revenues/{id}", revenue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revenue.getId().intValue()))
            .andExpect(jsonPath("$.revenuePerTick").value(DEFAULT_REVENUE_PER_TICK))
            .andExpect(jsonPath("$.cronSchedule").value(DEFAULT_CRON_SCHEDULE))
            .andExpect(jsonPath("$.delay").value(DEFAULT_DELAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRevenue() throws Exception {
        // Get the revenue
        restRevenueMockMvc.perform(get("/api/revenues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevenue() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        int databaseSizeBeforeUpdate = revenueRepository.findAll().size();

        // Update the revenue
        Revenue updatedRevenue = revenueRepository.findById(revenue.getId()).get();
        // Disconnect from session so that the updates on updatedRevenue are not directly saved in db
        em.detach(updatedRevenue);
        updatedRevenue
            .revenuePerTick(UPDATED_REVENUE_PER_TICK)
            .cronSchedule(UPDATED_CRON_SCHEDULE)
            .delay(UPDATED_DELAY);

        restRevenueMockMvc.perform(put("/api/revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRevenue)))
            .andExpect(status().isOk());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeUpdate);
        Revenue testRevenue = revenueList.get(revenueList.size() - 1);
        assertThat(testRevenue.getRevenuePerTick()).isEqualTo(UPDATED_REVENUE_PER_TICK);
        assertThat(testRevenue.getCronSchedule()).isEqualTo(UPDATED_CRON_SCHEDULE);
        assertThat(testRevenue.getDelay()).isEqualTo(UPDATED_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingRevenue() throws Exception {
        int databaseSizeBeforeUpdate = revenueRepository.findAll().size();

        // Create the Revenue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevenueMockMvc.perform(put("/api/revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isBadRequest());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRevenue() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        int databaseSizeBeforeDelete = revenueRepository.findAll().size();

        // Delete the revenue
        restRevenueMockMvc.perform(delete("/api/revenues/{id}", revenue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
