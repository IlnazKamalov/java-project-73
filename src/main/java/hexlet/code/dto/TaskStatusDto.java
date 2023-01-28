package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {

    @NotBlank
    private String name;
}
