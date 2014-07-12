#User Guide for MonkeyBrains

MonkeyBrains is a sophisticated AI Engine for jMonkeyEngine. It uses an agent framework to wrap human and AI controlled characters in plugin-based AI algorithms so that different each game can pick out whichever AI techniques fits best. 

##Basic concepts

###Agent<T>
Wrapper class for agent. It contains attributes and operations on those attributes. It is not recommended for extending. Instead of extending, you can use method setModel(T model) and set your attributes for agent.
Team – agent’s team. It contains attributes of team. By default team name is unique ID used for calculating friendly fire if it is off (agent can’t damage agent from its own team)

###Behaviour
Base class for agent behaviours. Every behavior that is made must extend this class. Every behavior then must implement ```controlUpdate(float tpf)``` and ```controlRender(RenderManager rm, ViewPort vp)```. Those have been requested by ```com.jme3.scene.control.AbstractControl```, which behaviour is extending. Also class that is extending Behaviour must have constructor that contains agent as input. With this, behaviour can change attributes of agent that is behaving. In ```com.jme3.ai.agents.behaviours.npc``` you can see examples of some simple behaviours that are easy for using for your NPC (Non-player character) agents. In ```com.jme3.ai.agents.behaviours.player``` are some simple behaviours adequate for player agents.

###GameObject
Class for all objects in game, that can be updated or destroyed. Example for these would be: Items, bullets, obstacles, agent (agent extends GameObjects)…

###Game
Singleton class that contains all GameObjects and updated them. It is built for easier manipulation of agents.

###GameControl
Interface that is added to Game for additional manipulation of game.

###GameObjectEvent
MonkeyBrains is event-based framework and GameObjectEvent is base class for all other events. It is recommended that first layer of extending GameObjectEvent be other event that is generic for all other GameObjects like GameObjectSeenEvent and then extending GameObjectSeenEvent with events like AgentSeenEvent, ItemSeenEvent…

###AbstractWeapon and AbstractBullet
Classes that also extend GameObject. Because MonkeyBrains will be used for games, agent contains attribute weapon and using attack behaviours will attack with that weapon. Use these class for making your weapons and bullets that will come out of those weapons.

##Code example for agent:
```java
public class Example extends SimpleApplication {

    Game game = Game.getInstance(); //creating game
    
    public static void main(String[] args) {
        Example app = new Example();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //defining rootNode for game processing
        game.setRootNode(rootNode);
        //defining input manager
        game.setInputManager(inputManager);
        //setting game control
        game.setGameControl(new CustomMadeGameControl());
        //registering input
        game.getGameControl().loadInputManagerMapping();

        //initialization of Agents with their names and spatials
        Agent agent = new Agent("First agent", createAgentSpatial()); 
        //there isn't any method in framework like createAgentSpatial()
        //user is supposed to build his own spatials for game

        //adding agent to game
        game.addAgent(agent);

        //setting moveSpeed, rotationSpeed, mass..
        agent.setMoveSpeed(20); 
        agent.setRotationSpeed(30);
        //used for steering behaviours in com.jme3.ai.agents.behaviours.npc.steering
        agent.setMass(40);
        agent.setMaxForce(3);
        //used for looking behaviors
        agent.setVisibilityRange(400f);
        

        //giving them weapons
        agent.setWeapon(new LaserWeapon("laser", player));
        //LaserWeapon extend AbstractWeapon and is custom made by user

        //creating main behaviour
        //agent can have only one behaviour but that behaviour can contain other behaviours
        SimpleMainBehaviour agentMain = new SimpleMainBehaviour(agent);
        SimpleLookBehaviour look = new SimpleLookBehaviour(agent);
        SimpleAttackBehaviour attack = new SimpleAttackBehaviour(agent);
        look.addListener(attack);
        agentMain.addBehaviour(look);
        agentMain.addBehaviour(attack);
        agentMain.addBehaviour(new WanderBehaviour(agent));
        agent.setMainBehaviour(agentMain);

        //starting agents
        game.start();

    }

    @Override
    public void simpleUpdate(float tpf) {
        game.update(tpf);
    }
}
```

##Working games:

For more examples of working games built with MonkeyBrains see:
https://github.com/QuietOne/MonkeyBrainsDemoGames

##Suggestions and questions:
If you have suggestion or any questions, please see forum:
http://hub.jmonkeyengine.org/forum/board/projects/monkeybrains/