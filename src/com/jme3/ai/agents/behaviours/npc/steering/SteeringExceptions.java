package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.behaviours.BehaviourExceptions.IllegalBehaviourException;

/**
 *
 * @author Tihomir Radosavljevic
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class SteeringExceptions {

    /**
     * @see IllegalBehaviour
     */
    public static final class IllegalBrakingFactorException extends IllegalArgumentException {

        public IllegalBrakingFactorException(float brakingFactor) {
            super("The braking factor value must be contained in the [0,1] interval. The current value is " + brakingFactor);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeScalarMultiplierException extends IllegalBehaviourException {

        public NegativeScalarMultiplierException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeMaxDistanceException extends IllegalBehaviourException {

        public NegativeMaxDistanceException(float maxDistance) {
            super("The max distance value can not be negative. Current value is " + maxDistance);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class NegativeSlowingDistanceException extends IllegalBehaviourException {

        public NegativeSlowingDistanceException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class BoxExploreWithNegativeSubdivisionDistanceException extends IllegalBehaviourException {

        public BoxExploreWithNegativeSubdivisionDistanceException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class BoxExploreWithNegativeBoxDimensionsException extends IllegalBehaviourException {

        public BoxExploreWithNegativeBoxDimensionsException(String message) {
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

    /**
     * @see IllegalBehaviourException
     */
    public static class WanderWithoutWanderAreaException extends IllegalBehaviourException {

        public WanderWithoutWanderAreaException(String message) {
            super(message);
        }
    }
}
