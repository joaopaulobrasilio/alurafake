package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;

import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import excepion.TaskValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class TaskServiceTest {


    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    TaskValidatorService taskValidatorService;

    @Test
    void should_create_open_text_task_successfully() {

        User instructor = new User("Maria", "maria@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Curso de desenvolvimento web", " iniciando com html,css e JS", instructor);
        course.setId(1L);

        NewTaskDTO dto =
                new NewTaskDTO("Explique o que é JS ", 1, 1L, List.of());

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(false);

        when(taskRepository.findMaxOrderByCourseId(1L))
                .thenReturn(Optional.of(0));

        taskService.createOpenTextTask(dto);

        verify(taskRepository).shiftOrderTemporarily(1L, 1);
        verify(taskRepository).normalizeOrder(1L, 1);
        verify(taskRepository).save(any(Task.class));

        verify(taskValidatorService).validateCourseStatus(course);
        verify(taskValidatorService).validateStatement(dto.getStatement());
        verify(taskValidatorService).validateOrder(1, 0);
    }

    @Test
    void should_throw_exception_when_course_not_found() {

        NewTaskDTO dto =
                new NewTaskDTO("Explique  o que é Java?", 1, 99L, List.of());

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> taskService.createOpenTextTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }


    @Test
    void should_throw_exception_when_statement_already_exists() {

        User instructor = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Python", "Curso de python", instructor);
        course.setId(1L);

        NewTaskDTO dto =
                new NewTaskDTO("Qual a diferença de python e java ?", 1, 1L, List.of());

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(true);

        assertThrows(
                TaskValidationException.class,
                () -> taskService.createOpenTextTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }

    @Test
    void should_create_single_choice_task_successfully() {

        User instructor = new User("Marcia", "marcia@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Curso de banco de dados", "Curso de banco de dados noSQL", instructor);
        course.setId(1L);

        NewTaskDTO dto = new NewTaskDTO(
                "O que é Java?", 1, 1L, List.of(
                new TaskOptionDTO("Linguagem", true),
                new TaskOptionDTO("Banco de dados", false)
        )
        );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(false);

        when(taskRepository.findMaxOrderByCourseId(1L))
                .thenReturn(Optional.of(0));

        taskService.createSingleChoiceTask(dto);

        verify(taskValidatorService).validateCourseStatus(course);
        verify(taskValidatorService).validateStatement(dto.getStatement());
        verify(taskValidatorService).validateOrder(1, 0);
        verify(taskValidatorService).validateOptions(dto.getOptions(), dto.getStatement(), Type.SINGLE_CHOICE);

        verify(taskRepository).shiftOrderTemporarily(1L, 1);
        verify(taskRepository).normalizeOrder(1L, 1);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void should_throw_exception_when_single_choice_options_are_invalid() {

        User instructor = new User("Marcos", "marcos@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("JavaScript", "JavaScript básico", instructor);
        course.setId(1L);

        NewTaskDTO dto = new NewTaskDTO(
                "O que é JavaScript?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Linguagem", true),
                        new TaskOptionDTO("Plataforma", true)
                )
        );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(false);

        when(taskRepository.findMaxOrderByCourseId(1L))
                .thenReturn(Optional.of(0));

        doThrow(new TaskValidationException("Opções inválidas"))
                .when(taskValidatorService)
                .validateOptions(dto.getOptions(), dto.getStatement(), Type.SINGLE_CHOICE);

        assertThrows(
                TaskValidationException.class,
                () -> taskService.createSingleChoiceTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_course_not_found_single_choice() {

        NewTaskDTO dto = new NewTaskDTO(
                "O que é Java?",
                1,
                99L,
                List.of()
        );

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> taskService.createSingleChoiceTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }

    @Test
    void should_create_multiple_choice_task_successfully() {

        User instructor = new User("Mario", "mario@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso Java", instructor);
        course.setId(1L);

        NewTaskDTO dto = new NewTaskDTO(
                "Quais são linguagens da JVM?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Kotlin", true),
                        new TaskOptionDTO("Python", false)
                )
        );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(false);

        when(taskRepository.findMaxOrderByCourseId(1L))
                .thenReturn(Optional.of(0));

        taskService.createMultipleChoiceTask(dto);

        verify(taskValidatorService).validateCourseStatus(course);
        verify(taskValidatorService).validateStatement(dto.getStatement());
        verify(taskValidatorService).validateOrder(1, 0);

        verify(taskRepository).shiftOrderTemporarily(1L, 1);
        verify(taskRepository).normalizeOrder(1L, 1);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void should_throw_exception_when_course_not_found_multiple_choice() {

        NewTaskDTO dto = new NewTaskDTO(
                "Quais são linguagens da JVM?",
                1,
                99L,
                List.of()
        );

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> taskService.createMultipleChoiceTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_statement_already_exists_multiple_choice() {

        User instructor = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso Java", instructor);
        course.setId(1L);

        NewTaskDTO dto = new NewTaskDTO(
                "Quais são linguagens da JVM?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Kotlin", true)
                )
        );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(true);

        assertThrows(
                TaskValidationException.class,
                () -> taskService.createMultipleChoiceTask(dto)
        );

        verify(taskRepository, never()).save(any());
    }


    @Test
    void should_throw_TaskValidationException_when_statement_already_exists_single_choice() {

        User instructor = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso Java", instructor);
        course.setId(1L);

        NewTaskDTO dto = new NewTaskDTO(
                "O que é Java?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Linguagem", true),
                        new TaskOptionDTO("Plataforma", false)
                )
        );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(taskRepository.existsByCourseIdAndStatement(1L, dto.getStatement()))
                .thenReturn(true);

        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskService.createSingleChoiceTask(dto)
        );

        assertEquals(
                "Já existe uma atividade com este enunciado neste curso.",
                exception.getMessage()
        );

        verify(taskRepository, never()).save(any());
        verify(taskRepository, never()).shiftOrderTemporarily(anyLong(), anyInt());
        verify(taskRepository, never()).normalizeOrder(anyLong(), anyInt());
    }




}