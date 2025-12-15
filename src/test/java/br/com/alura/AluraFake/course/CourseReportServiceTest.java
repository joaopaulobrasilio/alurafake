package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import excepion.CourseStatusException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseReportServiceTest {

    @InjectMocks
    private CourseReportService courseReportService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TaskRepository taskRepository;

    @Test
    void when_generate_report_should_occur_successfully() {

        User user = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);

        Course course = new Course("Java", "Curso de Java", user);
        course.setId(10L);
        course.setStatus(Status.PUBLISHED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findByInstructorId(1L)).thenReturn(List.of(course));
        when(taskRepository.countByCourseId(10L)).thenReturn(3L);
        when(courseRepository.countByInstructorIdAndStatus(1L, Status.PUBLISHED))
                .thenReturn(1L);

        InstructorCourseReportResponse response =
                courseReportService.generate(1L);

        assertNotNull(response);
        assertEquals(1, response.getCourses().size());
        assertEquals(1L, response.getTotalPublishedCourses());

        verify(userRepository).findById(1L);
        verify(courseRepository).findByInstructorId(1L);
        verify(taskRepository).countByCourseId(10L);
    }


    @Test
    void should_throw_exception_when_user_not_found() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class, () -> {
                    courseReportService.generate(1L);
                });

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(courseRepository, taskRepository);
    }


    @Test
    void should_throw_exception_when_user_is_not_instructor() {

        User user = new User("Joao", "joao@alura.com.br", Role.STUDENT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        CourseStatusException exception =
                assertThrows(CourseStatusException.class, () -> {
                    courseReportService.generate(1L);
                });

        assertEquals("Usuário não é instrutor", exception.getMessage());

        verify(userRepository).findById(1L);
        verifyNoInteractions(courseRepository, taskRepository);
    }

}