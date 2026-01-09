package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.dto.CourseReportItemDTO;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import excepion.CourseStatusException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseReportService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public CourseReportService(UserRepository userRepository,
                               CourseRepository courseRepository,
                               TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public InstructorCourseReportResponse generate(Long instructorId) {

        User user = userRepository.findById(instructorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado")
                );

        if (user.getRole() != Role.INSTRUCTOR) {
            throw new CourseStatusException("Usuário não é instrutor");
        }

        List<Course> courses =
                courseRepository.findByInstructorId(instructorId);

        List<CourseReportItemDTO> reportItems = new ArrayList<>();
        for (Course course : courses) {
            long taskCount = taskRepository.countByCourseId(course.getId());
            CourseReportItemDTO dto = new CourseReportItemDTO(
                    course.getId(),
                    course.getTitle(),
                    course.getStatus(),
                    course.getPublishedAt(),
                    taskCount
            );
            reportItems.add(dto);
        }

        long totalPublished =
                courseRepository.countByInstructorIdAndStatus(
                        instructorId, Status.PUBLISHED
                );

        return new InstructorCourseReportResponse(reportItems, totalPublished);
    }

}
