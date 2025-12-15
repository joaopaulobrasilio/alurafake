package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.Type;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import excepion.CourseStatusException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseServiceTest {


    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository repository;

    @Mock
    private TaskRepository taskRepository;


    @Test
    void when__to_publish_should_occur_successfully() {

        User user = new User("Marcos", "marcos@alura.com.br", Role.INSTRUCTOR);

        Course course = new Course("JavaScript", "Curso de desenvolvimento Web", user);

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskTypesByCourseId(1L)).thenReturn(List.of(Type.SINGLE_CHOICE, Type.OPEN_TEXT, Type.MULTIPLE_CHOICE));
        when(taskRepository.findOrdersByCourseId(1L)).thenReturn(List.of(1, 2, 3));

        courseService.publishCourse(1L);
        verify(taskRepository).findOrdersByCourseId(1L);
        verify(taskRepository).findTaskTypesByCourseId(1L);
        verify(repository, times(1)).save(course);

    }


    @Test
    void should_throw_exception_when_course_not_found() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            courseService.publishCourse(1L);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_course_has_no_activities() {
        User user = new User("José", "jose@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Curso  Python", "Curso de Python avançado", user);

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskTypesByCourseId(1L)).thenReturn(List.of());

        assertThrows(CourseStatusException.class, () -> {
            courseService.publishCourse(1L);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_course_status_is_not_building() {
        User user = new User("Maria", "maria@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java Avançado", "Curso  de Java Avançado", user);
        course.setStatus(Status.PUBLISHED);

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        CourseStatusException exception = assertThrows(CourseStatusException.class, () -> {
            courseService.publishCourse(1L);
        });

        assertEquals("Curso já publicado ou em estado inválido", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_tasks_order_is_not_sequential() {
        User user = new User("Mario", "mario@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java para iniciante", "Curso de Java Iniciante", user);

        course.setStatus(Status.BUILDING);

        when(repository.findById(1L)).thenReturn(Optional.of(course));

        when(taskRepository.findOrdersByCourseId(1L)).thenReturn(List.of(1, 3, 4));

        when(taskRepository.findTaskTypesByCourseId(1L))
                .thenReturn(List.of(Type.OPEN_TEXT, Type.MULTIPLE_CHOICE, Type.SINGLE_CHOICE));

        CourseStatusException exception = assertThrows(CourseStatusException.class, () -> {
            courseService.publishCourse(1L);
        });

        assertEquals("As atividades devem estar em ordem sequencial contínua", exception.getMessage());
        verify(repository, never()).save(any());
    }


    @Test
    void should_throw_exception_when_course_has_order_is_empty() {
        User user = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);

        Course course = new Course("Java Básico", "Curso  de Java Básico", user);

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskTypesByCourseId(1L)).thenReturn(List.of(Type.SINGLE_CHOICE, Type.OPEN_TEXT, Type.MULTIPLE_CHOICE));
        when(taskRepository.findOrdersByCourseId(1L)).thenReturn(List.of());

        assertThrows(CourseStatusException.class, () -> {
            courseService.publishCourse(1L);
        });
        verify(repository, never()).save(any());

    }


}