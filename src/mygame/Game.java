/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;

/**
 *
 * @author ariveralee
 */
public class Game {
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
    float gameTime;
    float waitTime;
    boolean waitTextVisible = false;
    
    
    
    public void initialize(AppStateManager stateManager, Application app) {
        helloAnimation = (HelloAnimation) app;
        asm = stateManager;
        
        worldSphere
    }
 
}
