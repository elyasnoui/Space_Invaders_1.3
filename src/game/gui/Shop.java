package game.gui;

import city.cs.engine.StepListener;
import game.objects.Player;
import game.setup.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Shop {
    private JPanel mainPanel;
    private JSpinner bulletUpgrades;
    private JButton lifeUp;
    private JLabel trolleyIcon;
    private JLabel coinImage;
    private final Icon coin = new ImageIcon("data/gui/coinPanelLeft.gif");
    public Shop(Game game) {
        bulletUpgrades.setFocusable(false);
        bulletUpgrades.setFocusTraversalKeysEnabled(false);
        bulletUpgrades.setRequestFocusEnabled(false);
        ((JSpinner.DefaultEditor) bulletUpgrades.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) bulletUpgrades.getEditor()).getTextField().setEnabled(false);
        ((JSpinner.DefaultEditor) bulletUpgrades.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        ((JSpinner.DefaultEditor) bulletUpgrades.getEditor()).getTextField().setBackground(Color.black);
        bulletUpgrades.setValue(0);
        trolleyIcon.setIcon(new ImageIcon("data/gui/shop.png"));
        bulletUpgrades.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //True initial value is 15, but program will display 0
                if ((int) bulletUpgrades.getValue() < 0) bulletUpgrades.setValue(0);
                //++ Upgrade
                else if ((int) bulletUpgrades.getValue() > Player.getBulletSpeed()-15 && Player.getCoins() >= 2 ) {
                    System.out.println("Bullet upgrade purchased!");
                    Player.setCoins(Player.getCoins() - 2);
                    Player.setBulletSpeed(Player.getBulletSpeed() + 1);
                } //-- Downgrade
                else if ((int) bulletUpgrades.getValue() < Player.getBulletSpeed()-15) {
                    System.out.println("Bullet upgrade sold!");
                    Player.setCoins(Player.getCoins() + 2);
                    Player.setBulletSpeed(Player.getBulletSpeed() - 1);
                } else bulletUpgrades.setValue(Player.getBulletSpeed()-15);
            }
        });
        lifeUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.setCoins(Player.getCoins() - 10);
                Player.setLives(Player.getLives() + 1);

                if (Player.getLives() == 3 || Player.getCoins() < 10) lifeUp.setEnabled(false);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JSpinner getBulletUpgrades() {
        return bulletUpgrades;
    }

    public JButton getLifeUp() {
        return lifeUp;
    }
}
