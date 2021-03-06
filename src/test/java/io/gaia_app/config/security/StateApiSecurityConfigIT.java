package io.gaia_app.config.security;

import io.gaia_app.test.SharedMongoContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class StateApiSecurityConfigIT extends SharedMongoContainerTest {

    @Autowired
    private StateApiSecurityConfig.StateApiSecurityProperties props;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void gaiaBackend_shouldHaveAccessToStateApi() throws Exception {
        mockMvc.perform(get("/api/state/test").with(httpBasic(props.getUsername(), props.getPassword())))
                .andExpect(authenticated().withUsername("gaia-backend").withRoles("STATE"));
    }

    @Test
    void gaiaBackend_shouldHaveAccessToStateApiWithPost() throws Exception {
        mockMvc.perform(post("/api/state/test")
            .content("{}")
            .contentType("application/json")
            .with(httpBasic(props.getUsername(), props.getPassword())))
            .andExpect(authenticated().withUsername("gaia-backend").withRoles("STATE"));
    }

    @Test
    void gaiaBackend_shouldNotHaveAccessToOtherApis() throws Exception {
        mockMvc.perform(get("/api/modules/test").with(httpBasic(props.getUsername(), props.getPassword())))
                .andExpect(unauthenticated());
    }

    @Test
    void gaiaBackend_shouldNotHaveAccessToScreens() throws Exception {
        mockMvc.perform(get("/modules/test").with(httpBasic(props.getUsername(), props.getPassword())))
                .andExpect(unauthenticated());
    }


}
