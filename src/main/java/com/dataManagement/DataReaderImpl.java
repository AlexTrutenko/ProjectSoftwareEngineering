package com.dataManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class DataReaderImpl implements DataReader{

    private File filepath;

    public DataReaderImpl(String filepath){
        this.filepath = new File(filepath);
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException{
        if(!filepath.exists()){
            throw new IOException(filepath + " does not exist");
        }
        if (!filepath.isFile()) {
            throw new IOException("Not a file: " + filepath.getAbsolutePath());
        }
        processFile(filepath, dataStorage);
    }
    private void processFile(File file, DataStorage dataStorage) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String headerLine = br.readLine();
        if (headerLine == null) {
            br.close();
            return;
        }
        String[] header = headerLine.split(",");
        int patientIdIdx = -1;
        int measurementValueIdx = -1;
        int recordTypeIdx = -1;
        int timestampIdx = -1;
        for (int i = 0; i < header.length; i++) {
            String h = header[i].trim();
            if ("patientId".equals(h)) {
                patientIdIdx = i;
            } else if ("measurementValue".equals(h)) {
                measurementValueIdx = i;
            } else if ("recordType".equals(h)) {
                recordTypeIdx = i;
            } else if ("timestamp".equals(h)) {
                timestampIdx = i;
            }
        }
        String line;
        while ((line = br.readLine()) != null) {
            String[] sections = line.split(",", -1);
            int patientId = Integer.parseInt(sections[patientIdIdx].trim());
            double measurementValue = Double.parseDouble(sections[measurementValueIdx].trim());
            String recordType = sections[recordTypeIdx].trim();
            long timestamp = Long.parseLong(sections[timestampIdx].trim());

            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        }
        br.close();
    }
}
