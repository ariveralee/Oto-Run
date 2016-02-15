package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import java.util.ArrayList;
import java.util.Random;

/**
 * Sample 7 - how to load an OgreXML model and play an animation, using
 * channels, a controller, and an AnimEventListener.
 */
public class HelloAnimation extends SimpleApplication {

   static Material matRed, matGreen, matBlue, matOrange, matTransparent;
    Spatial sky;
    private AnimChannel channel;
    private AnimControl control;
    AudioNode audioNode;
    
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        settings.setVSync(true);
        HelloAnimation app = new HelloAnimation();
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
    
    public AppSettings getSettings() {
        return (settings);
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        //initCam();
        initLights();
        //initPostProcessing();
        initAudio();
        
        // start the game yo.
        StartScreen s = new StartScreen();
        stateManager.attach(s);
    }
    
    protected static void clearJMonkey(HelloAnimation a) {
        a.guiNode.detachAllChildren();
        a.rootNode.detachAllChildren();
        a.inputManager.clearMappings();
    }

    private void initPostProcessing() {
        sky = SkyFactory.createSky(assetManager, "Textures/sky.JPG", true);
        rootNode.attachChild(sky);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //sky.rotate(tpf / 6, 0, 0);
    }

    public void initAudio() {
        audioNode = new AudioNode(assetManager, "Sounds/kissfromarose.ogg", false);
        audioNode.setPositional(false);
        audioNode.setLooping(true);
        audioNode.setVolume(4f);
        rootNode.attachChild(audioNode);
        audioNode.play();

    }
    
    public void initCam() {
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(80f);
        cam.setLocation(new Vector3f(0f, 13f, 15f));
        cam.lookAt(new Vector3f(0, 5f, 0), Vector3f.UNIT_Y);
    }
    
    public void initLights() {
        // Light source added so we can see the model.
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);
    }
    public void initMaterials() {
        matRed = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        matRed.setColor("Color", ColorRGBA.Orange);
        matRed = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matRed.setBoolean("UseMaterialColors", true);
        matRed.setColor("Ambient", new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
        matRed.setColor("Diffuse", ColorRGBA.Red);
        matRed.setTexture("DiffuseMap", assetManager.loadTexture("Textures/texture1.png"));

        matGreen = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matGreen.setBoolean("UseMaterialColors", true);
        matGreen.setColor("Ambient", new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
        matGreen.setColor("Diffuse", ColorRGBA.Green);
        matGreen.setColor("Specular", ColorRGBA.Orange);
        matGreen.setFloat("Shininess", 2f); // shininess from 1-128
        matGreen.setTexture("DiffuseMap", assetManager.loadTexture("Textures/texture1.png"));

        matBlue = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        matBlue.setColor("Color", ColorRGBA.Orange);
        matBlue = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matBlue.setBoolean("UseMaterialColors", true);
        matBlue.setColor("Ambient", new ColorRGBA(0.6f, 0.6f, 0.6f, 1.0f));
        matBlue.setColor("Diffuse", ColorRGBA.Blue);
        matBlue.setTexture("DiffuseMap", assetManager.loadTexture("Textures/texture1.png"));

        matOrange = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matOrange.setBoolean("UseMaterialColors", true);
        matOrange.setColor("Ambient", new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
        matOrange.setColor("Diffuse", ColorRGBA.Orange);
        matOrange.setColor("Specular", ColorRGBA.Orange);
        matOrange.setFloat("Shininess", 2f); // shininess from 1-128
        matOrange.setTexture("DiffuseMap", assetManager.loadTexture("Textures/texture1.png"));
    }
    
    
}