package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ChurnTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Churn.class);
        Churn churn1 = new Churn();
        churn1.setId(1L);
        Churn churn2 = new Churn();
        churn2.setId(churn1.getId());
        assertThat(churn1).isEqualTo(churn2);
        churn2.setId(2L);
        assertThat(churn1).isNotEqualTo(churn2);
        churn1.setId(null);
        assertThat(churn1).isNotEqualTo(churn2);
    }
}
