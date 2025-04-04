package ie.atu.sw;

import java.util.*;

public class EncoderDecoder {
	private HashMap<String, Integer> suffixMap;
	private HashMap<String, Integer> nonSuffixMap;
	private HashMap<String, Integer> encodingMap;
	private HashMap<Integer, String> decodingMap;

	public EncoderDecoder() { // default mapping file at src/
		CSVReader csv = new CSVReader();
		this.nonSuffixMap = csv.getEncodingNonSuffixMap();
		this.suffixMap = csv.getEncodingSuffixMap();
		this.encodingMap = csv.getEncodingMap();
		this.decodingMap = csv.getDecodingMap();
	}

	public EncoderDecoder(String filename) { // use another mapping file other than the default encodings-10000.csv
		CSVReader csv = new CSVReader(filename);
		this.nonSuffixMap = csv.getEncodingNonSuffixMap();
		this.suffixMap = csv.getEncodingSuffixMap();
		this.encodingMap = csv.getEncodingMap();
		this.decodingMap = csv.getDecodingMap();
	}

	// this function takes in a sentence and splits it into a bunch of tokens for
	// encoding
	// it also checks if the word is a suffix or not, if it is it adds @@ to the
	// start of it
	// time cpomplexity is O(n^2) because of the nested loop (looping thru each word
	// and also matching suffix function which is O(n))
	public String[] wordBreaker(String sentence) {
		List<String> list = new ArrayList<>();
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (word.length() < 2) {
				list.add(word);
			} else if (nonSuffixMap.containsKey(word)) { // whole word found in non suffix map
				list.add(word);
			} else {
				String[] pair = getLongestMatchingSuffixAndNonPair(word);
				String suffix = pair[1], non = pair[0];
				if (suffix != null) { // a pair was found, just add it
					list.add(non);
					list.add(suffix);
				} else { // no pair found, basically non is whole word
					if (nonSuffixMap.containsKey(non)) { // it's a word in the nonsuffix map
						list.add(non);
					} else { // else not a whole word just brute force break each char into single char and
								// match them
						String[] charArr = word.split("");
						for (String c : charArr) {
							if (nonSuffixMap.containsKey(c)) {
								list.add(c);
							} else {
								list.add("[???]");
							}
						}

					}
				}
			}
			list.add(" ");
		}
		String[] res = new String[list.size()];
		res = list.toArray(res);
		return res;
	}

	// this function takes in a word and checks if it has a suffix or not, if it
	// does it returns the non suffix and the suffix, O(n) time complexity
	private String[] getLongestMatchingSuffixAndNonPair(String word) {
		int wordLen = word.length();
		if (wordLen < 2)
			return new String[] { word, null };
		// so basically if word is fearsome it would start looking at earsome and keep
		// decreasing letters till it find it
		int currMaxSuffixLen = wordLen - 1;
		while (true) {
			if (currMaxSuffixLen == 0)
				break;
			String currSuffix = word.substring(wordLen - currMaxSuffixLen);
			String currNonSuffix = word.substring(0, wordLen - currSuffix.length());
			if (suffixMap.containsKey(currSuffix) && nonSuffixMap.containsKey(currNonSuffix)) {
				return new String[] { currNonSuffix, "@@" + currSuffix };
			}
			currMaxSuffixLen--;
		}
		return new String[] { word, null };
	}

	// matches each token to its encoding value, O(n) time complexity, one loop and
	// hashmap lookup is O(1)
	public int[] encode(String[] inputArr) {
		int[] res = new int[inputArr.length];
		for (int i = 0; i < inputArr.length; i++) {
			String token = inputArr[i];
			res[i] = encodingMap.get(token);
		}
		return res;
	}

	// same rationale as encode, O(n) time complexity, one loop and hashmap lookup
	// is O(1)
	public String[] decode(int[] inputArr) {
		String[] res = new String[inputArr.length];
		for (int i = 0; i < inputArr.length; i++) {
			int key = inputArr[i];
			res[i] = decodingMap.get(key);
		}
		return res;
	}

	// takes in the tokens array and convert it back to its original form, O(n) time
	// complexity, one loop and string builder is O(1)
	public String wordStringer(String[] strs) {

		StringBuilder res = new StringBuilder();
		for (String s : strs) {
			// check if it's a suffix
			if (s.startsWith("@@")) {
				res.append(s.substring(2));
			} else {
				res.append(s);
			}
		}
		return res.toString();
	}
	// //testing function, just to see if everything works as expected
	// public static void main(String[] args) {
	// EncoderDecoder test = new EncoderDecoder();
	// String sentence = "You are your child's best teacher, so set an example through your own behaviour.";
	// String[] arr = test.wordBreaker(sentence);
	// for (String s : arr) {
	// System.out.print(s);
	// }
	// System.out.println();
	// int[] encoded = test.encode(arr);
	// for (int el : encoded) {
	// System.out.print(el);
	// }
	// System.out.println();
	// String decoded = test.wordStringer(test.decode(encoded));
	// System.out.println(decoded);

	// }
}
