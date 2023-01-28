package hexlet.code.service.interfaces;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

public interface TaskStatusService {

    TaskStatus createNewStatus(TaskStatusDto taskStatusDto);

    TaskStatus updateStatus(long id, TaskStatusDto taskStatusDto);
}
