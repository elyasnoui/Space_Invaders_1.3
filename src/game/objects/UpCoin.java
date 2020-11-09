package game.objects;

import city.cs.engine.*;
import game.setup.Pickup;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class UpCoin extends Pickup {
    public UpCoin(World w) {
        super(w);
        Shape s = new PolygonShape(-0.39f,1.11f, 0.36f,1.08f, 1.05f,0.39f, 1.05f,-0.34f, 0.38f,-1.04f, -0.39f,-1.03f, -0.98f,-0.34f, -0.95f,0.39f);
        Fixture f = new SolidFixture(this, s);
        addImage(new BodyImage("data/player/coin.gif", 3.5f));
    }
}
