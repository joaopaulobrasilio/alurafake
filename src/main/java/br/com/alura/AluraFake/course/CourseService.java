package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.Type;
import excepion.CourseStatusException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public CourseService(CourseRepository courseRepository,
                         TaskRepository taskRepository) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void publishCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        if (course.getStatus() != Status.BUILDING) {
            throw new CourseStatusException("Curso já publicado ou em estado inválido");
        }

        List<Type> types = taskRepository.findTaskTypesByCourseId(courseId);

        if (!types.contains(Type.OPEN_TEXT)
                || !types.contains(Type.SINGLE_CHOICE)
                || !types.contains(Type.MULTIPLE_CHOICE)) {

            throw new CourseStatusException(
                    "Curso deve conter ao menos uma atividade de cada tipo"
            );
        }

        List<Integer> orders = taskRepository.findOrdersByCourseId(courseId);

        if (orders.isEmpty()) {
            throw new CourseStatusException("Curso não possui atividades");
        }

        for (int i = 0; i < orders.size(); i++) {
            int expectedOrder = i + 1;
            if (!orders.get(i).equals(expectedOrder)) {
                throw new CourseStatusException(
                        "As atividades devem estar em ordem sequencial contínua"
                );
            }
        }

        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());

        courseRepository.save(course);
    }
}
