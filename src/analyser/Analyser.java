package analyser;

import java.awt.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ccfindertools.CCFinder;

import searcher.ResultInfo;

import myUIUtils.CheckNode;

public class Analyser {

	private String requestText;
	private CheckNode root;
	private FileDownloader filedownloader;
	private String filelist;

	public Analyser() {

		filedownloader = new FileDownloader();
		File fdir = new File("./cache");
		if (!fdir.exists())
			fdir.mkdir();

	}

	public void setRequestText(String text) {
		// TODO Auto-generated method stub
		this.requestText = text;
	}

	public void setUrlTreeRoot(CheckNode root) {
		// TODO Auto-generated method stub
		this.root = root;
	}

	public void downloadFiles(TextArea logText,
			ArrayList<ResultInfo> finalResults) {
		if (root == null)
			return;
		// TODO Auto-generated method stub
		finalResults.add(new ResultInfo());
		filelist = "";
		int leafcount = 0;
		int dupcount = 0;
		int failedcount = 0;
		for (int i = 0; i < root.getChildCount(); ++i) {
			CheckNode queryNode = (CheckNode) root.getChildAt(i);
			int count = queryNode.getChildCount();

			for (int j = 0; j < count; ++j) {
				CheckNode urlNode = (CheckNode) queryNode.getChildAt(j);
				String url = urlNode.toString();
				logText.append((j + 1) + "/" + count + " of cadidates set "
						+ (i + 1) + "...");
				leafcount++;
				System.out.print(leafcount+" ");
				if (urlNode.isSelected()) {
					// httpdownload(urlNode);
					String filename = filedownloader.transfer(urlNode
							.toString());
					File file = new File("./cache/" + filename);

					if (file.exists()) {

						try {
							String filepath = file.getCanonicalPath();
							if (!filelist.contains(filepath)) {
								logText.append("File cached.");
								filelist += (file.getCanonicalPath() + "\n");
								finalResults.add((ResultInfo) urlNode
										.getUserObject());
							} else {
								logText.append("File duplicated.");
								dupcount++;
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						String downloadresult = filedownloader
								.httpdownload((ResultInfo) urlNode
										.getUserObject());

						if (downloadresult.equals("success!")) {

							try {
								String filepath = file.getCanonicalPath();
								if (!filelist.contains(filepath)) {
									logText.append("success!");
									filelist += (file.getCanonicalPath() + "\n");
									finalResults.add((ResultInfo) urlNode
											.getUserObject());
								} else {
									logText.append("File duplicated.");
									dupcount++;
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							logText.append("failed!");
							failedcount++;
						}
					}
				} else {
					logText.append("passed.");
				}
				logText.append(": download from " + url + "\n");
			}
		}
		System.out.println();
		logText.append("#All candidates number:" + leafcount + " (" + dupcount
				+ " duplicated files included)" + " failed:" + failedcount
				+ ". Available candidates number:"
				+ (finalResults.size() - 1) + "\n");
		if (filelist.length() > 0) {
			PrintWriter writer;
			try {
				File querycodeFile = new File("./cache/" + "querycode");
				writer = new PrintWriter(querycodeFile);
				writer.print(requestText);
				filelist = (querycodeFile.getCanonicalPath() + "\n") + filelist;
				writer.close();
				writer = new PrintWriter(new File("./ccfinderinput.txt"));
				writer.print(filelist);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void runCCFinder(String language, int minToken) {

		CCFinder ccfinder = new CCFinder(language, "", "-b " + minToken,
				new File("./ccfinderinput.txt"), new File(
						"./ccfinderoutput.txt"), true, true, true, System.out);
		ccfinder.run();
	}

	public void analyseCCFinderResult(TextArea logText,
			ArrayList<ResultInfo> finalResults) {
		CoverRatioCalculator crc = new CoverRatioCalculator();

		crc.getCoverRatio(logText, finalResults);

	}

	public void checkCopyrightAndLastModifiedTime(
			ArrayList<ResultInfo> finalResults, TextArea logText) {
		CopyRightExtractor ce = new CopyRightExtractor();
		TimeExtractor te = new TimeExtractor();
		logText.append("all " + (finalResults.size() - 1) + ":");
		Set<String> licenses = new HashSet<String>();
		Set<String> copyrights = new HashSet<String>();
		for (int i = 1; i < finalResults.size(); i++) {
			
			ResultInfo ri = finalResults.get(i);
			if(!ri.license.equals("NotSure")){
				licenses.add(ri.license);
			}
			if (ri.coverToken > 0) {
				String filename = filedownloader.transfer(ri.toString());
				String copyright = ce.extractCopyRigh(filename);

				if (copyright.length() > 0){
					ri.copyright = copyright;
					copyrights.add(copyright);
				}
				ri.lastModifyTime = te.ExtractTime(ri);

				finalResults.set(i, ri);

			} else {
				ri.lastModifyTime = "Don't Care";
				ri.copyright = "Don't Care";
				finalResults.set(i, ri);
			}
			logText.append(i + " ");
			System.out.print(i+" ");
		}
		logText.append("\n");
		System.out.print("\n");
		logText.append("#license types: "+licenses.size()+" copyrightTypes: "+copyrights.size()+"\n");

	}

}
