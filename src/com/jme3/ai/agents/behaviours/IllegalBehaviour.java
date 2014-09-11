//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours;

/**
 * This exception is thrown If it has been tried to instantiate a behaviour
 * with illegal arguments. <br><br>
 * 
 * This prevent the developer from suffer unexpected situations.
 * 
 * @author Jesús Martín Berlanga
 * @version 1.0.1
 */
public class IllegalBehaviour extends IllegalArgumentException 
{ 
    protected IllegalBehaviour(String msg) { super(msg); }
}