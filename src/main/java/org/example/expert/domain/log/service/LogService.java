package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.enums.LogType;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogRepository logRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createManagerCreateLog(Long managerId, Long todoId) {
        logRepository.save(new Log(managerId, todoId, LogType.MANAGER_REGISTERED));
    }
}
