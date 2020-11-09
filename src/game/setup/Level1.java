package game.setup;

import city.cs.engine.StepEvent;
import game.objects.Enemy;
import game.objects.Portal;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Level1 extends LevelManager {
    private Audio backgroundMusic;
    public Level1(Game g) {
        super(1);
        System.out.println("Level 1");

        //Start a music loop
        backgroundMusic = new Audio("data/audio/level1.wav", "loop", "music");
        if (isIsMusicMuted()) backgroundMusic.getAudio().pause();

        //Instantiate 25 enemies into a 2D array
        setEnemies(new Enemy[5][5]);
        setEnemiesLeft(25);
        float iniY = 40f;
        for (int i=0; i<5; i++) {
            int iniX = -16;
            iniY -= 4;
            for (int j = 0; j < 5; j++) {
                if (i == 0) getEnemies()[i][j] = new Enemy(this, getPlayer(), 1.3f, i, j, getEnemies(), iniX, iniY);
                else if (i == 1) getEnemies()[i][j] = new Enemy(this, getPlayer(), 1.2f, i, j, getEnemies(), iniX, iniY);
                else getEnemies()[i][j] = new Enemy(this, getPlayer(), 1.1f, i, j, getEnemies(), iniX, iniY);
                iniX += 8;
            }
        } getPlayer().setEnemiesList(getEnemies());
    }

    /**
     * Pauses music for Level 1
     * <p>
     *     Overrides method in {@link LevelManager#PauseMusic()}
     * </p>
     */
    public void PauseMusic() {
        backgroundMusic.getAudio().pause();
    }

    public void PlayMusic() {
        backgroundMusic.getAudio().resume();
    }

    public void ChangeMusic(String fileName) {
        backgroundMusic.getAudio().stop();
        backgroundMusic = new Audio(fileName, "loop", "music");

        if (isIsMusicMuted()) PauseMusic();
    }

    public void ChangeMusicVolume(double volume) {
        try {
            backgroundMusic.getAudio().setVolume(volume);
        } catch (IllegalArgumentException ignored) {}
    }
}