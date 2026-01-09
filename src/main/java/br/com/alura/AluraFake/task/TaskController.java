package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/new/opentext")
    public ResponseEntity newOpenTextExercise(@RequestBody @Valid NewTaskDTO dto) {
        taskService.createOpenTextTask(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/task/new/singlechoice")
    public ResponseEntity newSingleChoice(@RequestBody @Valid NewTaskDTO dto) {
        taskService.createSingleChoiceTask(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity newMultipleChoice(@RequestBody @Valid NewTaskDTO dto) {
        taskService.createMultipleChoiceTask(dto);
        return ResponseEntity.ok().build();
    }

}