package org.sound.service;

import org.sound.service.impl.PitchDetectorServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PitchDetectorService {

    Double detectFreq(MultipartFile wavFile);

    List<PitchDetectorServiceImpl.FrequencyBin> analyzeSpectrum(MultipartFile wavFile);
}
