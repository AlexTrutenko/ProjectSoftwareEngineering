package com.dataManagement;

import java.io.IOException;
import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
/**
 * WebSocket client for receiving patient data updates in real-time.
 * Connects to a WebSocket server and updates the given DataStorage with incoming patient records.
 */
public class WebSocketPatientClient extends WebSocketClient{
    private final DataStorage dataStorage;
    private int attemptsToReconnect = 0;

    /**
     * Constructs a new WebSocketPatientClient.
     *
     * @param port        the URI of the WebSocket server to connect to
     * @param dataStorage the DataStorage instance to update with received patient data
     */
    public WebSocketPatientClient(URI port, DataStorage dataStorage){
        super(port);
        this.dataStorage = dataStorage;
    }
    /**
     * Called when the WebSocket connection is opened.
     * Resets the reconnect attempts counter.
     *
     * @param handshake the handshake data from the server
     */
    @Override
    public void onOpen(ServerHandshake handshake){
        System.out.println("Server is connected !");
        attemptsToReconnect = 0;
    }
     /**
     * Called when a new message is received from the WebSocket server.
     * Parses the message and updates the DataStorage.
     *
     * @param message the received message string in the format "patientId,measurementValue,recordType,timestamp"
     */
    @Override
    public void onMessage(String message){
        
        try{
            String[] parts = message.split(",");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Not a valid message format !");
            }
            int patientId = Integer.parseInt(parts[0].trim());
            double value = Double.parseDouble(parts[1].trim());
            String type = parts[2].trim();
            long timestamp = Long.parseLong(parts[3].trim());

            dataStorage.addPatientData(patientId, value, type, timestamp);
        }catch(Exception e){
            System.out.println("The error while parsing the message: " + message);
            e.printStackTrace();
        }
    }
    /**
     * Called when the WebSocket connection is closed.
     * Attempts to reconnect automatically.
     *
     * @param code   the status code indicating the reason for closure
     * @param reason the textual reason for closure
     * @param remote true if the connection was closed by the remote host
     */
    @Override
    public void onClose(int code, String reason, boolean remote){
        System.out.println("Websocket is closed. Reason: " + reason + "... Trying to reconnect..." );
        reconnectAttempt();
    }
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage() + ". Trying to reconnect...");
        ex.printStackTrace();
        reconnectAttempt();
    }
     /**
     * Attempts to reconnect to the WebSocket server up to 5 times, 
     * waiting 3 seconds between each attempt.
     * Runs asynchronously in a new thread.
     */

    private void reconnectAttempt(){
        new Thread(() -> {        
        while(attemptsToReconnect < 5){
            try{
                System.out.println("Trying to reconnect... Attempt " + (attemptsToReconnect+1) + "/5");
                this.reconnectBlocking();
                System.out.println("Reconnected successfully !");
                attemptsToReconnect = 0;
                break;
            } catch(Exception e){
                System.out.println("Error occured while executing ! ");
                attemptsToReconnect++;
                try{
                    Thread.sleep(3000);
                }catch(Exception error){
                    error.printStackTrace();
                }
            }}
        if (attemptsToReconnect == 5) {
            System.out.println("Failed to reconnect after 5 attempts.");}
        }).start();

    }
}
