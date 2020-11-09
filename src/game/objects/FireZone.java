package game.objects;

import city.cs.engine.*;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class FireZone extends Sensor implements SensorListener {
    private int xPos;
    private int iSize;
    private int level;
    public FireZone (int xPos, Body b, Shape s, int level) {
        super(b,s);
        this.xPos = xPos;
        this.level = level;
        addSensorListener(this);
    }

    @Override
    public boolean contains(Vec2 p) {
        return super.contains(p);
    }

    //This method is to get enemies to shoot at Player when in range
    @Override
    public void beginContact(SensorEvent e) {
        //Different levels have different amount of enemy rows in 2d array
        if (level < 4) iSize = 5;
        else if (level == 4) iSize = 3;
        if (e.getContactBody() instanceof Player && !((Player) e.getContactBody()).isSpawning()) {
            if (!((LevelManager) e.getContactBody().getWorld()).isBossInGame()) {
                switch (xPos) {
                    case -16:
                        //1st column of enemies
                        for (int i = 0; i < iSize; i++)
                            if (((Player) e.getContactBody()).getEnemiesList()[i][0] != null) {
                                ((Player) e.getContactBody()).getEnemiesList()[i][0].Shoot();
                            }
                        break;
                    case -8:
                        //2nd column of enemies
                        for (int i = 0; i < iSize; i++)
                            if (((Player) e.getContactBody()).getEnemiesList()[i][1] != null)
                                ((Player) e.getContactBody()).getEnemiesList()[i][1].Shoot();
                        break;
                    case 0:
                        //3rd column of enemies
                        for (int i = 0; i < iSize; i++)
                            if (((Player) e.getContactBody()).getEnemiesList()[i][2] != null)
                                ((Player) e.getContactBody()).getEnemiesList()[i][2].Shoot();
                        break;
                    case 8:
                        //4th column of enemies
                        for (int i = 0; i < iSize; i++)
                            if (((Player) e.getContactBody()).getEnemiesList()[i][3] != null)
                                ((Player) e.getContactBody()).getEnemiesList()[i][3].Shoot();
                        break;
                    case 16:
                        //5th column of enemies
                        for (int i = 0; i < iSize; i++)
                            if (((Player) e.getContactBody()).getEnemiesList()[i][4] != null)
                                ((Player) e.getContactBody()).getEnemiesList()[i][4].Shoot();
                        break;
                }
            }
        }
    }

    @Override
    public void endContact(SensorEvent sensorEvent) {

    }
}
