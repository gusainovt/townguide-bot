package io.project.townguidebot.controller.impl;

import gusainov.library.annotation.LogUserRequest;
import io.project.townguidebot.controller.AdsController;
import io.project.townguidebot.dto.AdDto;
import io.project.townguidebot.service.AdsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdsControllerImpl implements AdsController {

    private final AdsService adsService;

    @Override
    @LogUserRequest(method = "findAllAds")
    public ResponseEntity<List<AdDto>> findAllAds() {
        return ResponseEntity.ok(adsService.findAllAds());
    }

    @Override
    @LogUserRequest(method = "getAdById")
    public ResponseEntity<AdDto> getAdById(long id) {
        return ResponseEntity.ok(adsService.findAdById(id));
    }

    @Override
    @LogUserRequest(method = "createAd")
    public ResponseEntity<AdDto> createAd(AdDto adDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.createAd(adDto));
    }

    @Override
    @LogUserRequest(method = "updateAd")
    public ResponseEntity<AdDto> updateAd(long id, AdDto adDTO) {
        return ResponseEntity.ok(adsService.updateAd(id, adDTO));
    }

    @Override
    @LogUserRequest(method = "deleteAd")
    public ResponseEntity<Void> deleteAd(long id) {
        adsService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }
}
