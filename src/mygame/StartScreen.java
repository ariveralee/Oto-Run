package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class StartScreen extends AbstractAppState implements ActionListener {

    BitmapText text, spaceText;
    private Node cameraTarget;
    HelloAnimation helloAnimation;
    FilterPostProcessor fpp;
    AppStateManager asm;
    boolean realGameStarted = false;

    // State Transitions are coded here!
    public void onAction(String name, boolean keyDown, float tpf) {
        if (keyDown) {
            if (name.equals("Start") && !realGameStarted) {
                Game game = new Game(); // false: real game, not a demo
                asm.detach(this);
                asm.attach((AppState) game);
                realGameStarted = true; // this is needed to avoid triggering multiple games
                                        // if someone pressed space multiple times      
            }
            if (name.equals("Quit")) {
                // this is brutal.
                System.exit(0);
            }
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        helloAnimation = (HelloAnimation) app;
        asm = stateManager;
        HelloAnimation.clearJMonkey(helloAnimation);

        // Text
        //main.setGuiFont(main.getAssetManager().loadFont("Interface/Fonts/Console.fnt"));
        BitmapFont bmf = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(bmf);
        text.setSize(bmf.getCharSet().getRenderedSize() * 7);
        text.setColor(ColorRGBA.Red);
        text.setText("OTO-RUN");
        helloAnimation.getGuiNode().attachChild(text);
        
        BitmapFont space = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        spaceText = new BitmapText(space);
        spaceText.setSize(space.getCharSet().getRenderedSize() * 3);
        spaceText.setColor(ColorRGBA.Blue);
        spaceText.setText("PRESS SPACE TO START");
        helloAnimation.getGuiNode().attachChild(spaceText);
        

        // set camera location
        helloAnimation.getFlyByCamera().setEnabled(false);
        cameraTarget = new Node();
        CameraNode camNode = new CameraNode("Camera Node", helloAnimation.getCamera());
        camNode.setLocalTranslation(new Vector3f(0f, 6f, 15f));
        camNode.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cameraTarget.attachChild(camNode);
        helloAnimation.getRootNode().attachChild(cameraTarget);


        // Keys
        InputManager inputManager = helloAnimation.getInputManager();
        inputManager.clearMappings();
        inputManager.addMapping("Start", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(this, "Start", "Quit");

   
        // and set blurring
        initPostProcessing();
    }

    private void initPostProcessing() {
        Spatial sky = SkyFactory.createSky(
                helloAnimation.getAssetManager(),"Textures/space_background.jpg", true);
        helloAnimation.getRootNode().attachChild(sky);
    }

    @Override
    public void cleanup() {
        helloAnimation.clearJMonkey(helloAnimation);
    }

    @Override
    public void update(float tpf) {
        AppSettings s = helloAnimation.getSettings();
       float lineY = s.getHeight() / 1.5f;
       float lineX = (s.getWidth() - text.getLineWidth()) / 2 ;
       text.setLocalTranslation(lineX, lineY, 0f);
       
       float spaceLineY = s.getHeight() / 2.5f;
       float spaceLineX = (s.getWidth() - spaceText.getLineWidth() / .71f);
       spaceText.setLocalTranslation(spaceLineX, spaceLineY, tpf);

       cameraTarget.rotate(0, 0, tpf/2);
    }
}
