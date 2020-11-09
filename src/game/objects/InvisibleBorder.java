package game.objects;

import city.cs.engine.*;
import game.setup.Pickup;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
//Borders to destroy the bullets when they leave the main scene
public class InvisibleBorder extends Sensor implements SensorListener {
    public InvisibleBorder(Body b, Shape s) {
        super(b,s);
        addSensorListener(this);
    }

    @Override
    public void beginContact(SensorEvent e) {
        if (e.getContactBody() instanceof PlayerBullet) ((PlayerBullet) e.getContactBody()).Destruct();
        if (e.getContactBody() instanceof EnemyBullet) ((EnemyBullet) e.getContactBody()).Destruct();
        if (e.getContactBody() instanceof BossBullet) ((BossBullet) e.getContactBody()).Destruct();

        //Allow pickups to pass through the top border but not the bottom one
        if (getBody().getName().equals("Bottom") && e.getContactBody() instanceof Pickup) e.getContactBody().destroy();
    }

    @Override
    public void endContact(SensorEvent sensorEvent) {

    }
}
