package com.alerts.Decorator_Pattern;

import com.alerts.Alert;
/**
 * Implementation of priority decorator.
 */
public class PriorityAlertDecorator extends AlertDecorator{
    private String priority;
    
    /**Constructor.
     * 
     * @param decoratedAlert - the base alert to decorate
     * @param priorityLevel  - priority level as a String
     */
    public PriorityAlertDecorator(Alert decoratedAlert, String priorityLevel) {
        super(decoratedAlert);
        this.priority = priorityLevel;
    }
    /**
     * Priorities encoded
     * @return the weight based on the level of priority.
     */
    public int getPriority(){
        switch(priority.toUpperCase()){
            case "CRITICAL": return 4;
            case "HIGH": return 3;
            case "MEDIUM": return 2;
            default: return 1;
        }
    }
    /**
     * condition of priority
     * @return the string with the priority level
     */

    public String getCondition(){
        return "[" + priority.toUpperCase() + " PRIORITY]" + super.getCondition();
    }
}
