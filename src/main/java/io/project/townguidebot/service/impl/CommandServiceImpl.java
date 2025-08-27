package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.Command;
import io.project.townguidebot.repository.CommandRepository;
import io.project.townguidebot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Command> getAllCommand() {
        log.info("Get all commands...");
        return commandRepository.findAll();
    }
}
