/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.util.Random;

/**
 *
 * @author ariveralee
 */
public class Obstacle extends Geometry {

    Sphere obstacle;
    Geometry obstacleGeom;
    Random random;

    public Obstacle(SimpleApplication sa) {
        obstacle = new Sphere(5, 11, 1.5f);
        obstacleGeom = new Geometry("obstacle", obstacle);
        Material obstacleMat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture obstacleText = sa.getAssetManager().loadTexture("Textures/lumpy.jpg");
        obstacleMat.setTexture("ColorMap", obstacleText);
        obstacleGeom.setMaterial(obstacleMat);
    }
}
