package br.com.alura.AluraFake.task;

import jakarta.persistence.*;

@Entity
@Table(name = "task_options", uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id", "option_text"})})
public class TaskOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "option_text", nullable = false, length = 80)
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    public TaskOption() {
    }

    public TaskOption(Task task, String optionText, boolean correct) {
        this.task = task;
        this.optionText = optionText;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}