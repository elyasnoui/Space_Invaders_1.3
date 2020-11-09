package game.objects;

import city.cs.engine.*;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
//Requires its own class because GhostlyFixture is required to allow bullets to pass through while exploding
public class EnemyExplode extends GhostlyFixture implements StepListener {
    private int time;
    public EnemyExplode(Walker enemy, int size) {
        super(new StaticBody(enemy.getWorld()), new BoxShape(0, 0, new Vec2(0, 0)));

        getBody().getWorld().addStepListener(this);
        getBody().addImage(new BodyImage("data/environment/explosion.gif", size));
        getBody().setPosition(new Vec2(enemy.getPosition().x, enemy.getPosition().y));

        //Play animation for only 2 seconds
        time = ((LevelManager) getBody().getWorld()).getSecondsElapsed() + 2;
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        if (time <= ((LevelManager) getBody().getWorld()).getSecondsElapsed()) {
            //Destroy and remove steplistener to help performance
            getBody().getWorld().removeStepListener(this);
            getBody().destroy();
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {
    }
}
