package org.sound.dto;

import lombok.Data;
import org.sound.service.impl.PitchDetectorServiceImpl;

import java.util.List;

@Data
public class SoundSpectreDto {

    List<PitchDetectorServiceImpl.FrequencyBin> file1Fq;
    List<PitchDetectorServiceImpl.FrequencyBin> file2Fq;
}
