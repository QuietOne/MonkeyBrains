/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

/**
 *
 */
public interface RMInterrupt {
    boolean testForInterrupt();
    boolean manageInterrupt();
    boolean didISucceed();
}
