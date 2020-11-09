package game.setup;

import city.cs.engine.*;
import game.gui.ControlPanel;
import game.objects.*;
import org.jbox2d.common.Vec2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Controller extends KeyAdapter implements StepListener {
    private final int hSpeed = 15;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private Player player;
    private LevelManager w;
    public Controller (Player player) {
        this.player = player;
        w = ((LevelManager) player.getWorld());
    }

    public void setPlayer(Player player) {
        //Bug fix (Player would go up and down after going to next level)
        isMovingUp = false;
        isMovingDown = false;
        this.player = player;
        w = ((LevelManager) player.getWorld());
    }

    public void adaptStepListener(World world) {
        world.addStepListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!player.isSpawning() && !w.isEnemiesSpawning()) {
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                isMovingLeft = true;
                player.startWalking(- hSpeed);
            }
            if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                isMovingRight = true;
                player.startWalking(hSpeed);
            }
        }

        //Allow the player to move vertically when the level is completed
        if (w.isLevelCompleted() && (w.getLevel() == 4 || !w.getPortal().isSpawning())) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                isMovingUp = true;
                player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, 10));
            }
            if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                isMovingDown = true;
                player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, -10));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Stopping movement
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) isMovingLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) isMovingRight = false;

        //Shooting a bullet - only when the level isn't completed yet
        if (!w.isLevelCompleted() && (!w.isEnemiesSpawning()))
            if (!player.isSpawning() && !player.isShooting() && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)) {
                player.setShooting(true);
                new PlayerBullet(player.getWorld(), player);
            }
        if (w.isLevelCompleted()) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) isMovingUp = false;
            if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) isMovingDown = false;
        }

        //Pause/Resume
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
            ControlPanel.setPaused(!ControlPanel.isPaused());
            LevelManager w = ((LevelManager) LevelManager.getGame().getUserView().getWorld());
            if (ControlPanel.isPaused()) w.PauseLevel(1);
            else w.ResumeLevel();
        }

        //DEBUG
        if (e.getKeyCode() == KeyEvent.VK_C) new UpCoin(player.getWorld());
        else if (e.getKeyCode() == KeyEvent.VK_M) LevelManager.getGame().DisplayMenu();
        else if (e.getKeyCode() == KeyEvent.VK_V) new UpScore(player.getWorld());
        else if (e.getKeyCode() == KeyEvent.VK_T) w.setTimeRemaining(w.getTimeRemaining() - 10);
        else if (w.isBossInGame() && e.getKeyCode() == KeyEvent.VK_B) {
            w.getPlayer().getEnemyBoss().Destruct();
            w.setEnemiesSpawning(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_X) {
            w.setEnemiesSpawning(false);
            for (Enemy[] enemies : player.getEnemiesList())
                for (Enemy enemy : enemies)
                    if (enemy != null) {
                        enemy.Destroy();
                        if (w.getLevel() == 4)
                            if (enemy.isShooting()) enemy.getBullet().Destruct();
                    }
        }
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        //Snappy Controls - Horizontal
        if (isMovingLeft && !isMovingRight) {
            player.startWalking(- hSpeed);
            if (isMovingRight) player.startWalking(hSpeed);
        } if (!isMovingLeft && isMovingRight) {
            player.startWalking(hSpeed);
            if (isMovingLeft) player.startWalking(- hSpeed);
        }

        //Snappy Controls - Vertical
        if (isMovingUp && !isMovingDown) {
            player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, 10));
            if (isMovingDown) player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, -10));
        } if (!isMovingUp && isMovingDown) {
            player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, -10));
            if (isMovingUp) player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, 10));
        }

        //Stop player from going out of bounds when game is complete
        if (player.getPosition().x <= -19.75f) player.setPosition(new Vec2(-19.75f, player.getPosition().y));
        else if (player.getPosition().x >= 19.75f) player.setPosition(new Vec2(19.75f, player.getPosition().y));
        if (w.isLevelCompleted()) {
            if (player.getPosition().y <= -20.25f) player.setPosition(new Vec2(player.getPosition().x, -20.25f));
            else if (player.getPosition().y >= 18.5f) player.setPosition(new Vec2(player.getPosition().x, 18.5f));
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        //Player spawning
        if (player.isSpawning()) {
            isMovingLeft = false;
            isMovingRight = false;
            if (player.getPosition().y > -18) {
                player.setLinearVelocity(new Vec2(0, 0));
                player.setPosition(new Vec2(player.getPosition().x, -18));
                player.setSpawning(false);
            }
        }

        //Stopping movement
        if (!isMovingLeft && !isMovingRight) player.startWalking(0);
        if (!isMovingUp && !isMovingDown && w.isLevelCompleted())
            player.setLinearVelocity(new Vec2(player.getLinearVelocity().x, 0));
    }
}
