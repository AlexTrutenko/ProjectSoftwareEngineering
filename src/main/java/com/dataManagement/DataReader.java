package com.dataManagement;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
    /**
     * Stops the data reading process gracefully.
     * This method should close any open connections or resources used during data reading.
     *
     * @throws IOException if an error occurs while stopping the reading process
     */
    void stopReading() throws IOException;
    
}
