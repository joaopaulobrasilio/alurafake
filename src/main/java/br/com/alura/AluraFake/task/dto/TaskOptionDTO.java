package br.com.alura.AluraFake.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskOptionDTO {
    @NotBlank
    @Size(min = 4, max = 80)
    private String option;

    @JsonProperty("isCorrect")
    private boolean isCorrect;

    public TaskOptionDTO(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    @JsonProperty("isCorrect")
    public boolean isCorrect() {
        return isCorrect;
    }
    @JsonProperty("isCorrect")
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    public String getOption() {
        return option;
    }
    public void setOption(String option) {
        this.option = option;
    }


}
