package com.dataManagement;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
/**
 * Implementation of DataReader that reads patient data via a WebSocket connection.
 * Connects to a WebSocket server and streams patient data into the provided DataStorage.
 */
public class WebSocketDataReader implements DataReader{
    private URI serverPort;
    private WebSocketClient client;
    /**
     * Constructs a WebSocketDataReader with the given WebSocket server URI.
     *
     * @param port the URI string of the WebSocket server to connect to
     * @throws URISyntaxException if the given port string is not a valid URI
     */
    public WebSocketDataReader(String port) throws URISyntaxException {
        this.serverPort = new URI(port);
    }
    /**
     * Establishes a blocking connection to the WebSocket server and starts reading data,
     * pushing updates into the provided DataStorage.
     *
     * @param dataStorage the DataStorage instance to receive patient data updates
     * @throws IOException if the connection to the WebSocket server cannot be established
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException{
        this.client = new WebSocketPatientClient(serverPort, dataStorage);
        try{
            this.client.connectBlocking();
        } catch(InterruptedException  e){
            throw new IOException("Failed to connect to WebSocket server", e);
        }
    }
    /**
     * Stops reading data by closing the WebSocket connection if it is open.
     *
     * @throws IOException if an IOexception error occurs during the closing of the connection
     */
    @Override
    public void stopReading() throws IOException{
        if (client != null && client.isOpen()) {
            this.client.close();
        }
    }
}
