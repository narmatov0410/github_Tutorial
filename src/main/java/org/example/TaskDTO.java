package org.example;

import lombok.Data;
import java.time.LocalDateTime;
@Data

public class TaskDTO {
    private String id;
    private String title;
    private String content;
    private TaskStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime finishedDate;

}
