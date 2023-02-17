package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public final TaskStatus createNewStatus(final TaskStatusDto taskStatusDto) {

        final TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());

        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public final TaskStatus updateStatus(final long id, final TaskStatusDto taskStatusDto) {

        final TaskStatus taskStatusUpdate = taskStatusRepository.findById(id).isPresent()
                ? taskStatusRepository.findById(id).get() : null;
        assert taskStatusUpdate != null;
        taskStatusUpdate.setName(taskStatusDto.getName());

        return taskStatusRepository.save(taskStatusUpdate);
    }
}
