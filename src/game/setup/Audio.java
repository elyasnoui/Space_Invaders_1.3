package game.setup;

import city.cs.engine.SoundClip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Audio {
    private SoundClip audio;
    private double volume = 1;
    public Audio(String fileName, String loopOrPlay, String type) {
        if (type.equals("music")) volume = LevelManager.getMusicVolume();
        else if (type.equals("sfx")) volume = LevelManager.getSfxVolume();

        //Bug fix
        if (volume == 0) volume = 0.000001;

        try {
            audio = new SoundClip(fileName);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException io) {
            System.out.println(io);
        }

        //Loop audio
        if (loopOrPlay.equals("loop")) {
            assert audio != null;
            audio.loop();
        }

        //Play audio once
        else if (loopOrPlay.equals("play")) {
            assert audio != null;
            audio.play();
        }

        //Play audio with set volume
        try {
            assert audio != null;
            audio.setVolume(volume);
        } catch (IllegalArgumentException ignored) {}
    }

    public SoundClip getAudio() {
        return audio;
    }
}
