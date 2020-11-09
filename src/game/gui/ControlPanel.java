package game.gui;

import game.objects.EnemyBoss;
import game.objects.Player;
import game.setup.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class ControlPanel {
    private Shop shop;
    private JPanel mainPanel;
    private JButton pauseButton;
    private static boolean paused = false;
    private JButton restartButton;
    private JButton exitButton;
    private JComboBox levelSelect;
    private JProgressBar bossHealth;
    private JLabel boss;
    private JButton mainMenu;
    private String curSelect = "Level 1";

    /**
     * Control panel is the controls displayed on the top of the frame.
     * <p>
     *     Use of Control Panel will be during the levels.
     *     </br>
     *     The main menu will not display a Control Panel.
     * </p>
     * @param game Takes {@link Game} as parameter.
     */
    public ControlPanel(Game game) {
        pauseButton.setFocusable(false);
        restartButton.setFocusable(false);
        exitButton.setFocusable(false);
        levelSelect.setFocusable(false);
        bossHealth.setFocusable(false);
        pauseButton.addActionListener(new ActionListener() {
            //Action listener for Pause button

            /**
             * This method is automatically run when the user clicks the Pause button on the Control Panel.
             * <p>
             *     This will cause the game to either pause or resume, depending on the game's state before it was pressed.
             *     </br>
             *     Methods activated by this button are {@link LevelManager#PauseLevel(int)} and {@link LevelManager#ResumeLevel()}.
             * </p>
             * @param e Generated on-click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //Pause level
                paused = !paused;
                LevelManager w = ((LevelManager) game.getUserView().getWorld());
                if (paused) game.getWorld().PauseLevel(1);
                //Resume level
                else game.getWorld().ResumeLevel();
            }
        });
        restartButton.addActionListener(new ActionListener() {
            //Action listener for Restart button

            /**
             * This method is automatically run when the user clicks the Restart button on the Control Panel.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //Restart the game from Level 1
                game.getWorld().PauseLevel(0);
                int warning = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (
                        null, "Any unsaved progress will be lost, are you sure you want to quit?","Warning", warning);
                if (result == JOptionPane.YES_OPTION) game.NextLevel("restart");
                //Continue playing if User presses No
                else game.getWorld().ResumeLevel();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            //Action listener for Exit button
            @Override
            public void actionPerformed(ActionEvent e) {
                //Close the application
                game.getWorld().PauseLevel(0);
                int warning = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (
                        null, "Any unsaved progress will be lost, are you sure you want to quit?","Warning", warning);
                if (result == JOptionPane.YES_OPTION) System.exit(0);
                //Continue playing if User presses No
                else game.getWorld().ResumeLevel();
            }
        });
        levelSelect.addActionListener(new ActionListener() {
            //Action listener for Dropdown menu
            @Override
            public void actionPerformed(ActionEvent e) {
                //Select to jump between levels
                if (!((String) Objects.requireNonNull(levelSelect.getSelectedItem())).equals(curSelect))
                    switch ((String) Objects.requireNonNull(levelSelect.getSelectedItem())) {
                        case "Level 1":
                            game.Jump(1);
                            break;
                        case "Level 2":
                            game.Jump(2);
                            break;
                        case "Level 3":
                            game.Jump(3);
                            break;
                        case "Level 4":
                            game.Jump(4);
                            break;
                    } curSelect = (String) Objects.requireNonNull(levelSelect.getSelectedItem());
                }
        });
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Return to the main menu
                game.getWorld().PauseLevel(0);
                int warning = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (
                        null, "Any unsaved progress will be lost, are you sure you want to quit?","Warning", warning);
                if (result == JOptionPane.YES_OPTION) game.DisplayMenu();
                    //Continue playing if User presses No
                else game.getWorld().ResumeLevel();
            }
        });
    }

    /**
     * Display the boss's health in Control Panel.
     * <p>
     *     This method will be run when {@link EnemyBoss } is instantiated.
     * </p>
     */
    public void BossDisplay() {
        boss.setForeground(Color.decode("#12F116"));
        bossHealth.setValue(25);
    }

    /**
     * Removes the boss's health from Control Panel.
     * <p>
     *     This method will be run when {@link EnemyBoss#Destruct()} is run.
     *     </br>
     *     Another use is when the level is restarted.
     * </p>
     */
    public void ResetDisplay() {
        boss.setForeground(Color.decode("#000000"));
        bossHealth.setValue(0);
    }

    /**
     * Returns the main JPanel holding all components.
     * @return
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns the drop-down menu listing all the levels.
     * <p>
     *     This will typically be used when switching levels to change the focus.
     * </p>
     * @return
     */
    public JComboBox getLevelSelect() {
        return levelSelect;
    }

    /**
     * Switch between specific indexes in {@link ControlPanel#levelSelect}.
     * <p>
     *     This will typically be used when switching levels to change the focus.
     * </p>
     * @return
     */
    public void setCurSelect(String curSelect) {
        this.curSelect = curSelect;
    }

    /**
     * Returns the boss's health value in Control Panel.
     * <p>
     *     Typical use of this is to reflect damage inflicted on {@link EnemyBoss} by {@link Player}.
     * </p>
     * @return
     */
    public JProgressBar getBossHealth() {
        return bossHealth;
    }

    /**
     * Returns whether the game is paused or not.
     * <p>
     *     Typical use of this is by all other classes to determine if the game is paused.
     * </p>
     * @return Boolean value
     */
    public static boolean isPaused() {
        return paused;
    }

    /**
     * Set the game to paused or un-paused.
     * <p>
     *     Typical use of this is by all other classes to relay if game is paused or un-paused.
     * </p>
     * @param paused Set true/false.
     */
    public static void setPaused(boolean paused) {
        ControlPanel.paused = paused;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
