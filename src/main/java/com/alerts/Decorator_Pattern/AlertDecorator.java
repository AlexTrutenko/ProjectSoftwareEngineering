package com.alerts.Decorator_Pattern;

import com.alerts.Alert;

public class AlertDecorator extends Alert{
    private Alert alertDecorator;

    /**
     * Constructor.
     * @param alertDecorator the Alert instance to be decorated
     */
    public AlertDecorator(Alert alertDecorator){
        super(alertDecorator.getPatientId(), alertDecorator.getCondition(), alertDecorator.getTimestamp());
        this.alertDecorator = alertDecorator;
    }
    /**
     * Getter for patientID
     * @return patientID
     */
    @Override
    public String getPatientId() {
        return alertDecorator.getPatientId();
    }
    /**
     * Getter for condition
     * @return condition
     */
    @Override
    public String getCondition() {
        return alertDecorator.getCondition();
    }

    /**
     * Getter for timestamp
     * @return timestamp
     */
    @Override
    public long getTimestamp() {
        return alertDecorator.getTimestamp();
    }

}
