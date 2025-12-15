package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import excepion.TaskValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;
    private final TaskValidatorService validator;

    public TaskService(TaskRepository taskRepository, CourseRepository courseRepository,
                       TaskValidatorService validator) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
        this.validator = validator;
    }

    @Transactional
    public void createOpenTextTask(NewTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        validator.validateCourseStatus(course);
        validator.validateStatement(dto.getStatement());

        if (taskRepository.existsByCourseIdAndStatement(course.getId(), dto.getStatement())) {
            throw new TaskValidationException("Já existe uma atividade com este enunciado neste curso.");
        }

        int maxOrder = taskRepository.findMaxOrderByCourseId(course.getId()).orElse(0);
        validator.validateOrder(dto.getOrder(), maxOrder);

        taskRepository.shiftOrderTemporarily(course.getId(), dto.getOrder());
        taskRepository.normalizeOrder(course.getId(), dto.getOrder());

        Task task = new Task();
        task.setCourse(course);
        task.setStatement(dto.getStatement());
        task.setOrder(dto.getOrder());
        task.setType(Type.OPEN_TEXT);

        taskRepository.save(task);
    }

    @Transactional
    public void createSingleChoiceTask(NewTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        validator.validateCourseStatus(course);
        validator.validateStatement(dto.getStatement());

        if (taskRepository.existsByCourseIdAndStatement(course.getId(), dto.getStatement())) {
            throw new TaskValidationException("Já existe uma atividade com este enunciado neste curso.");
        }

        int maxOrder = taskRepository.findMaxOrderByCourseId(course.getId()).orElse(0);
        validator.validateOrder(dto.getOrder(), maxOrder);

        validator.validateOptions(dto.getOptions(), dto.getStatement(), Type.SINGLE_CHOICE);

        taskRepository.shiftOrderTemporarily(course.getId(), dto.getOrder());
        taskRepository.normalizeOrder(course.getId(), dto.getOrder());

        Task task = new Task();
        task.setCourse(course);
        task.setStatement(dto.getStatement());
        task.setOrder(dto.getOrder());
        task.setType(Type.SINGLE_CHOICE);

        List<TaskOption> options = new ArrayList<>();
        for (TaskOptionDTO o : dto.getOptions()) {
            TaskOption option = new TaskOption(task, o.getOption(), o.isCorrect());
            options.add(option);
        }
        task.setOptions(options);

        taskRepository.save(task);
    }

    @Transactional
    public void createMultipleChoiceTask(NewTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        validator.validateCourseStatus(course);
        validator.validateStatement(dto.getStatement());

        validator.validateOptions(dto.getOptions(), dto.getStatement(), Type.MULTIPLE_CHOICE);

        if (taskRepository.existsByCourseIdAndStatement(course.getId(), dto.getStatement())) {
            throw new TaskValidationException("Já existe uma atividade com este enunciado neste curso.");
        }

        int maxOrder = taskRepository.findMaxOrderByCourseId(course.getId()).orElse(0);
        validator.validateOrder(dto.getOrder(), maxOrder);

        taskRepository.shiftOrderTemporarily(course.getId(), dto.getOrder());
        taskRepository.normalizeOrder(course.getId(), dto.getOrder());

        Task task = new Task();
        task.setCourse(course);
        task.setStatement(dto.getStatement());
        task.setOrder(dto.getOrder());
        task.setType(Type.MULTIPLE_CHOICE);

        List<TaskOption> options = new ArrayList<>();

        for (TaskOptionDTO o : dto.getOptions()) {
            TaskOption taskOption = new TaskOption(task, o.getOption(), o.isCorrect());
            options.add(taskOption);
        }

        task.setOptions(options);

        taskRepository.save(task);
    }

}
