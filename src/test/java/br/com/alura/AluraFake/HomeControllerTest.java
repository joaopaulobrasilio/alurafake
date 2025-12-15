package br.com.alura.AluraFake;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void should_return_home_page_html() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())

                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "<h1>Bem vindo ao teste de java Alura</h1>"
                        )
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "<a href=\"/user/all\">Usuários cadastrados</a>"
                        )
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "<a href=\"/course/all\">Cursos cadastrados</a>"
                        )
                ));
    }

}