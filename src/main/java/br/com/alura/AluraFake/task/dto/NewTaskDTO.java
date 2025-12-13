package br.com.alura.AluraFake.task.dto;

import br.com.alura.AluraFake.task.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class NewTaskDTO {
    @NotBlank
    @Size(min = 4, max = 255)
    private String statement;

    @NotNull
    private Integer order;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @NotNull
    private Long courseId;  // ← adicione este campo

    private List<TaskOptionDTO> options;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<TaskOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOptionDTO> options) {
        this.options = options;
    }



}
