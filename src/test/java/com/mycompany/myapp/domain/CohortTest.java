package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class CohortTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cohort.class);
        Cohort cohort1 = new Cohort();
        cohort1.setId(1L);
        Cohort cohort2 = new Cohort();
        cohort2.setId(cohort1.getId());
        assertThat(cohort1).isEqualTo(cohort2);
        cohort2.setId(2L);
        assertThat(cohort1).isNotEqualTo(cohort2);
        cohort1.setId(null);
        assertThat(cohort1).isNotEqualTo(cohort2);
    }
}
