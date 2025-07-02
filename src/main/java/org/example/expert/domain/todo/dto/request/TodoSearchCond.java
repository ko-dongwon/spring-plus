package org.example.expert.domain.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TodoSearchCond {
    private String weather;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String managerName;
    private String title;

    public TodoSearchCond(String weather, LocalDateTime startDate, LocalDateTime endDate, String managerName, String title) {
        this.weather = weather;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managerName = managerName;
        this.title = title;
    }
}
