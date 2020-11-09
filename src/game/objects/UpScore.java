package game.objects;

import city.cs.engine.*;
import game.setup.Pickup;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class UpScore extends Pickup {
    public UpScore(World w) {
        super(w);
        Shape s = new PolygonShape(0.0f,0.86f, 0.76f,0.24f, 0.62f,-0.85f, -0.68f,-0.87f, -0.85f,0.25f);
        Fixture f = new SolidFixture(this, s);
        addImage(new BodyImage("data/player/star.gif", 2));
    }
}
