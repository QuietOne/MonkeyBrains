//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents;

/**
 * This exception is thrown If it has been tried to call a method inside the Agent class
 * with illegal arguments. <br><br>
 * 
 * This prevent the developer from suffer unexpected situations.
 * 
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class IllegalActorFunctionParams extends IllegalArgumentException 
{ 
    protected IllegalActorFunctionParams(String msg) { super(msg); }
}