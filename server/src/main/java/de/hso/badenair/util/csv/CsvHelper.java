package de.hso.badenair.util.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Small helper class to read and parse csv files.
 */
public class CsvHelper {
	/**
	 * Reads the given csv file and pases the document.
	 * @param csvFile the csv file to read and parse
	 * @return the parsed csv data: a list entry represents a line in the document,
     * an element of the string array represents a single value
	 */
	public static List<String[]> getData(File csvFile) {
		ArrayList<String[]> data = new ArrayList<String[]>();

		try (BufferedReader reader = new BufferedReader(
				new FileReader(csvFile))) {
			String line = "";

			while ((line = reader.readLine()) != null) {
				data.add(line.split(","));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
