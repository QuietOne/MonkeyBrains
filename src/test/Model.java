package test;

/**
 * Example for using model to add it to agent. Yes, I could only add float to 
 * setModel() but I wanted to show big picture. :)
 * @author Tihomir Radosavljević
 */
public class Model {
    /**
     * Turn speed for agent.
     */
    private float turnSpeed;

    public Model(float turnSpeed) {
        this.turnSpeed = turnSpeed;
    }
    
    public float getTurnSpeed() {
        return turnSpeed;
    }
    
}
