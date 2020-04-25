package de.hso.badenair.util.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {
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
