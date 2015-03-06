/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import redmonkey.RMReachedGoalInterrupt;

/**
 *
 */
public enum RMMonkeyState implements State<RMMonkey>{
    START(){
        @Override
        public void enter(RMMonkey monkey) {
        }    
        @Override
        public void update(RMMonkey monkey) {
        }
        @Override
        public void exit(RMMonkey e) {
        }
    },
    IDLE() {
        @Override
        public void enter(RMMonkey monkey) {
            monkey.channel.setAnim("Idle", 1.f);
            System.out.println("Monkey: entering IDLE");
        }

        @Override
        public void update(RMMonkey monkey) {
            // CHECK FOR INTERRUPTIONS
            /*if (!monkey.hpSystem.isAlive()) {
                monkey.characterControl.setWalkDirection(Vector3f.ZERO);
                return;
            }
            }*/
        }

        @Override
        public void exit(RMMonkey e) {
            System.out.println("Monkey: exiting IDLE");
        }
    },
    WALKING_TOWARD_BANANA() {
        
        RMReachedGoalInterrupt irq;
        @Override
        public void enter(RMMonkey monkey) {
            monkey.channel.setAnim("Punches", 1.f);
            System.out.println("Monkey: entering WALKING_TOWARD_BANANA");
            irq=new RMReachedGoalInterrupt(monkey);
        }

        @Override
        public void update(RMMonkey monkey) {
            monkey.goTo();
            // CHECK FOR INTERRUPTIONS
            /*if (!monkey.hpSystem.isAlive()) {
                monkey.characterControl.setWalkDirection(Vector3f.ZERO);
                return;
            }
            }*/
        }

        @Override
        public void exit(RMMonkey e) {
            System.out.println("Monkey: exiting WALKING_TOWARD_BANANA");
        }
    };    
            @Override
    public boolean onMessage(RMMonkey e, Telegram tlgrm) {
        return false;
    }
}
