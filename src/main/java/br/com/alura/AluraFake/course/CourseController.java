package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.user.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    private final CourseReportService courseReportService;
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseRepository courseRepository, UserRepository userRepository, CourseReportService courseReportService, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseReportService = courseReportService;
        this.courseService = courseService;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        // email do usuário autenticado

        User instructor = userRepository.findByEmail(newCourse.getEmailInstructor())
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado")
                );

        if (!instructor.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), instructor);

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> createCourse() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{id}/courses")
    public ResponseEntity<InstructorCourseReportResponse> reportCourses(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(courseReportService.generate(id));
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity createCourse(@PathVariable("id") Long id) {

        courseService.publishCourse(id);
        return ResponseEntity.ok().build();
    }

}
