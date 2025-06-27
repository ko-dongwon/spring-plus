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

    public TodoSearchCond(String weather, LocalDateTime startDate, LocalDateTime endDate) {
        this.weather = weather;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
