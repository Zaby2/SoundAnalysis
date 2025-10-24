package org.sound.service.impl;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.fft.FFT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sound.exception.InternalSoundAnalysisException;
import org.sound.service.PitchDetectorService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PitchDetectorServiceImpl implements PitchDetectorService {

    @Override
    public Double detectFreq(MultipartFile wavFile) {
        File tmpFile;
        try {
            tmpFile = File.createTempFile("audio", ".wav");
            wavFile.transferTo(tmpFile);
        } catch (IOException e) {
            log.error("Ошибка создания временного файла");
            throw new InternalSoundAnalysisException(e.getMessage());
        }

        final double[] frequency = {0.0};

        var dispatcher = AudioDispatcherFactory.fromPipe(tmpFile.getAbsolutePath(),
                44100, 1024, 512);
        PitchDetectionHandler handler = (res, e) -> {
            if (res.getPitch() != 1) {
                frequency[0] = res.getPitch();
            }
        };

        var processor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.YIN,
                44100,
                1024,
                handler
        );

        dispatcher.addAudioProcessor(processor);
        dispatcher.run();

        tmpFile.delete();
        return frequency[0];
    }

    @Override
    public List<FrequencyBin> analyzeSpectrum(MultipartFile wavFile) {
        File tmpFile;
        try {
            tmpFile = File.createTempFile("spectrum", ".wav");
            wavFile.transferTo(tmpFile);
        } catch (IOException e) {
            log.error("Ошибка создания временного файла");
            throw new InternalSoundAnalysisException(e.getMessage());
        }

        var sampleRate = 44100;
        var bufferSize = 1024;
        var overlap = 512;

        var spectrum = new ArrayList<FrequencyBin>();

        var dispatcher = AudioDispatcherFactory.fromPipe(
                tmpFile.getAbsolutePath(), sampleRate, bufferSize, overlap);

        dispatcher.addAudioProcessor(new AudioProcessor() {
            FFT fft = new FFT(bufferSize);
            float[] amplitudes = new float[bufferSize / 2];

            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buffer = audioEvent.getFloatBuffer();

                fft.forwardTransform(buffer);
                fft.modulus(buffer, amplitudes);

                for (var i = 0; i < amplitudes.length; i++) {
                    double freq = i * (sampleRate / (double) bufferSize);
                    double db = 20 * Math.log10(amplitudes[i] + 1e-8);
                    spectrum.add(new FrequencyBin(freq, db));
                }
                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        dispatcher.run();
        tmpFile.delete();
        return spectrum;
    }

    @Data
    @AllArgsConstructor
    public static class FrequencyBin {
        private double frequency;
        private double amplitude;
    }
}

