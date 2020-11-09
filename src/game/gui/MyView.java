package game.gui;


import city.cs.engine.UserView;
import city.cs.engine.World;
import game.objects.Player;
import game.setup.Game;
import game.setup.LevelManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class MyView extends UserView {
    private Game game;
    private Image background;

    //UI Graphics
    private final Image player = new ImageIcon("data/gui/player.png").getImage();
    private final Font fontUI = new Font("OCR A Extended", Font.BOLD, 20);
    private final Font fontScreen = new Font("OCR A Extended", Font.BOLD, 70);

    /**
     * This class is to override {@link UserView} to access
     * {@link MyView#paintBackground(Graphics2D)} and {@link MyView#paintForeground(Graphics2D)}.
     * <p>
     *     Typical use of this is to display a background image for each level.
     *     </br>
     *     Another use is to display HUD containing {@link Player} stats.
     * </p>
     * @param world
     * @param w
     * @param h
     * @param game
     */
    public MyView(World world, int w, int h, Game game) {
        //Creates a UserView for the World
        super(world, w, h);
        this.game = game;
    }

    /**
     * Display a background image for {@link UserView}.
     * <p>
     *     Typical use is to display different images for different levels.
     * </p>
     * @param g {@link Graphics2D}
     */
    @Override
    protected void paintBackground(Graphics2D g) {
        //This method is to paint a background into our UserView
        super.paintBackground(g);

        //Switch background depending on level
        switch (game.getLevel()) {
            case 1:
                background = new ImageIcon("data/environment/background1.gif").getImage();
                break;
            case 2:
                background = new ImageIcon("data/environment/background2.gif").getImage();
                break;
            case 3:
                background = new ImageIcon("data/environment/background3.gif").getImage();
                break;
            case 4:
                background = new ImageIcon("data/environment/background4.gif").getImage();
        }
        g.drawImage(background,0,0 ,null);
    }

    /**
     * Display HUD stats for {@link Player}.
     * <p>
     *     This will also be used to display a message when the game is complete on {@link UserView}.
     * </p>
     * @param g {@link Graphics2D}
     */
    @Override
    protected void paintForeground(Graphics2D g) {
        //This method is to paint HUD graphics into our UserView
        super.paintForeground(g);

        if (game.getLevel() != 0) {
            //Displaying UI stats on the top of the screen
            g.setColor(Color.GREEN);
            g.setFont(fontUI);
            g.drawString("Score: " + Player.getScore(), 75, 20);
            g.drawString("Coins: " + Player.getCoins(), 255, 20);
            //g.drawString("Lives: " + Player.getLives(), 435, 20);

            //Depending on amount of lives, switch between 3 states
            g.drawString("Lives: ", 395, 20);
            switch (Player.getLives()) {
                case 1:
                    g.drawImage(player, 485, 5, null);
                    break;
                case 2:
                    g.drawImage(player, 485, 5, null);
                    g.drawImage(player, 515, 5, null);
                    break;
                case 3:
                    g.drawImage(player, 485, 5, null);
                    g.drawImage(player, 515, 5, null);
                    g.drawImage(player, 545, 5, null);
                    break;
            }

            //Set time left text to red
            g.setColor(Color.RED);
            g.drawString("Time Left: " + ((LevelManager) getWorld()).getTimeRemaining(), 610, 20);

            //Display game over message at the end of the game
            if (game.getLevel() == 4 && ((LevelManager) getWorld()).isLevelCompleted()) {
                g.setFont(fontScreen);
                g.setColor(Color.GREEN);
                g.drawString("Game Complete!", 125, 200);
            }
        }
    }
}
