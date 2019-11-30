package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Scenario;
import com.mycompany.myapp.repository.ScenarioRepository;
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
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ScenarioResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class ScenarioResourceIT {

    private static final String DEFAULT_SCENARIO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCENARIO_NAME = "BBBBBBBBBB";

    @Autowired
    private ScenarioRepository scenarioRepository;

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

    private MockMvc restScenarioMockMvc;

    private Scenario scenario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScenarioResource scenarioResource = new ScenarioResource(scenarioRepository);
        this.restScenarioMockMvc = MockMvcBuilders.standaloneSetup(scenarioResource)
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
    public static Scenario createEntity(EntityManager em) {
        Scenario scenario = new Scenario()
            .scenarioName(DEFAULT_SCENARIO_NAME);
        return scenario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scenario createUpdatedEntity(EntityManager em) {
        Scenario scenario = new Scenario()
            .scenarioName(UPDATED_SCENARIO_NAME);
        return scenario;
    }

    @BeforeEach
    public void initTest() {
        scenario = createEntity(em);
    }

    @Test
    @Transactional
    public void createScenario() throws Exception {
        int databaseSizeBeforeCreate = scenarioRepository.findAll().size();

        // Create the Scenario
        restScenarioMockMvc.perform(post("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isCreated());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeCreate + 1);
        Scenario testScenario = scenarioList.get(scenarioList.size() - 1);
        assertThat(testScenario.getScenarioName()).isEqualTo(DEFAULT_SCENARIO_NAME);
    }

    @Test
    @Transactional
    public void createScenarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scenarioRepository.findAll().size();

        // Create the Scenario with an existing ID
        scenario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScenarioMockMvc.perform(post("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isBadRequest());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllScenarios() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get all the scenarioList
        restScenarioMockMvc.perform(get("/api/scenarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].scenarioName").value(hasItem(DEFAULT_SCENARIO_NAME)));
    }
    
    @Test
    @Transactional
    public void getScenario() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get the scenario
        restScenarioMockMvc.perform(get("/api/scenarios/{id}", scenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scenario.getId().intValue()))
            .andExpect(jsonPath("$.scenarioName").value(DEFAULT_SCENARIO_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingScenario() throws Exception {
        // Get the scenario
        restScenarioMockMvc.perform(get("/api/scenarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScenario() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        int databaseSizeBeforeUpdate = scenarioRepository.findAll().size();

        // Update the scenario
        Scenario updatedScenario = scenarioRepository.findById(scenario.getId()).get();
        // Disconnect from session so that the updates on updatedScenario are not directly saved in db
        em.detach(updatedScenario);
        updatedScenario
            .scenarioName(UPDATED_SCENARIO_NAME);

        restScenarioMockMvc.perform(put("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScenario)))
            .andExpect(status().isOk());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeUpdate);
        Scenario testScenario = scenarioList.get(scenarioList.size() - 1);
        assertThat(testScenario.getScenarioName()).isEqualTo(UPDATED_SCENARIO_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingScenario() throws Exception {
        int databaseSizeBeforeUpdate = scenarioRepository.findAll().size();

        // Create the Scenario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScenarioMockMvc.perform(put("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isBadRequest());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScenario() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        int databaseSizeBeforeDelete = scenarioRepository.findAll().size();

        // Delete the scenario
        restScenarioMockMvc.perform(delete("/api/scenarios/{id}", scenario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
