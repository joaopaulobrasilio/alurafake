package br.com.alura.AluraFake.course;


import br.com.alura.AluraFake.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CourseReportService courseReportService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newCourseDTO_should_return_not_found_when_user_not_exists() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        when(userRepository.findByEmail("paulo@alura.com.br"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void newCourseDTO__should_return_forbidden_when_user_is_not_instructor() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Angular");
        newCourseDTO.setDescription("Curso de Angular");
        newCourseDTO.setEmailInstructor("joao@alura.com.br");
        User user = new User("Joao", "joao@alura.com.br", Role.STUDENT);

        doReturn(Optional.of(user)).when(userRepository).findByEmail("joao@alura.com.br");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void newCourseDTO__should_return_created_when_user_is_instructor() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Banco de dados");
        newCourseDTO.setDescription("Curso de Banco de dados");
        newCourseDTO.setEmailInstructor("maria@alura.com.br");

        User user = new User("Maria", "maria@alura.com.br", Role.INSTRUCTOR);

        doReturn(Optional.of(user)).when(userRepository).findByEmail("maria@alura.com.br");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllCourses_should_return_list_of_courses() throws Exception {

        Course course1 = new Course("Java Básico", "Curso Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR));
        Course course2 = new Course("Spring Boot", "Curso Spring", new User("Maria", "maria@alura.com.br", Role.INSTRUCTOR));


        when(courseRepository.findAll()).thenReturn(List.of(
                new Course("Java Básico", "Curso Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR)),
                new Course("Spring Boot", "Curso Spring", new User("Maria", "maria@alura.com.br", Role.INSTRUCTOR))
        ));

        mockMvc.perform(get("/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Básico"))
                .andExpect(jsonPath("$[1].title").value("Spring Boot"));
    }

    @Test
    void reportCourses_should_return_course_report() throws Exception {
        Long instructorId = 1L;
        InstructorCourseReportResponse report = new InstructorCourseReportResponse(List.of(), 2L);
        when(courseReportService.generate(instructorId)).thenReturn(report);

        mockMvc.perform(get("/instructor/{id}/courses", instructorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPublishedCourses").value(2));
    }

    @Test
    void publishCourse_should_return_ok() throws Exception {
        Long courseId = 1L;

        doNothing().when(courseService).publishCourse(courseId);

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(courseService, times(1)).publishCourse(courseId);
    }

}