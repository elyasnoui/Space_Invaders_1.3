package game.setup;

import city.cs.engine.*;
import game.objects.*;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public abstract class Pickup extends DynamicBody implements CollisionListener {

    public Pickup(World w) {
        super(w);
        addCollisionListener(this);

        //Based of random number, spawn in any of these 4 positions
        int rand = (int)(Math.random() * 4);
        switch (rand) {
            case 0:
                setPosition(new Vec2(-12,23));
                break;
            case 1:
                setPosition(new Vec2(-4,23));
                break;
            case 2:
                setPosition(new Vec2(4,23));
                break;
            case 3:
                setPosition(new Vec2(12,23));
                break;
        } setLinearVelocity(new Vec2(0f, -5f));
    }

    @Override
    public void collide(CollisionEvent e) {
        destroy();
        if (e.getReportingBody() instanceof UpCoin && e.getOtherBody() instanceof Player) {
            if (!LevelManager.isIsSfxMuted())
                new Audio("data/audio/coinPickup.wav", "play", "sfx");

            Player.setCoins(Player.getCoins() + 1);
            System.out.println("Coin Collected! Coins: "+ Player.getCoins());
        }
        else if (e.getReportingBody() instanceof UpScore && e.getOtherBody() instanceof Player) {
            if (!LevelManager.isIsSfxMuted())
                new Audio("data/audio/starPickup.wav", "play", "sfx");

            Player.setScore(Player.getScore() + 1000);
        }
        else if (e.getOtherBody() instanceof PlayerBullet) ((PlayerBullet) e.getOtherBody()).Destruct();
        else if (e.getOtherBody() instanceof EnemyBullet) ((EnemyBullet) e.getOtherBody()).Destruct();
        else if (e.getOtherBody() instanceof BossBullet) ((BossBullet) e.getOtherBody()).Destruct();
    }
}
