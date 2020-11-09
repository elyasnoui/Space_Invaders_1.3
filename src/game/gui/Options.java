package game.gui;

import game.objects.Enemy;
import game.objects.EnemyBoss;
import game.objects.Player;
import game.objects.Portal;
import game.setup.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Options {
    private JPanel mainPanel;
    private JLabel keyControls;
    private JCheckBox muteCheckBox;
    private JSlider musicSlider;
    private JLabel musicVolume;
    private JCheckBox sfxCheckBox;
    private JSlider sfxSlider;
    private JLabel sfxVolume;
    private JButton save;
    private JButton load;
    private String name;
    public Options(Game game) {
        keyControls.setIcon(new ImageIcon("data/gui/keyControls.png"));
        muteCheckBox.setIcon(new ImageIcon("data/gui/unmute1.png"));
        muteCheckBox.setSelectedIcon(new ImageIcon("data/gui/mute1.png"));
        sfxCheckBox.setIcon(new ImageIcon("data/gui/unmute1.png"));
        sfxCheckBox.setSelectedIcon(new ImageIcon("data/gui/mute1.png"));
        muteCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (muteCheckBox.isSelected()) {
                    game.getWorld().PauseMusic();
                    LevelManager.setIsMusicMuted(true);
                } else {
                    if (!ControlPanel.isPaused()) game.getWorld().PlayMusic();
                    LevelManager.setIsMusicMuted(false);
                }
            }
        });
        musicSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                musicVolume.setText(" Volume: "+musicSlider.getValue()+"%");

                double volume = (double) musicSlider.getValue() / 100;
                game.getWorld().ChangeMusicVolume(volume);
                LevelManager.setMusicVolume(volume);
            }
        });
        sfxCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sfxCheckBox.isSelected()) LevelManager.setIsSfxMuted(true);
                else LevelManager.setIsSfxMuted(false);
            }
        });
        sfxSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sfxVolume.setText(" Volume: "+sfxSlider.getValue()+"%");

                double volume = (double) sfxSlider.getValue() / 100;
                LevelManager.setSfxVolume(volume);
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Save gamesave object into .txt
                game.getWorld().PauseLevel(0);
                JFileChooser jfc = new JFileChooser(new File("savedata"));
                jfc.setDialogTitle("Save File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
                jfc.setFileFilter(filter);
                int result = jfc.showSaveDialog(save);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(jfc.getSelectedFile()+".txt"));
                        GameSave gameSave = new GameSave(
                                game.getLevel(), game.getWorld().isLevelCompleted(), game.getWorld().isBossInGame(), game.getWorld().getPlayer().getPosition(),
                                Player.getLives(), Player.getScore(), Player.getBulletSpeed(), Player.getCoins(), game.getWorld().getTimeRemaining(), LevelManager.isIsMusicMuted(),
                                LevelManager.isIsSfxMuted(), LevelManager.getMusicVolume(), LevelManager.getSfxVolume(), game.getWorld().getEnemies()
                        );
                        out.writeObject(gameSave);
                        JOptionPane.showMessageDialog(save,"Game Saved.");
                    } catch (IOException io) {
                        System.out.println(io);
                        JOptionPane.showMessageDialog(save,"Unable to save.");
                    }
                } else game.getWorld().ResumeLevel();
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Load gamesave object into .txt
                game.getWorld().PauseLevel(0);
                JFileChooser jfc = new JFileChooser(new File("savedata"));
                jfc.setDialogTitle("Open Save File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
                jfc.setFileFilter(filter);
                int result = jfc.showOpenDialog(load);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        //Try to find and read the object from files
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(jfc.getSelectedFile()));
                        GameSave gameSave = (GameSave) in.readObject();

                        //Start world player was in and load player state
                        game.Jump(gameSave.getLevel());
                        game.getWorld().setTimeRemaining(gameSave.getTimeRemaining());
                        game.getWorld().getPlayer().setSpawning(false);
                        game.getWorld().getPlayer().setLinearVelocity(new Vec2(0, 0));
                        game.getWorld().getPlayer().setPosition(gameSave.getPlayerPos());
                        Player.setLives(gameSave.getLives());
                        Player.setScore(gameSave.getScore());
                        Player.setBulletSpeed(gameSave.getBulletSpeed());
                        game.getShop().getBulletUpgrades().setValue(gameSave.getBulletSpeed() - 15);
                        Player.setCoins(gameSave.getCoins());

                        //Update audio settings
                        LevelManager.setIsMusicMuted(gameSave.isMusicMuted());
                        LevelManager.setIsSfxMuted(gameSave.isSfxMuted());
                        LevelManager.setMusicVolume(gameSave.getMusicVolume());
                        LevelManager.setSfxVolume(gameSave.getSfxVolume());
                        muteCheckBox.setSelected(gameSave.isMusicMuted());
                        sfxCheckBox.setSelected(gameSave.isSfxMuted());
                        musicSlider.setValue((int) (gameSave.getMusicVolume() * 100));
                        musicVolume.setText(" Volume: "+musicSlider.getValue()+"%");
                        sfxSlider.setValue((int) (gameSave.getSfxVolume() * 100));
                        sfxVolume.setText(" Volume: "+sfxSlider.getValue()+"%");

                        //Bug fix (audio)
                        if (gameSave.isMusicMuted()) game.getWorld().PauseMusic();
                        else game.getWorld().PlayMusic();

                        if (!gameSave.isLevelCompleted()) {
                            //Bug fix (player not loading in correct position)
                            game.getWorld().getPlayer().setPosition(new Vec2(game.getWorld().getPlayer().getPosition().x, -18));

                            if (gameSave.isBossInGame()) {
                                game.getWorld().setEnemiesSpawning(false);
                                game.getWorld().setEnemiesLeft(0);
                                for (Enemy[] enemies : game.getWorld().getEnemies())
                                    for (Enemy enemy : enemies)
                                        if (enemy != null) enemy.destroy();
                                EnemyBoss.setLives(gameSave.getBossHealth());
                                new EnemyBoss(LevelManager.getGame().getWorld(), LevelManager.getGame().getWorld().getPlayer(), 32);
                                LevelManager.getGame().getControlPanel().BossDisplay();
                                LevelManager.getGame().getControlPanel().getBossHealth().setValue(gameSave.getBossHealth());
                            } else if (gameSave.getLevel() == 4) {
                                for (int i=0; i<3; i++)
                                    for (int j=0; j<5; j++)
                                        if (gameSave.getEnemiesNull()[i][j] == null) {
                                            game.getWorld().getEnemies()[i][j].setLives(gameSave.getEnemiesHealth()[i][j]);
                                            game.getWorld().getEnemies()[i][j].UpdateSprite();
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].setLives(gameSave.getEnemiesHealth()[i][j]);
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].UpdateSprite();
                                        } else if (gameSave.getEnemiesNull()[i][j].equals("destroyed")) {
                                            game.getWorld().getEnemies()[i][j].destroy();
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].destroy();
                                            game.getWorld().getEnemies()[i][j] = null;
                                            game.getWorld().getPlayer().getEnemiesList()[i][j] = null;
                                            game.getWorld().setEnemiesLeft(game.getWorld().getEnemiesLeft() - 1);
                                        }
                            } else if (gameSave.getLevel() < 4)
                                for (int i=0; i<5; i++)
                                    for (int j=0; j<5; j++)
                                        if (gameSave.getEnemiesNull()[i][j] == null) {
                                            game.getWorld().getEnemies()[i][j].setLives(gameSave.getEnemiesHealth()[i][j]);
                                            game.getWorld().getEnemies()[i][j].UpdateSprite();
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].setLives(gameSave.getEnemiesHealth()[i][j]);
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].UpdateSprite();
                                        } else if (gameSave.getEnemiesNull()[i][j].equals("destroyed")) {
                                            game.getWorld().getEnemies()[i][j].destroy();
                                            game.getWorld().getPlayer().getEnemiesList()[i][j].destroy();
                                            game.getWorld().getEnemies()[i][j] = null;
                                            game.getWorld().getPlayer().getEnemiesList()[i][j] = null;
                                            game.getWorld().setEnemiesLeft(game.getWorld().getEnemiesLeft() - 1);
                                        }
                        } else {
                            game.getWorld().setLevelCompleted(true);
                            game.getWorld().setEnemiesSpawning(false);
                            game.getWorld().setEnemiesLeft(0);
                            for (Enemy[] enemies : game.getWorld().getEnemies())
                                for (Enemy enemy : enemies)
                                    if (enemy != null) enemy.destroy();
                            if (gameSave.getLevel() != 4) game.getWorld().setPortal(new Portal(game.getWorld(), gameSave.getLevel()));
                        }
                    } catch (IOException | ClassNotFoundException io) {
                        if (io instanceof InvalidClassException)
                            JOptionPane.showMessageDialog(save,"Save Data is incompatible.");
                        else if (io instanceof FileNotFoundException)
                            JOptionPane.showMessageDialog(save,"Save Data does not exist.");
                    }
                } else game.getWorld().ResumeLevel();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JCheckBox getMuteCheckBox() {
        return muteCheckBox;
    }

    public JCheckBox getSfxCheckBox() {
        return sfxCheckBox;
    }

    public JLabel getMusicVolume() {
        return musicVolume;
    }

    public JLabel getSfxVolume() {
        return sfxVolume;
    }

    public JSlider getMusicSlider() {
        return musicSlider;
    }

    public JSlider getSfxSlider() {
        return sfxSlider;
    }
}
