package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.dto.CourseReportItemDTO;

import java.util.List;

public class InstructorCourseReportResponse {
    private final List<CourseReportItemDTO> courses;
    private final Long totalPublishedCourses;

    public InstructorCourseReportResponse(
            List<CourseReportItemDTO> courses,
            Long totalPublishedCourses
    ) {
        this.courses = courses;
        this.totalPublishedCourses = totalPublishedCourses;
    }

    public List<CourseReportItemDTO> getCourses() {
        return courses;
    }

    public Long getTotalPublishedCourses() {
        return totalPublishedCourses;
    }
}
