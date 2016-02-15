/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import java.util.Random;

/**
 *
 * @author ariveralee
 */
public class Game extends AbstractAppState implements ActionListener {
    static final int NUM_OBSTACLES = 100;
    private final int WAIT = 0;
    private final int RUN = 1;
    private final int PAUSE = 2;
    private int state, stateMemory;
    private final int INITIALWAITTIME = 3; // seconds
    
    Obstacle obstacleHolder[] = new Obstacle[NUM_OBSTACLES];
    HelloAnimation helloAnimation;
    
    AppStateManager asm;
    BitmapText scoreText, waitText, pauseText;
    Oto oto;
    WorldSphere worldSphere;
    Obstacle obstacle;
    float gameTime;
    float waitTime;
    float xPosition;
    Random random = new Random();
    boolean waitTextVisible = false;

    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        helloAnimation =  (HelloAnimation) app;
        asm = stateManager;
        
         worldSphere = new WorldSphere(helloAnimation);
        addObstacles();
        oto = new Oto(helloAnimation, obstacleHolder);
        oto.setGroundSpeed(1.5f);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
  
    public void addObstacles() {
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            obstacle = new Obstacle(helloAnimation);
            obstacleHolder[i] = obstacle;
            // i don't even know why this works
            float angle = FastMath.PI / FastMath.nextRandomFloat() * 360;
            float yPosition = 200f * FastMath.cos(angle);
            float zPosition = 200f * FastMath.sin(angle);
            // So we can populate the left and right sides of our bounded area
            if (i % 2 == 0) {
                xPosition = 4.5f * random.nextFloat();
            } else {
                xPosition = 4.5f * -random.nextFloat();
            }
            obstacleHolder[i].obstacleGeom.setLocalTranslation(xPosition, yPosition, zPosition);
            worldSphere.spinner.attachChild(obstacleHolder[i].obstacleGeom);
//       worldSphere.spinner.rotate(100f, 0, 0);
        }
}
}
