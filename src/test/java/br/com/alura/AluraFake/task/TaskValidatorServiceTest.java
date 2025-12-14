package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import excepion.CourseStatusException;
import excepion.TaskValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TaskValidatorServiceTest {

    private TaskValidatorService validator;

    @BeforeEach
    void setup() {
        validator = new TaskValidatorService();
    }

    @Test
    void should_accept_valid_statement() {
        assertDoesNotThrow(() ->
                validator.validateStatement("Explique o que é Java")
        );
    }

    @Test
    void should_throw_exception_when_statement_is_invalid() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateStatement(null));

        assertThrows(TaskValidationException.class,
                () -> validator.validateStatement("abc"));

        assertThrows(TaskValidationException.class,
                () -> validator.validateStatement("a".repeat(256)));
    }

    @Test
    void should_throw_exception_when_course_is_not_building() {
        User instructor = new User("João", "joao@alura.com.br", Role.INSTRUCTOR);

        Course course = new Course(
                "Java Básico",
                "Curso de Java",
                instructor
        );
        course.setStatus(Status.PUBLISHED);

        assertThrows(CourseStatusException.class,
                () -> validator.validateCourseStatus(course));
    }

    @Test
    void should_accept_course_with_valid_status() {
        User instructor = new User("João", "joao@alura.com.br", Role.INSTRUCTOR);

        Course course = new Course(
                "Java Básico",
                "Curso de Java",
                instructor
        );
        course.setStatus(Status.BUILDING);

        assertDoesNotThrow(() ->
                validator.validateCourseStatus(course));
    }

    @Test
    void should_throw_exception_when_order_is_invalid() {

        assertThrows(TaskValidationException.class,
                () -> validator.validateOrder(0, 2));

        assertThrows(TaskValidationException.class,
                () -> validator.validateOrder(-1, 2));

        assertThrows(TaskValidationException.class,
                () -> validator.validateOrder(5, 2));
    }

    @Test
    void should_accept_valid_order() {
        assertDoesNotThrow(() ->
                validator.validateOrder(3, 2));
    }

    @Test
    void should_ignore_options_for_open_text() {
        assertDoesNotThrow(() ->
                validator.validateOptions(null, "Explique Java", Type.OPEN_TEXT));
    }

    @Test
    void should_throw_exception_when_options_are_empty() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(List.of(), "O que aprendemos na aula de hoje?", Type.SINGLE_CHOICE));
    }

    @Test
    void should_throw_exception_when_option_equals_statement() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(new TaskOptionDTO("O que é Spring", true)),
                        "O que é POO",
                        Type.SINGLE_CHOICE
                ));
    }

    @Test
    void should_throw_exception_when_options_are_duplicated() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", true),
                                new TaskOptionDTO("Java", false)
                        ),
                        "O que é Java?",
                        Type.SINGLE_CHOICE
                ));
    }

    @Test
    void should_throw_exception_when_single_choice_has_multiple_correct() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", true),
                                new TaskOptionDTO("Kotlin", true)
                        ),
                        "O que é Angular?",
                        Type.SINGLE_CHOICE
                ));
    }

    @Test
    void should_throw_exception_when_single_choice_has_no_correct() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", false),
                                new TaskOptionDTO("Kotlin", false)
                        ),
                        "Defina oque é uma classe?",
                        Type.SINGLE_CHOICE
                ));
    }

    @Test
    void should_accept_valid_single_choice_options() {
        assertDoesNotThrow(() ->
                validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", true),
                                new TaskOptionDTO("Kotlin", false)
                        ),
                        "Oque é estrutura de repetição?",
                        Type.SINGLE_CHOICE
                ));
    }

    @Test
    void should_throw_exception_when_multiple_choice_has_less_than_two_correct() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", true),
                                new TaskOptionDTO("Python", false),
                                new TaskOptionDTO("C#", false)
                        ),
                        "Oque é Herença?",
                        Type.MULTIPLE_CHOICE
                ));
    }

    @Test
    void should_throw_exception_when_multiple_choice_has_all_correct() {
        assertThrows(TaskValidationException.class,
                () -> validator.validateOptions(
                        List.of(
                                new TaskOptionDTO("Java", true),
                                new TaskOptionDTO("Kotlin", true),
                                new TaskOptionDTO("Scala", true)
                        ),
                        "O que é polimorfismo ?",
                        Type.MULTIPLE_CHOICE
                ));
    }


}