package io.project.BorovskBot.service;

import java.io.IOException;

public interface PhotoService {
    void uploadPhoto(Long placeId) throws IOException;
}
