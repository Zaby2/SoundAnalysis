package org.sound.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sound.dto.SoundPitchDto;
import org.sound.dto.SoundSpectreDto;
import org.sound.service.PitchDetectorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/audio/compare")
@RequiredArgsConstructor
@Slf4j
public class SoundSettingsController {

    private final PitchDetectorService pitchDetectorService;

    @PostMapping("/pitch")
    public SoundPitchDto compareFilesFrequency(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2) {
        var firstFq = pitchDetectorService.detectFreq(file1);
        var secondFq = pitchDetectorService.detectFreq(file2);

        var diff = Math.abs(firstFq - secondFq);

        var response = new SoundPitchDto();
        response.setFirstFileFrequency(String.valueOf(firstFq));
        response.setSecondFileFrequency(String.valueOf(secondFq));
        response.setDiffFrequency(String.valueOf(diff));
        return response;
    }

    @PostMapping("/spectre")
    public SoundSpectreDto compareFilesSpectre(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2) {

        var frequencyBinsFile1 = pitchDetectorService.analyzeSpectrum(file1);
        var frequencyBinsFile2 = pitchDetectorService.analyzeSpectrum(file2);
        log.debug(String.valueOf(frequencyBinsFile1.size()));
        log.debug(String.valueOf(frequencyBinsFile2.size()));

        var response = new SoundSpectreDto();
        response.setFile1Fq(frequencyBinsFile1);
        response.setFile2Fq(frequencyBinsFile2);
        return response;
    }
}
