package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rolf
 */
public class Oto {

    SimpleApplication sa;
    private AnimChannel channel;
    private AnimControl control;
    private String requestedState = "";
    Node otoNode;
    Control otoControl;
    float speed = 10f;
    Obstacle obstacles[];
    private float groundSpeed = 0.0f;
    int health = 100;
    //
    // -------------------------------------------------------------------------
    // the key action listener: set requested state
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) {
                requestedState = name;
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Left") && otoNode.getLocalTranslation().x > -5.0f) {
                otoNode.move(-tpf * speed, 0, 0);
            } else if (name.equals("Right") && otoNode.getLocalTranslation().x < 5.0f) {
                otoNode.move(tpf * speed, 0, 0);
            }
        }
    };

    // -------------------------------------------------------------------------
    public Oto(SimpleApplication sa, Obstacle obstacles[]) {
        this.sa = sa;
        this.obstacles = obstacles;
        initKeys();
        initModel();
    }

    // -------------------------------------------------------------------------  
    // set ground speed. Used for walking adjustment in control.
    public void setGroundSpeed(float spd) {
        this.groundSpeed = spd;
    }

    // -------------------------------------------------------------------------  
    // Custom Keybindings: Mapping a named action to a key input.
    private void initKeys() {
        sa.getInputManager().addMapping("Push", new KeyTrigger(KeyInput.KEY_V));
        sa.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        sa.getInputManager().addListener(actionListener, new String[]{"Push", "Jump"});

        // key bindings for moving otto
        sa.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        sa.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        sa.getInputManager().addListener(analogListener, new String[]{"Left", "Right"});
    }

    // -------------------------------------------------------------------------
    // init model
    // load a model that contains animation
    private void initModel() {
        otoNode = (Node) sa.getAssetManager().loadModel("Models/Oto/Oto.mesh.xml");
        otoNode.setLocalScale(0.5f);
        sa.getRootNode().attachChild(otoNode);

        // This turns oto in the oppose direction.
        Quaternion q = new Quaternion();
        q.fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
        otoNode.rotate(q);

        // Create a controller and channels.
        control = otoNode.getControl(AnimControl.class);
        channel = control.createChannel();
        channel.setAnim("stand");
        //
        // add control
        otoControl = new OtoControl();
        otoNode.addControl(otoControl);


    }

    // -------------------------------------------------------------------------
    // OtoControl
    class OtoControl extends AbstractControl {

        private final int STATE_WALK = 0;
        private final int STATE_STAND = 1;
        private final int STATE_JUMP = 2;
        private int state;
        private boolean stateIsInitialized = false;
        private float stateTime;

        public OtoControl() {
            switchState(STATE_STAND);
        }

        // ---------------------------------------------------------------------
        @Override
        protected void controlUpdate(float tpf) {
            stateTime += tpf;
            // state machine
            String reqState = requestedState;
            requestedState = "";

            // just for debugging purpose: toggle ground speed
            if (reqState.equals("Push")) {
                groundSpeed = groundSpeed > 0 ? 0 : 1.0f;
            }

            // let's get that collision yo.
            CollisionResults results = new CollisionResults();
            for (Obstacle o : obstacles) {
                BoundingVolume bv = o.obstacleGeom.getWorldBound();
                otoNode.collideWith(bv, results);

                if (results.size() > 0) {
                     //health -= 5;

                    System.out.println("Here's my health: " + health);
               if (health <= 0) {
                      System.exit(0);
                  }
                    results.clear();
                }

            }
            // Exit if we lose too much. 

            // ----------------------------------------
            switch (state) {
                case (STATE_STAND):
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("stand", 0.0f);
                    }
                    if (reqState.equals("Jump")) {
                        switchState(STATE_JUMP);
                    }//
                    // if the earth spins, immediately switch to walk.
                    else if (groundSpeed > 0.0f) {
                        switchState(STATE_WALK);
                    }
                    break;
                case (STATE_JUMP):
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("pull");

                    }
                    // Jump
                    // Get where we currently are.
                    Vector3f v = otoNode.getLocalTranslation();
                    float y = FastMath.sin(stateTime * 5);
                    otoNode.setLocalTranslation(v.x, y + 3, v.z);
                    //
                    // end of state?
                    if (y <= 0.0f) {
                        // second vector to get our location after landing
                        Vector3f v2 = otoNode.getLocalTranslation();
                        otoNode.setLocalTranslation(v2.x, v2.y - 3, v2.z);
                        switchState(STATE_STAND);
                    }
                    break;
                case (STATE_WALK):
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("Walk");
                        channel.setSpeed(groundSpeed);
                    }
                    // state action: adjust to groundspeed
                    channel.setSpeed(groundSpeed);
                    //
                    // end of state?
                    if (groundSpeed == 0.0f) {
                        switchState(STATE_STAND);
                    }
                    if (reqState.equals("Jump")) {
                        switchState(STATE_JUMP);
                    }
                    break;
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        // ---------------------------------------------------------------------
        private void switchState(int state) {
            stateIsInitialized = false;
            this.state = state;
            stateTime = 0.0f;
        }
    }
}
