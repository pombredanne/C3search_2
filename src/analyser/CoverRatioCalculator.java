package analyser;

import java.awt.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import searcher.ResultInfo;

public class CoverRatioCalculator {
	public static void main(String[] args) {
		// CoverRatioCalculator crc = new CoverRatioCalculator();
		// ArrayList<ResultInfo> finalResults = new ArrayList<ResultInfo>();
		// crc.getCoverRatio(finalResults);
	}

	public void getCoverRatio(TextArea logText, ArrayList<ResultInfo> finalResults) {
		boolean flag1 = false;// #begin{file description}
		boolean flag2 = false;// #begin{set}"
		
		boolean[][] coverFlags = null;
		int count = 0;
		int countClone=0;
		try {
			Scanner scanner = new Scanner(new File("./ccfinderoutput.txt"));
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#begin{file description}"))
					flag1 = true;
				if (line.startsWith("#end{file description}"))
					flag1 = false;
				if (line.startsWith("#begin{set}"))
					flag2 = true;
				if (line.startsWith("#end{set}")) {
					
						for (int i = 0; i < finalResults.get(0).tokenNumber; i++) {

							coverFlags[0][i] = false;

						}
					
					flag2 = false;
				}
				
				if (line.startsWith("#")) {
					continue;
				}
				String[] lineinfo = line.split("\t");
				if (flag1) {
					count++;
					if (count > finalResults.size())
						throw new Exception("Wrong results!");

					ResultInfo ri = finalResults.get(count - 1);
					ri.lineNumber = Integer.valueOf(lineinfo[1]);
					ri.tokenNumber = Integer.valueOf(lineinfo[2]);
					finalResults.set(count - 1, ri);
					continue;
				}
				if (coverFlags == null) {
					coverFlags = new boolean[count][finalResults.get(0).tokenNumber];
					for (int i = 0; i < count; i++) {
						Arrays.fill(coverFlags[i], false);
					}
				}
				if (flag2) {
					if (line.startsWith("0.0")) {
						int startToken = Integer
								.valueOf(lineinfo[1].split(",")[2]);
						int endToken = Integer
								.valueOf(lineinfo[2].split(",")[2]);

						for (int i = startToken; i < endToken; i++) {

							coverFlags[0][i] = true;

						}
					} else {
						int index = Integer
								.valueOf(lineinfo[0].split("\\.")[1]);
						for (int i = 0; i < finalResults.get(0).tokenNumber; i++) {

						
								coverFlags[index][i] |= coverFlags[0][i];
							

						}

					}
				} 
				/*
				 * if (flag3) { if (line.startsWith("0.0")) { continue; } else {
				 * int index = Integer .valueOf(lineinfo[0].split("\\.")[1]);
				 * 
				 * int startToken = Integer .valueOf(lineinfo[1].split(",")[2]);
				 * int endToken = Integer .valueOf(lineinfo[2].split(",")[2]);
				 * 
				 * for (int i = startToken; i < endToken; i++) {
				 * 
				 * coverFlags[index][i] = true;
				 * 
				 * } }
				 * 
				 * }
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int sum = 0;
		int n = finalResults.get(0).tokenNumber;

		for (int i = 0; i < count; i++) {

			for (int j = 0; j < n; j++) {
				//
				if (coverFlags[i][j]) {
					sum++;

				}
			}
			ResultInfo ri = finalResults.get(i);
			ri.coverToken = sum;
			if(sum>0)countClone++;
			finalResults.set(i, ri);
			sum = 0;
			
		}
		logText.append("#"+countClone+" files contain code clone.\n");
	}
}
