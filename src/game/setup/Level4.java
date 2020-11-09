package game.setup;

import city.cs.engine.*;
import game.objects.Enemy;
import game.objects.EnemyBoss;

import java.util.Arrays;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Level4 extends LevelManager {
    private Audio backgroundMusic;
    public Level4(Game game) {
        super(4);
        System.out.println("Level 4");

        backgroundMusic = new Audio("data/audio/level4.wav", "loop", "music");
        if (isIsMusicMuted()) backgroundMusic.getAudio().pause();

        //Instantiate 25 enemies into a 2D array
        setEnemies(new Enemy[3][5]);
        setEnemiesLeft(15);
        float iniY = 30f;
        for (int i=0; i<3; i++) {
            int iniX = -16;
            iniY -= 4;
            for (int j=0; j<5; j++) {
                if (j==0 || j==4) getEnemies()[i][j] = new Enemy(this, getPlayer(), 3.3f, i, j, getEnemies(), iniX, iniY);
                else if (j==1 || j==3) getEnemies()[i][j] = new Enemy(this, getPlayer(), 3.3f, i, j, getEnemies(), iniX, iniY + 3);
                else getEnemies()[i][j] = new Enemy(this, getPlayer(), 3.3f, i, j, getEnemies(), iniX, iniY + 6);
                iniX += 8;
            }
        } getPlayer().setEnemiesList(getEnemies());
    }

    /**
     * Pauses music for Level 4
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
        backgroundMusic.getAudio().setVolume(volume);
    }
}
