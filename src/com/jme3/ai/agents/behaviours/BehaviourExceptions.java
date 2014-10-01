/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.ai.agents.behaviours;

import com.jme3.ai.agents.Agent;

/**
 * Class container for exceptions related to behaviours.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class BehaviourExceptions {

    /**
     * This exception is thrown if it has been tried to instantiate a behaviour
     * with illegal arguments.
     */
    public static class BehaviourException extends RuntimeException {

        public BehaviourException(String message) {
            super(message);
        }

        public BehaviourException(Agent agent, String message, float value) {
            super(agent + message + value + '.');
        }
    }

    public static class AgentNotIncludedException extends BehaviourException {

        public AgentNotIncludedException() {
            super("You can not instantiate a behaviour without an agent.");
        }
    }

    public static class NullBehaviourException extends BehaviourException {

        public NullBehaviourException() {
            super("Behaviour has not been initialized");
        }

        public NullBehaviourException(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown if the behaviour tries to call null target.
     */
    public static class TargetNotFoundException extends BehaviourException {

        public TargetNotFoundException() {
            super("Target can not be null");
        }
    }
}
