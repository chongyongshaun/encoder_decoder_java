package ie.atu.sw;

import java.io.*;
import java.util.HashMap;

// CSV = Comma seperated values, so just split them with ','
public class CSVReader {
	String file;

	public CSVReader() {
		this.file = "encodings-10000.csv"; // this is the default mapping file, if you want to use another one just pass
											// it in the constructor
	}

	public CSVReader(String file) {
		this.file = file;
	}

	// reads each line from the csv and puts it into a hashmap, O(n) time complexity
	// cuz we're just iterating through the file once
	public HashMap<Integer, String> getDecodingMap() {
		// String file = "src\\students.csv"; this works but i figured \\ is only for
		// windows so i opted for input stream
		BufferedReader reader = null;
		HashMap<Integer, String> map = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				// the second value after the , is the num/id sort of corresponding with the
				// decoded string value
				// key= id, val= the decoded string
				map.put(Integer.parseInt(row[1]), row[0]);
			}
			map.put(10000, " ");
			map.put(10001, ",");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// close the reader after it's been used and catch the error otherwise the
				// compiler complains
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	// same rationale as before just flipped so also O(n) time complexity
	public HashMap<String, Integer> getEncodingMap() {
		BufferedReader reader = null;
		HashMap<String, Integer> map = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				map.put(row[0], Integer.parseInt(row[1]));
			}
			map.put(" ", 10000);
			map.put(",", 10001);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	// O(n) time complexity again, just iterating through the file once
	public HashMap<String, Integer> getEncodingSuffixMap() {
		BufferedReader reader = null;
		HashMap<String, Integer> suffixMap = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				if (row[0].startsWith("@@")) {
					String suffix = row[0].substring(2);
					suffixMap.put(suffix, Integer.parseInt(row[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return suffixMap;
	}

	// O(n) time complexity again, same here
	public HashMap<String, Integer> getEncodingNonSuffixMap() {
		BufferedReader reader = null;
		HashMap<String, Integer> nonSuffixMap = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				if (!row[0].startsWith("@@")) {
					nonSuffixMap.put(row[0], Integer.parseInt(row[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return nonSuffixMap;
	}

	public static void main(String[] args) {
		CSVReader csvReader = new CSVReader("encodings-10000.csv");
		// System.out.println(csvReader.getEncodingNonSuffixMap());
		// System.out.println(csvReader.getEncodingSuffixMap());
		// System.out.println(csvReader.getEncodingMap());
		// System.out.println(csvReader.getDecodingMap());
	}
}
