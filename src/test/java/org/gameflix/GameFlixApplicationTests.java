package org.gameflix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest
class GameFlixApplicationTests {

    private final ApplicationContext applicationContext;
    private final MockMvc mockMvc;

    @Autowired
    GameFlixApplicationTests(ApplicationContext applicationContext, MockMvc mockMvc) {
        this.applicationContext = applicationContext;
        this.mockMvc = mockMvc;
    }

    @Test
    void applicationContextContainsGameFlixApplicationBean() {
        assertThat(applicationContext.getBeanNamesForType(GameFlixApplication.class)).hasSize(1);
    }

    @Test
    void homePageIsPubliclyAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

}
