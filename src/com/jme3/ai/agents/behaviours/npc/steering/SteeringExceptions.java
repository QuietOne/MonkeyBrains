package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.BehaviourExceptions.IllegalBehaviourException;

/**
 * Class container for exceptions related to steering behaviour.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class SteeringExceptions {

    /**
     * Main generic exception related for all illegal arguments for steering
     * behaviours.
     *
     * @see IllegalBehaviourException
     * @author Tihomir Radosavljević
     * @version 1.0.0
     */
    public static class IllegalSteeringBehaviourException extends IllegalBehaviourException {

        public IllegalSteeringBehaviourException(Agent agent, String message, float value) {
            super(agent, message, value);
        }

        public IllegalSteeringBehaviourException(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown when braking factor is not set in the [0, 1]
     * interval.
     *
     * @see IllegalSteeringBehaviourException
     * @author Jesús Martín Berlanga
     * @version 1.0.1
     */
    public static class IllegalBrakingFactorException extends IllegalSteeringBehaviourException {

        public IllegalBrakingFactorException(float brakingFactor) {
            super("The braking factor value must be contained in the [0,1] interval. The current value is " + brakingFactor + '.');
        }
    }

    /**
     * This exception is thrown when negative value is given to steering
     * behaviour.
     *
     * @see IllegalSteeringBehaviourException
     * @author Jesús Martín Berlanga
     * @author Tihomir Radosavljević
     * @version 1.0.1
     */
    public static class NegativeValueException extends IllegalSteeringBehaviourException {

        public NegativeValueException(String message) {
            super(message);
        }

        public NegativeValueException(String message, float value) {
            super(message + " You inputed " + value + '.');
        }
    }

    /**
     * This exception is thrown when negative value is given to box explore
     * steering behaviour.
     *
     * @see NegativeValueException
     * @author Jesús Martín Berlanga
     * @version 1.0.1
     */
    public static class BoxExploreWithNegativeValueException extends NegativeValueException {

        public BoxExploreWithNegativeValueException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ContainmentWithInvalidContainmenetAreaException extends IllegalBehaviourException {

        public ContainmentWithInvalidContainmenetAreaException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ContainmentWithoutContainmentAreaException extends IllegalBehaviourException {

        public ContainmentWithoutContainmentAreaException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class HideWithoutTargetException extends IllegalBehaviourException {

        public HideWithoutTargetException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeSeparationFromObstacleException extends IllegalBehaviourException {

        public NegativeSeparationFromObstacleException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeDistanceToEvadeException extends IllegalBehaviourException {

        public NegativeDistanceToEvadeException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class LeaderFollowWithoutLeaderException extends IllegalBehaviourException {

        public LeaderFollowWithoutLeaderException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeDistanceToChangeFocusException extends IllegalBehaviourException {

        public NegativeDistanceToChangeFocusException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ObstacleAvoindanceWithNegativeMinDistanceException extends IllegalBehaviourException {

        public ObstacleAvoindanceWithNegativeMinDistanceException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ObstacleAvoindanceWithNoMinTimeToCollisionException extends IllegalBehaviourException {

        private ObstacleAvoindanceWithNoMinTimeToCollisionException(String message) {
            super(message);
        }
    }

    public static class PathFollowIstinsufficientPointsException extends IllegalBehaviourException {

        public PathFollowIstinsufficientPointsException(String message) {
            super(message);
        }
    }

    public static class PathFollowNegativeRadiusException extends IllegalBehaviourException {

        public PathFollowNegativeRadiusException(String message) {
            super(message);
        }
    }

    public static class PathFollowNegativeCohesionStrengthException extends IllegalBehaviourException {

        public PathFollowNegativeCohesionStrengthException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class QueuingWithNegativeMinDistanceException extends IllegalBehaviourException {

        public QueuingWithNegativeMinDistanceException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class UnalignedObstacleAvoindanceWithNegativeDistanceMultiplierException extends IllegalBehaviourException {

        public UnalignedObstacleAvoindanceWithNegativeDistanceMultiplierException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ObstacleAvoindanceWithoutTimeIntervalException extends IllegalBehaviourException {

        public ObstacleAvoindanceWithoutTimeIntervalException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderInvalidRandomFactorException extends IllegalBehaviourException {

        public SphereWanderInvalidRandomFactorException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderInvalidRotationFactorException extends IllegalBehaviourException {

        public SphereWanderInvalidRotationFactorException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderWithoutTimeIntervalException extends IllegalBehaviourException {

        public SphereWanderWithoutTimeIntervalException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderWithoutRadiusException extends IllegalBehaviourException {

        public SphereWanderWithoutRadiusException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class WallApproachWithoutWallException extends IllegalBehaviourException {

        public WallApproachWithoutWallException(String message) {
            super(message);
        }
    }

    public static class WallApproachNegativeOffsetException extends IllegalBehaviourException {

        public WallApproachNegativeOffsetException(String message) {
            super(message);
        }
    }
}
