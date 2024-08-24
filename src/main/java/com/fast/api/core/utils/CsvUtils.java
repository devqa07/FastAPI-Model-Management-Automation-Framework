package com.fast.api.core.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

/**
 * CSV Reader utility responsible for performing all CSV
 * related functions Different methods decide different roles on csv
 * Requires only file name place in the src/main/resources folder.
 * comma(,) is used as separator/delimiter
 */
public class CsvUtils {
    char customSeparator = ',';

    public CsvUtils() {
    }

    public List<String[]> readCsvData(String fileName) {
        List<String[]> allData = null;
        com.opencsv.CSVReader csvReader;
        CSVParser parser;
        FileReader filereader;
        try {

            // Create an object of file reader class with CSV file as a parameter.
            filereader = new FileReader(String.valueOf(Paths.get("src/main/resources/testData/" + fileName + ".csv")));

            // create csvParser object with custom separator semicolon
            parser = new CSVParserBuilder().withSeparator(customSeparator).build();

            // create csvReader object with parameter file-reader and parser
            csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    // To Fetch Any Random Cell from CSV file(Indexing starts from 0)
    public String getSpecificCSVData(List<String[]> csvData, int rowNum, int cellNum) {
        return csvData.get(rowNum)[cellNum];
    }
}