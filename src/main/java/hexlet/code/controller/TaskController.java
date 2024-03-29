package hexlet.code.controller;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import com.querydsl.core.types.Predicate;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TaskController.TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";

    private static final String TASK_OWNER =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Task registerNewTask(@RequestBody @Valid final TaskDto dto) {

        return taskService.createNewTask(dto);
    }

    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = Task.class))))
    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<Task> getAllTasks(@QuerydslPredicate final Predicate predicate) {

        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    @Operation(summary = "Get task")
    public Task getTaskById(@PathVariable final Long id) {

        return taskRepository.findById(id).get();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task update"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping(ID)
    @Operation(summary = "Update task")
    @PreAuthorize(TASK_OWNER)
    public Task updateTask(@PathVariable final long id,
                           @RequestBody @Valid final TaskDto dto) {

        return taskService.updateTask(id, dto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task delete"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping(ID)
    @Operation(summary = "Delete task")
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final long id) {

        taskRepository.deleteById(id);
    }
}
