package org.sound.dto;

import lombok.Data;
import org.sound.service.impl.PitchDetectorServiceImpl;

import java.util.List;

@Data
public class SoundSpectreDto {

    private List<PitchDetectorServiceImpl.FrequencyBin> file1Fq;
    private List<PitchDetectorServiceImpl.FrequencyBin> file2Fq;
}
