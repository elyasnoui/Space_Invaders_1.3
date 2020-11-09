package game.setup;

import city.cs.engine.*;
import game.gui.*;
import game.objects.Player;

import javax.swing.*;
import java.awt.*;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Game {
    public static JFrame frame = new JFrame("Space Invaders");
    private ImageIcon appIcon = new ImageIcon("data/enemies/enemy1E.png");
    private UserView userView;
    private int level = 1;
    private LevelManager world;
    private Controller controller;
    private int lastScore = 0;
    private int lastCoin = 0;
    private int lastLives = 3;
    private int lastSpeed = 15;
    private ControlPanel controlPanel;
    private Shop shop;
    private Options options;
    private MainMenu menu;
    private boolean boot = false;
    public Game() {

        //Load menu
        menu = new MainMenu(this);
        frame.add(menu.getMainPanel(), BorderLayout.CENTER);

        //Initialise JPanels for later
        controlPanel = new ControlPanel(this);
        frame.add(controlPanel.getMainPanel(), BorderLayout.NORTH);

        shop = new Shop(this);
        frame.add(shop.getMainPanel(), BorderLayout.EAST);

        options = new Options(this);
        frame.add(options.getMainPanel(), BorderLayout.WEST);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setIconImage(appIcon.getImage());

        //JFrame debug = new DebugViewer(world, 900, 900);
    }

    public void Boot() {
        world = new Level1(this);
        world.SecondsElapsed();
        world.Populate(this);
        userView = new MyView(world, 850, 850, this);
        userView.setBackground(Color.black);
        world.start();

        controller = new Controller(world.getPlayer());
        frame.addKeyListener(controller);
        controller.adaptStepListener(world);
    }

    public void DisplayMenu() {
        //Display the menu JPanel and hide all others
        world.stop();
        world.PauseMusic();
        world.getTask().cancel();
        shop.getMainPanel().setVisible(false);
        frame.remove(userView);
        frame.removeKeyListener(controller);
        controlPanel.getMainPanel().setVisible(false);
        options.getMainPanel().setVisible(false);
        frame.add(menu.getMainPanel(), BorderLayout.CENTER);
        menu.getMainPanel().setVisible(true);
        frame.pack();
    }

    public void HideMenu() {
        //Hide the menu and make other JPanels visible as well as start the game
        menu.getMainPanel().setVisible(false);
        controlPanel.getMainPanel().setVisible(true);
        controlPanel.getLevelSelect().setSelectedIndex(0);
        options.getMainPanel().setVisible(true);
        frame.add(userView);
        frame.pack();
    }

    //This method either restarts the level or moves on to the next one
    public void NextLevel(String s) {
        world.stop();
        world.PauseMusic();
        world.getTask().cancel();
        //Restarts cause player to retain their stats when they started the level
        if (s == "restart") {
            Player.setScore(lastScore);
            Player.setCoins(lastCoin);
            Player.setLives(lastLives);
            Player.setBulletSpeed(lastSpeed);
            getControlPanel().ResetDisplay();
        //Going to the next level saves the current stats in case the player restarts the next level
        } else if (s == "next") {
            level++;
            lastScore = Player.getScore();
            lastCoin = Player.getCoins();
            lastLives = Player.getLives();
            lastSpeed = Player.getBulletSpeed();
        }

        //Start respective level world
        if (level == 1) world = new Level1(this);
        else if (level == 2) world = new Level2(this);
        else if (level == 3) world = new Level3(this);
        else if (level == 4) world = new Level4(this);

        world.SecondsElapsed();
        world.Populate(this);
        controller.setPlayer(world.getPlayer());
        controller.adaptStepListener(world);
        userView.setWorld(world);
        world.start();

        JFrame debug = new DebugViewer(world, 900, 900);
    }

    //When play is pressed again in Main Menu after already booting the game once
    public void RestartGame() {
        //Stop previous world
        world.stop();
        world.PauseMusic();
        world.getTask().cancel();

        //Reset player stats
        Player.setLives(3);
        Player.setScore(0);
        Player.setCoins(0);
        Player.setBulletSpeed(15);

        //Resetting audio
        LevelManager.setIsMusicMuted(false);
        LevelManager.setIsSfxMuted(false);
        LevelManager.setMusicVolume(1);
        LevelManager.setSfxVolume(1);
        options.getMuteCheckBox().setSelected(false);
        options.getSfxCheckBox().setSelected(false);
        options.getMusicSlider().setValue(100);
        options.getMusicVolume().setText(" Volume: "+100+"%");
        options.getSfxSlider().setValue(100);
        options.getSfxVolume().setText(" Volume: "+100+"%");

        //Start new world
        level = 0;
        frame.addKeyListener(controller);
        controlPanel.ResetDisplay();
        controlPanel.setCurSelect("Level 1");
        shop.getBulletUpgrades().setValue(Player.getBulletSpeed() - 15);
        NextLevel("next");
    }

    //Allows user to skip to any level
    public void Jump(int iLevel) {
        world.PauseMusic();
        getControlPanel().ResetDisplay();
        level = iLevel - 1;
        NextLevel("next");
    }

    public int getLevel() {
        return level;
    }

    public void setWorld(LevelManager world) {
        this.world = world;
    }

    public LevelManager getWorld() {
        return world;
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }

    public UserView getUserView() {
        return userView;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public Shop getShop() {
        return shop;
    }

    public Options getOptions() {
        return options;
    }

    public MainMenu getMenu() {
        return menu;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public void Pause() {
        world.stop();
    }

    public void Resume() {
        world.start();
    }

    public static void main(String[] args) {
        new Game();
    }

    public boolean isBoot() {
        return boot;
    }

    public void setBoot(boolean boot) {
        this.boot = boot;
    }
}