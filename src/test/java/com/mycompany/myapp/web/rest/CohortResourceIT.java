package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Cohort;
import com.mycompany.myapp.repository.CohortRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CohortResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class CohortResourceIT {

    private static final String DEFAULT_COHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COHORT_NAME = "BBBBBBBBBB";

    @Autowired
    private CohortRepository cohortRepository;

    @Mock
    private CohortRepository cohortRepositoryMock;

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

    private MockMvc restCohortMockMvc;

    private Cohort cohort;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CohortResource cohortResource = new CohortResource(cohortRepository);
        this.restCohortMockMvc = MockMvcBuilders.standaloneSetup(cohortResource)
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
    public static Cohort createEntity(EntityManager em) {
        Cohort cohort = new Cohort()
            .cohortName(DEFAULT_COHORT_NAME);
        return cohort;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cohort createUpdatedEntity(EntityManager em) {
        Cohort cohort = new Cohort()
            .cohortName(UPDATED_COHORT_NAME);
        return cohort;
    }

    @BeforeEach
    public void initTest() {
        cohort = createEntity(em);
    }

    @Test
    @Transactional
    public void createCohort() throws Exception {
        int databaseSizeBeforeCreate = cohortRepository.findAll().size();

        // Create the Cohort
        restCohortMockMvc.perform(post("/api/cohorts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cohort)))
            .andExpect(status().isCreated());

        // Validate the Cohort in the database
        List<Cohort> cohortList = cohortRepository.findAll();
        assertThat(cohortList).hasSize(databaseSizeBeforeCreate + 1);
        Cohort testCohort = cohortList.get(cohortList.size() - 1);
        assertThat(testCohort.getCohortName()).isEqualTo(DEFAULT_COHORT_NAME);
    }

    @Test
    @Transactional
    public void createCohortWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cohortRepository.findAll().size();

        // Create the Cohort with an existing ID
        cohort.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCohortMockMvc.perform(post("/api/cohorts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cohort)))
            .andExpect(status().isBadRequest());

        // Validate the Cohort in the database
        List<Cohort> cohortList = cohortRepository.findAll();
        assertThat(cohortList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCohorts() throws Exception {
        // Initialize the database
        cohortRepository.saveAndFlush(cohort);

        // Get all the cohortList
        restCohortMockMvc.perform(get("/api/cohorts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cohort.getId().intValue())))
            .andExpect(jsonPath("$.[*].cohortName").value(hasItem(DEFAULT_COHORT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCohortsWithEagerRelationshipsIsEnabled() throws Exception {
        CohortResource cohortResource = new CohortResource(cohortRepositoryMock);
        when(cohortRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCohortMockMvc = MockMvcBuilders.standaloneSetup(cohortResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCohortMockMvc.perform(get("/api/cohorts?eagerload=true"))
        .andExpect(status().isOk());

        verify(cohortRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCohortsWithEagerRelationshipsIsNotEnabled() throws Exception {
        CohortResource cohortResource = new CohortResource(cohortRepositoryMock);
            when(cohortRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCohortMockMvc = MockMvcBuilders.standaloneSetup(cohortResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCohortMockMvc.perform(get("/api/cohorts?eagerload=true"))
        .andExpect(status().isOk());

            verify(cohortRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCohort() throws Exception {
        // Initialize the database
        cohortRepository.saveAndFlush(cohort);

        // Get the cohort
        restCohortMockMvc.perform(get("/api/cohorts/{id}", cohort.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cohort.getId().intValue()))
            .andExpect(jsonPath("$.cohortName").value(DEFAULT_COHORT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCohort() throws Exception {
        // Get the cohort
        restCohortMockMvc.perform(get("/api/cohorts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCohort() throws Exception {
        // Initialize the database
        cohortRepository.saveAndFlush(cohort);

        int databaseSizeBeforeUpdate = cohortRepository.findAll().size();

        // Update the cohort
        Cohort updatedCohort = cohortRepository.findById(cohort.getId()).get();
        // Disconnect from session so that the updates on updatedCohort are not directly saved in db
        em.detach(updatedCohort);
        updatedCohort
            .cohortName(UPDATED_COHORT_NAME);

        restCohortMockMvc.perform(put("/api/cohorts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCohort)))
            .andExpect(status().isOk());

        // Validate the Cohort in the database
        List<Cohort> cohortList = cohortRepository.findAll();
        assertThat(cohortList).hasSize(databaseSizeBeforeUpdate);
        Cohort testCohort = cohortList.get(cohortList.size() - 1);
        assertThat(testCohort.getCohortName()).isEqualTo(UPDATED_COHORT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCohort() throws Exception {
        int databaseSizeBeforeUpdate = cohortRepository.findAll().size();

        // Create the Cohort

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCohortMockMvc.perform(put("/api/cohorts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cohort)))
            .andExpect(status().isBadRequest());

        // Validate the Cohort in the database
        List<Cohort> cohortList = cohortRepository.findAll();
        assertThat(cohortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCohort() throws Exception {
        // Initialize the database
        cohortRepository.saveAndFlush(cohort);

        int databaseSizeBeforeDelete = cohortRepository.findAll().size();

        // Delete the cohort
        restCohortMockMvc.perform(delete("/api/cohorts/{id}", cohort.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cohort> cohortList = cohortRepository.findAll();
        assertThat(cohortList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
