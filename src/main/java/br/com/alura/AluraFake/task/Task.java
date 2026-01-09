package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks", uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "statement"}), @UniqueConstraint(columnNames = {"course_id", "order_number"})})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String statement;

    @Column(name = "order_number", nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Type type;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskOption> options = new ArrayList<>();

    public Task() {
    }

    public Task(Course course, String statement, Integer order, Type type) {
        this.course = course;
        this.statement = statement;
        this.order = order;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }

}
