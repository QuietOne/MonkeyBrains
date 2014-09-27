package com.jme3.ai.agents;

import java.util.List;

/**
 * Class for team of agents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class Team {

    /**
     * Name of team.
     */
    private String name;
    /**
     * Team accumulated score.
     */
    private float score;
    /**
     * Members of team.
     */
    private List<Agent> members;

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, float score) {
        this.name = name;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Agent> getMembers() {
        return members;
    }

    public void setMembers(List<Agent> members) {
        this.members = members;
    }

    public void addMember(Agent agent) {
        members.add(agent);
    }

    public void removeMember(Agent agent) {
        members.remove(agent);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Team other = (Team) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
