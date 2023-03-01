package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.STATUS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;
    private final TaskStatusRepository taskStatusRepository;

    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = TaskStatus.class))))
    @GetMapping
    @Operation(summary = "Get all statuses")
    public List<TaskStatus> getAll() {

        return taskStatusRepository.findAll()
                .stream()
                .toList();
    }

    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    @Operation(summary = "Get status")
    public TaskStatus getById(@PathVariable long id) {

        return taskStatusRepository.findById(id).isPresent()
                ? taskStatusRepository.findById(id).get() : null;
    }

    @Operation(summary = "Create new status")
    @ApiResponse(responseCode = "201", description = "Status created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createTaskStatus(@RequestBody @Valid TaskStatusDto taskStatusDto) {

        return taskStatusService.createNewStatus(taskStatusDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status update"),
        @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    @PutMapping(ID)
    @Operation(summary = "Update status")
    public TaskStatus updateTaskStatus(@PathVariable long id,
                                       @RequestBody @Valid TaskStatusDto taskStatusDto) {

        return taskStatusService.updateStatus(id, taskStatusDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status delete"),
        @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    @Operation(summary = "Delete status")
    @DeleteMapping(ID)
    public void deleteTaskStatus(@PathVariable long id) {
        taskStatusRepository.deleteById(id);
    }
}
