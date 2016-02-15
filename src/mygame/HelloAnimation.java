package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import java.util.ArrayList;
import java.util.Random;

/**
 * Sample 7 - how to load an OgreXML model and play an animation, using
 * channels, a controller, and an AnimEventListener.
 */
public class HelloAnimation extends SimpleApplication {

    Node player;
    Oto oto;
    WorldSphere worldSphere;
    Obstacle obstacle;
    Spatial sky;
    Random random = new Random();
    static final int NUM_OBSTACLES = 100;
    Obstacle obstacleHolder[] = new Obstacle[NUM_OBSTACLES];
    private AnimChannel channel;
    private AnimControl control;
    
    
    float xPosition;
    AudioNode audioNode;
    int count = 0;
    
    public static void main(String[] args) {
        HelloAnimation app = new HelloAnimation();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);

        // camera settings
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(80f);
        cam.setLocation(new Vector3f(0f, 13f, 15f));
        cam.lookAt(new Vector3f(0, 5f, 0), Vector3f.UNIT_Y);

        /**
         * Add a light source so we can see the model
         */
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);


        // create the worldsphere for us to run on.
        worldSphere = new WorldSphere(this);

        // add the obstacles
        addObstacles();

        // create oto and set his ground speed.
        oto = new Oto(this, obstacleHolder);
        oto.setGroundSpeed(1.5f);
        initPostProcessing();
        initAudio();
    }

    /* should pass this in to oto controller so we can consistently 
     * update the postion of the obstacles */
    public void addObstacles() {
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            obstacle = new Obstacle(this);
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

    private void initPostProcessing() {
        sky = SkyFactory.createSky(assetManager, "Textures/sky.JPG", true);
        rootNode.attachChild(sky);
    }

    @Override
    public void simpleUpdate(float tpf) {
        sky.rotate(tpf / 6, 0, 0);
    }

    public void initAudio() {
        audioNode = new AudioNode(assetManager, "Sounds/kissfromarose.ogg", false);
        audioNode.setPositional(false);
        audioNode.setLooping(true);
        audioNode.setVolume(4f);
        rootNode.attachChild(audioNode);
        audioNode.play();

    }
}