package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class UserAcquisitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAcquisition.class);
        UserAcquisition userAcquisition1 = new UserAcquisition();
        userAcquisition1.setId(1L);
        UserAcquisition userAcquisition2 = new UserAcquisition();
        userAcquisition2.setId(userAcquisition1.getId());
        assertThat(userAcquisition1).isEqualTo(userAcquisition2);
        userAcquisition2.setId(2L);
        assertThat(userAcquisition1).isNotEqualTo(userAcquisition2);
        userAcquisition1.setId(null);
        assertThat(userAcquisition1).isNotEqualTo(userAcquisition2);
    }
}
