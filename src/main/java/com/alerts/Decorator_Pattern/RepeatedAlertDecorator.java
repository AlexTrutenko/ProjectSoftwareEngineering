package com.alerts.Decorator_Pattern;

import com.alerts.Alert;
/**Decorator for repeated alerts.
 * Repeats the warning a specified number of times at intervals.
 */
public class RepeatedAlertDecorator extends AlertDecorator{
    private long repeatInterval; // In milliseconds
    private int amountOfRepeats;
    int counter = 0;

    /**Constructor
     * @param decoratedAlert - the base alert to decorate
     * @param repeatInterval - interval between repeats in milliseconds
     * @param amountOfRepeats - number of times to repeat the alert
     */

    public RepeatedAlertDecorator(Alert decoratedAlert, long repeatInterval, int amountOfRepeats){
        super(decoratedAlert);
        this.amountOfRepeats = amountOfRepeats;
        this.repeatInterval = repeatInterval;
    }
    /** Checks if the repeat should be initiated
    * @return true if the condition is CRITICAL or HIGH
    */
    public boolean shouldRepeat(){
        String cond = super.getCondition().toUpperCase();
        if (cond.equals("CRITICAL") || cond.equals("HIGH")){
            return true;
        }
        return false;
    }
    /**Returns a string describing the current repeat interval if repeats are still available.
     * 
     * @return repeat information string or null if no repeats remain
     */
    public String repeatIntervals(){
        if(shouldRepeat() && counter < amountOfRepeats){
            counter++;	
            return "[REPEATED " + counter + "/" + amountOfRepeats + "] " + getCondition() + " (Patient: " + getPatientId() + "), interval:  " + repeatInterval + "ms";
        }
        else{
            return null;
        }
    }
    /**
     * Gets the number of repeats already performed.
     * @return count of repeats done
     */

    public int getRepeatCounter() {
        return counter;
    }
    /**
     * Gets the interval between repeats.
     * @return repeat interval in milliseconds
     */

    public long getRepeatInterval() {
        return amountOfRepeats;
    }
    /**
     * Checks if all repeats have been completed.
     * @return true if the repeat count reached the set amount
     */
    public boolean isFinished() {
        return counter >= amountOfRepeats;
    }
    
}
