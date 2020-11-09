package game.objects;

import city.cs.engine.*;
import game.setup.Audio;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Portal extends DynamicBody implements StepListener {
    private final Shape shape = new CircleShape(0.5f);
    private final Fixture fixture = new SolidFixture(this, shape);
    private boolean spawning = false;
    public Portal(World w, int level) {
        super(w);
        w.addStepListener(this);
        setPosition(new Vec2(0, 23));

        if (level < 3) addImage(new BodyImage("data/environment/portal1.gif", 4));
        else if (level == 3) addImage(new BodyImage("data/environment/portal2.gif", 4));

        Spawn();
    }

    public void Spawn() {
        spawning = true;
        setLinearVelocity(new Vec2(0, -5));
    }

    //Method is for taking player to the next level
    public void Teleport(Player player) {
        if (!LevelManager.isIsSfxMuted())
            new Audio("data/audio/portal.wav", "play", "sfx");

        player.setLinearVelocity(new Vec2(0, 0));
        LevelManager.getGame().NextLevel("next");
    }

    public boolean isSpawning() {
        return spawning;
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {
        if (spawning && getPosition().y < 15) {
            setLinearVelocity(new Vec2(0, 0));
            spawning = false;
        }
    }
}
