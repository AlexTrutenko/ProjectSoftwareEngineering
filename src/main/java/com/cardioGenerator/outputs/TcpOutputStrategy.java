package com.cardioGenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
/**This class is used for creating of a TCP output. It implements OutputStrategy interface.
 * This application starts a server socket and waits for a client to connect.
 * If connected, shows the outputed data of a patient in a specific format:
 * 
 * Patient ID, timestamp, label and data.
 * @author Oleksandr Trutenko
 * 
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    /**Starts a TCP server socket on the given port and wait for a client connection.
     * 
     * @param port - The TCP port to listen on.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Sends a patient data line to a connected client via TCP.
     * It only sends output if the client is connected and ready. 
     * 
     * @param patientID - patient identification.
     * @param timestamp - timestamp of the data.
     * @param label - data label.
     * @param data - actual data value.
     * 
     * @return a formated data via TCP.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
