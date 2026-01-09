package br.com.alura.AluraFake.course.dto;

import br.com.alura.AluraFake.course.Status;

import java.time.LocalDateTime;

public class CourseReportItemDTO {
    private Long id;
    private String title;
    private Status status;
    private LocalDateTime publishedAt;
    private Long taskCount;

    public CourseReportItemDTO(
            Long id,
            String title,
            Status status,
            LocalDateTime publishedAt,
            Long taskCount
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.publishedAt = publishedAt;
        this.taskCount = taskCount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Long getTaskCount() {
        return taskCount;
    }
}
