/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.simsilica.lemur.event.BaseAppState;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class RedMonkeyDebugAppState extends BaseAppState {

    protected ViewPort viewPort;
    protected RenderManager rm;
    RMSpace space;
    Node rootNode;
    Node redmonkeyDebugRootNode = new Node("RM Debug Root Node");
    BitmapFont font;
    protected HashMap<RMItem, BitmapText> labels = new HashMap<RMItem, BitmapText>();

    public RedMonkeyDebugAppState(RMSpace space, Node rootNode, BitmapFont font) {
        this.space = space;
        this.rootNode = rootNode;
        this.font = font;
    }

    @Override
    protected void initialize(Application app) {
        this.rm = app.getRenderManager();
        redmonkeyDebugRootNode.setCullHint(Spatial.CullHint.Never);
        viewPort = rm.createMainView("Physics Debug Overlay", app.getCamera());
        viewPort.setClearFlags(false, true, false);
        viewPort.attachScene(redmonkeyDebugRootNode);    
        rootNode.attachChild(redmonkeyDebugRootNode);
    }

    @Override
    public void update(float tpf) {
        updateRMItems();
    }

    private void updateRMItems() {
        HashMap<RMItem, BitmapText> oldObjects = labels;
        labels = new HashMap<RMItem, BitmapText>();
        Collection<RMItem> current = space.items;
        //create new map
        for (Iterator<RMItem> it = current.iterator(); it.hasNext();) {
            RMItem physicsObject = it.next();
            //copy existing spatials
            if (oldObjects.containsKey(physicsObject)) {
                BitmapText spat = oldObjects.get(physicsObject);
                labels.put(physicsObject, spat);
                oldObjects.remove(physicsObject);
            } else {
                //if (filter == null || filter.displayObject(physicsObject))
                {
                    //logger.log(Level.FINE, "Create new debug RigidBody");
                    //create new spatial
                    BitmapText hudText = new BitmapText(font, false);
                    hudText.scale(0.01f);
//hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
//hudText.setColor(ColorRGBA.Blue);                             // font color
                    hudText.setText(physicsObject.toString());             // the text
                    hudText.setLocalTranslation(physicsObject.position); // position
                    hudText.addControl(new BillboardControl());
                    labels.put(physicsObject, hudText);
                    redmonkeyDebugRootNode.attachChild(hudText);
                }
            }
        }
        //remove leftover spatials
        for (Map.Entry<RMItem, BitmapText> entry : oldObjects.entrySet()) {
            RMItem object = entry.getKey();
            BitmapText spatial = entry.getValue();
            spatial.removeFromParent();
        }
    }
    @Override
    public void render(RenderManager rm) {
        super.render(rm);
        if (viewPort != null) {
            rm.renderScene(redmonkeyDebugRootNode, viewPort);
        }
    }
    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void enable() {
    }

    @Override
    protected void disable() {
    }
}
