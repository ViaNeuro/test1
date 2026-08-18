package com.enterprise.portal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiControllerTest {
    @Autowired MockMvc mvc;
    @Test void userCanCreateAndListOwnTickets() throws Exception {
        mvc.perform(post("/api/tickets").with(httpBasic("user", "password")).contentType(APPLICATION_JSON)
            .content("{\"title\":\"Нет сети\",\"category\":\"Сеть\",\"description\":\"Кабинет 10\",\"priority\":\"HIGH\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("NEW"));
        mvc.perform(get("/api/tickets").with(httpBasic("user", "password"))).andExpect(status().isOk()).andExpect(jsonPath("$[0].title").value("Нет сети"));
    }
    @Test void itCanAssignAndComment() throws Exception {
        mvc.perform(post("/api/tickets").with(httpBasic("user", "password")).contentType(APPLICATION_JSON)
            .content("{\"title\":\"ПК\",\"category\":\"Оборудование\",\"description\":\"Не включается\",\"priority\":\"CRITICAL\"}"));
        mvc.perform(put("/api/it/tickets/1").with(httpBasic("it", "password")).contentType(APPLICATION_JSON).content("{\"status\":\"IN_PROGRESS\",\"assigneeId\":2}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.assignee.username").value("it"));
        mvc.perform(post("/api/it/tickets/1/comments").with(httpBasic("it", "password")).contentType(APPLICATION_JSON).content("{\"text\":\"Взял в работу\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.comments[0].text").value("Взял в работу"));
    }
}
