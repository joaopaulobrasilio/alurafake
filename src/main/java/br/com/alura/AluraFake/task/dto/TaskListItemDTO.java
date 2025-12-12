package br.com.alura.AluraFake.task.dto;

import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.Type;

public class TaskListItemDTO {
    private Long id;
    private String statement;
    private Type type;
    private Integer order;

    private int optionsCount;

    public TaskListItemDTO(Task task) {
        this.id = task.getId();
        this.statement = task.getStatement();
        this.type = task.getType();
        this.order = task.getOrder();
        this.optionsCount = task.getOptions() != null ? task.getOptions().size() : 0;
    }

    public Long getId() {
        return id;
    }

    public String getStatement() {
        return statement;
    }

    public Type getType() {
        return type;
    }

    public Integer getOrder() {
        return order;
    }

}
