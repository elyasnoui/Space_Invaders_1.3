package game.objects;

import city.cs.engine.*;
import game.setup.Audio;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class PlayerBullet extends DynamicBody {
    private Player p;
    public PlayerBullet(World w, Player p) {
        super(w);
        this.p = p;

        //Play laser sound effect
        if (!LevelManager.isIsSfxMuted()) {
            Audio laser = new Audio("data/audio/playerLaser.wav", "play", "sfx");
        }

        Shape s = new CircleShape(0.4f);
        Fixture sf = new SolidFixture(this, s);
        addImage(new BodyImage("data/player/bullet1.png"));
        setPosition(new Vec2(p.getPosition().x, p.getPosition().y + 2));
        setLinearVelocity(new Vec2(0f, Player.getBulletSpeed()));
    }

    public void Destruct() {
        p.setShooting(false);
        destroy();
    }
}