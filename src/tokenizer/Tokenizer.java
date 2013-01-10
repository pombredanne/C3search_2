package tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	Token[] tokens;

	public Token[] tokennize(String s, int sortType, int targetType,int minSize,
			String[] filterStr) {
		String[] splitedStr = splitCodeAndComment(s);
		String codeStr = splitedStr[0];
		String commentStr = splitedStr[1];
		List<String> filter = Arrays.asList(filterStr);
		Map<String, Integer> tokenMap = new HashMap<String, Integer>();
		Pattern tokenPattern = Pattern.compile("\\W*([a-zA-Z_]\\w*)\\W*");
		String targetStr = targetType == 0 ? codeStr
				: (targetType == 1 ? commentStr : s);
		// Scanner scanner = new Scanner(targetStr);
		boolean strFlag = false;
		StringBuffer nextBuffer = new StringBuffer("");
		String next = "";
		
		for (int i = 0; i <= targetStr.length(); i++) {
			// while (scanner.hasNext()) {
			char nextc = i==targetStr.length()?' ':targetStr.charAt(i);
			if (isDec(nextc) || isWord(nextc)) {
				strFlag = true;
				nextBuffer.append(nextc);
			} else if (nextBuffer.length() > 0 && strFlag) {

				strFlag = false;
				next = nextBuffer.toString();
				nextBuffer = new StringBuffer("");
				Matcher matcher = tokenPattern.matcher(next);
				if (next.length()<=minSize||filter.contains(next) || !matcher.matches()) {

					continue;
				}
				next = matcher.group(1);
				if (tokenMap.containsKey(next)) {
					int nextv = ((Integer) tokenMap.get(next)) + 1;
					tokenMap.put(next, nextv);
				} else {
					tokenMap.put(next, 1);
				}
				next = "";

			}
		}
		tokens = new Token[tokenMap.size()];
		int index = 0;

		if (sortType == 0) {
			for (Entry<String, Integer> entry : tokenMap.entrySet()) {
				tokens[index++] = new TokenSortByFrequency(entry.getKey(),
						entry.getValue());
			}
		} else if (sortType == 1) {
			for (Entry<String, Integer> entry : tokenMap.entrySet()) {
				tokens[index++] = new TokenSortByTfidf(entry.getKey(),
						entry.getValue());
			}
		}
		Arrays.sort(tokens);
		// scanner.close();
		return tokens;
	}

	private boolean isWord(char chr) {
		if ((chr >= 65 && chr <= 90) || chr >= 97 && chr <= 122 || chr == 95)
			return true;
		return false;
	}

	private boolean isDec(char chr) {
		if (chr >= 48 && chr <= 57)
			return true;
		return false;
	}

	public String[] splitCodeAndComment(String s) {
		// TODO Auto-generated method stub
		// defect /* /* */ */ may cause problem

		StringBuffer comment = new StringBuffer("");
		StringBuffer code = new StringBuffer("");
		boolean flag1 = false;// for //
		boolean flag2 = false;// for /*
		char thisChar = '\0';
		char preChar = '\0';
		for (int i = 0; i < s.length(); i++) {
			thisChar = s.charAt(i);

			if (thisChar == '/' && preChar == '/' && !flag2)
				flag1 = true;
			if (thisChar == '*' && preChar == '/' && !flag1)
				flag2 = true;
			if (thisChar == '/' && preChar == '*' && !flag1)
				flag2 = false;
			if (thisChar == '\n' && !flag2)
				flag1 = false;
			if (flag1 || flag2) {
				comment.append(thisChar);
			} else {
				code.append(thisChar);
			}
			preChar = thisChar;
		}
		return new String[] { code.toString(), comment.toString() };
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		for (Token token : tokens) {
			sb.append(token.toString());
		}
		return sb.toString();
	}
	public void clear(){
		tokens=null;
	}

}
