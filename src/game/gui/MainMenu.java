package game.gui;

import game.setup.Controller;
import game.setup.Game;
import game.setup.GameSave;
import game.setup.Level1;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class MainMenu {
    private Game game;
    private JPanel mainPanel;
    private JButton playButton;
    private JButton scoresButton;
    private JButton quitButton;
    private JPanel menuPanel;
    private JPanel scoresPanel;
    private JLabel no1;
    private JLabel no2;
    private JLabel no3;
    private JLabel no4;
    private JLabel no5;
    private JLabel no6;
    private JLabel no7;
    private JLabel no8;
    private JLabel no9;
    private JLabel no10;
    private JButton backButton;
    private JLabel art;
    private GameSave[] saves;

    /**
     * The constructor is used to instantiate a Main Menu JPanel with all its components.
     * <p>
     *     This will be run on startup as it's the first thing the user will see.
     * </p>
     * @param game {@link Game}
     */
    public MainMenu(Game game) {
        this.game = game;
        mainPanel.setPreferredSize(new Dimension(850, 850));
        art.setIcon(new ImageIcon("data/gui/art.png"));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Start the game
                if (!game.isBoot()) {
                    JOptionPane.showMessageDialog(
                            playButton,"If you are experiencing performance issues, mute SFX for better stability.");
                    game.Boot();
                    game.setBoot(true);
                }
                else game.RestartGame();

                game.HideMenu();
            }
        });
        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(false);
                scoresPanel.setVisible(true);

                //Read 'savedata' directory for saves and store into array for sorting
                File dir = new File("savedata");
                File [] files = dir.listFiles();
                assert files != null;
                saves = new GameSave[files.length];
                //Loop through savedata directory for .txt files.
                for (int i = 0; i < saves.length; i++){
                    if (files[i].isFile() && files[i].getName().endsWith(".txt")) {
                        //Read .txt files and disregard invalid files.
                        try {
                            ObjectInputStream in = new ObjectInputStream(new FileInputStream(files[i]));

                            //Store into array for sorting
                            saves[i] = (GameSave) in.readObject();
                        } catch (IOException | ClassNotFoundException ignored) {}
                    }
                }

                //Update high score fields by sorting them according to score.
                for (int i=1; i<=10; i++) {
                    int index = -1;
                    int temp = 0;
                    switch (i) {
                        //First Place
                        case 1:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no1.setText("1: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Second Place
                        case 2:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no2.setText("2: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Third Place
                        case 3:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no3.setText("3: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Forth Place
                        case 4:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no4.setText("4: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Fifth Place
                        case 5:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no5.setText("5: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Sixth Place
                        case 6:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no6.setText("6: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Seventh Place
                        case 7:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no7.setText("7: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Eighth Place
                        case 8:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no8.setText("8: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Ninth Place
                        case 9:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no9.setText("9: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                        //Tenth Place
                        case 10:
                            for (int j=0; j<saves.length; j++) {
                                if (saves[j] != null && saves[j].getScore() > temp) {
                                    temp = saves[j].getScore();
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                no10.setText("10: "+saves[index].getScore()+" by "+files[index].getName().substring(0, files[index].getName().length()-4));
                                saves[index] = null;
                            }
                            break;
                    }
                }
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(true);
                scoresPanel.setVisible(false);
            }
        });
    }

    /**
     * This is used to display or hide {@link MainMenu}, using {@link MainMenu#mainPanel}'s .setVisible method.
     * @return
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
