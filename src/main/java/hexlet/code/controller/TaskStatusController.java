package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.STATUS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    @Autowired
    private final TaskStatusService taskStatusService;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @GetMapping
    public final List<TaskStatus> getAll() {

        return new ArrayList<>(taskStatusRepository.findAll());
    }

    @GetMapping(ID)
    public final TaskStatus getById(@PathVariable long id) {

        return taskStatusRepository.findById(id).get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public final TaskStatus createTaskStatus(@RequestBody TaskStatusDto taskStatusDto) {

        return taskStatusService.createNewStatus(taskStatusDto);
    }

    @PutMapping(ID)
    public final TaskStatus updateTaskStatus(@PathVariable long id,
                                       @RequestBody TaskStatusDto taskStatusDto) {

        return taskStatusService.updateStatus(id, taskStatusDto);
    }

    @DeleteMapping(ID)
    public final void deleteTaskStatus(@PathVariable long id) {
        taskStatusRepository.deleteById(id);
    }
}
