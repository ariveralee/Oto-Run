/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
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
    BitmapText scoreText; 
     BitmapText       waitText;
     BitmapText pauseText;
    Oto oto;
    WorldSphere worldSphere;
    Obstacle obstacle;
    Spatial sky;
    float gameTime;
    float waitTime;
    float xPosition;
    Random random = new Random();
    boolean waitTextVisible = false;
    

    protected Game() {
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        helloAnimation =  (HelloAnimation) app;
        asm = stateManager;
        
        // Initialize the world, obstacles and oto.
         worldSphere = new WorldSphere(helloAnimation);
        addObstacles();
        oto = new Oto(helloAnimation, obstacleHolder);
        helloAnimation.getRootNode().attachChild(oto.otoNode);
        
        gameTime = 0;
        state = WAIT;
        waitTime = INITIALWAITTIME;
        
        // stuff for pause menu
        /* stuff for paused */
    BitmapFont bmf = helloAnimation.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        scoreText = new BitmapText(bmf);
        scoreText.setSize(bmf.getCharSet().getRenderedSize() * 2);
        scoreText.setColor(ColorRGBA.Black);
        scoreText.setText("");
        scoreText.setLocalTranslation(20, 20, 0f);
        helloAnimation.getGuiNode().attachChild(scoreText);
        waitText = new BitmapText(bmf);
        waitText.setSize(bmf.getCharSet().getRenderedSize() * 10);
        waitText.setColor(ColorRGBA.White);
        waitText.setText("");
        AppSettings s = helloAnimation.getSettings();
        float lineY = s.getHeight() / 2;
        float lineX = (s.getWidth() - waitText.getLineWidth()) / 2;
        waitText.setLocalTranslation(lineX, lineY, 0f);
        pauseText = new BitmapText(bmf);
        pauseText.setSize(bmf.getCharSet().getRenderedSize() * 10);
        pauseText.setColor(ColorRGBA.White);
        pauseText.setText("PAUSED");
        lineY = s.getHeight() / 2;
        lineX = (s.getWidth() - pauseText.getLineWidth()) / 2;
        pauseText.setLocalTranslation(lineX, lineY, 0f);

        
        // keys
        InputManager inputManager = helloAnimation.getInputManager();
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(this, "Pause", "Quit");

        
        initViews();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (name.equals("Pause")) {
                if (state == PAUSE) {
                    state = stateMemory;
                    helloAnimation.getGuiNode().detachChild(pauseText);
                    
                    
                } else {
                    stateMemory = state;
                    state = PAUSE;
                    helloAnimation.getGuiNode().attachChild(pauseText);
                    
                }
            }
            if (name.equals("Quit")) {
                StartScreen s = new StartScreen();
                asm.detach(this);
                asm.attach(s);
            }
        }
    }
    
    private void initViews() {
        helloAnimation.getFlyByCamera().setEnabled(false);
        helloAnimation.getFlyByCamera().setMoveSpeed(90f);
        helloAnimation.getCamera().setLocation(new Vector3f(0f, 13f, 15f));
        helloAnimation.getCamera().lookAt(new Vector3f(0, 5f, 0), Vector3f.UNIT_Y);
        sky = SkyFactory.createSky(helloAnimation.getAssetManager(), "Textures/sky.JPG", true);
        helloAnimation.getRootNode().attachChild(sky);
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
    @Override
    public void update(float tpf) {
        
        
        switch (state) {
            case WAIT:
                if (!waitTextVisible) {
                    waitTextVisible = true;
                    helloAnimation.getGuiNode().attachChild(waitText);
                }
                waitTime -= tpf;
                if (waitTime <= 0f) {
                    state = RUN;
                    if (waitTextVisible) {
                        waitTextVisible = false;
                        helloAnimation.getGuiNode().detachChild(waitText);
                    }
                } else {
                    waitText.setText("" + ((int) waitTime + 1));
                }
                break;
            case RUN:
                worldSphere.spinner.rotate(tpf/4, 0f, 0f);
                 oto.setGroundSpeed(1.5f);
                 sky.rotate(tpf / 6, 0, 0);
                if (oto.health <= 0) {
                    endGame();
                }
                break;
            case PAUSE:
                oto.setGroundSpeed(0f);
                break;
        }
    }
    
    private void endGame() {
        System.exit(0);
    }
}
