package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import br.com.alura.AluraFake.util.ValidationExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import static org.mockito.Mockito.verify;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ValidationExceptionHandler.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_create_open_text_task() throws Exception {

        NewTaskDTO dto = new NewTaskDTO("Explique o que é Java", 1, 1L, List.of());

        mockMvc.perform(
                        post("/task/new/opentext")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());

        verify(taskService).createOpenTextTask(any(NewTaskDTO.class));
    }

    @Test
    void should_create_single_choice_task() throws Exception {
        NewTaskDTO dto = new NewTaskDTO("Explique oque é Angular", 1, 1L, List.of());

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(taskService).createSingleChoiceTask(any(NewTaskDTO.class));
    }

    @Test
    void should_create_multiple_choice_task() throws Exception {
        NewTaskDTO dto = new NewTaskDTO("O que é uma Lista", 1, 1L, List.of());

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(taskService).createMultipleChoiceTask(any(NewTaskDTO.class));
    }


}

