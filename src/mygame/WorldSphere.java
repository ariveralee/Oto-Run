/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.util.Random;

/**
 *
 * @author ariveralee
 */
public class WorldSphere extends Node {

    private Sphere worldSphere;
    Node spinner;
    private Geometry worldSphereGeom;

    public WorldSphere(SimpleApplication sa) {
        spinner = new Node();
        worldSphere = new Sphere(100, 100, 200f);
        worldSphereGeom = new Geometry("world sphere", worldSphere);
        Material sphereMat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture sphereText = sa.getAssetManager().loadTexture("Textures/space_background.jpg");
        sphereMat.setTexture("ColorMap", sphereText);
        worldSphereGeom.setMaterial(sphereMat);
        spinner.attachChild(worldSphereGeom);
        // add to the scene graph
        sa.getRootNode().attachChild(spinner);
        spinner.setLocalTranslation(0, -202.4f, 0);
        spinner.addControl(new worldSphereControl());
    }

    class worldSphereControl extends AbstractControl {

        @Override
        protected void controlUpdate(float tpf) {
            spinner.rotate(tpf / 4, 0, 0);
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}