import java.awt.Color;
import java.awt.Component;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;

import myUIUtils.CheckNode;
import myUIUtils.MyJCheckBoxTree;
import searcher.ResultInfo;

public class OpenCCFinderCmd {

	private OpenCCFinder ocf;
	private TextArea textArea;
	private TextArea logText;
	private JTree resultsTree;
	private ConfigDialog config;
	private CheckNode root = new CheckNode("Results");
	private JTextField textField_1;
	private JCheckBox chckbxUseFilename = new JCheckBox();
	private String inputFile;
	private String logFile;

	// private ArrayList<CheckNode> nodes = new ArrayList<CheckNode>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		 * EventQueue.invokeLater(new Runnable() { public void run() { try {
		 * OpenCCFinderCmd window = new OpenCCFinderCmd();
		 * window.frmCSearchV.setVisible(true); } catch (Exception e) {
		 * e.printStackTrace(); } } });
		 */

		OpenCCFinderCmd ocfc = new OpenCCFinderCmd();
		ocfc.inputFile = args[0];
		ocfc.logFile = args[1];
		ocfc.openFile();
		for (int i = 0; i < ocfc.config.getIterationNumber(); i++) {

			ocfc.search();
			if (ocfc.config.getFrequencyOrRandom() == 1) {
				ocfc.shuffle();
			}
		}
		ocfc.analyze();
		ocfc.exportLog();

	}

	private void exportLog() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(logText.getText());
		String log = "";
		String result = DateFormat.getTimeInstance().format(new Date()) + "\t"
				+ inputFile;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("#")) {
				log += line;
			}
		}
		System.out.println(log);
		Pattern p = Pattern
				.compile("#All candidates number:(\\d+) \\((\\d+) duplicated files included\\) failed:(\\d+)\\. Available candidates number:(\\d+)#(\\d+) files contain code clone\\.#license types: (\\d+) copyrightTypes: (\\d+)");
		Matcher m = p.matcher(log);
		if (m.matches()) {
			result += ("\t" + m.group(1) + "\t" + m.group(2) + "\t"
					+ m.group(3) + "\t" + m.group(4) + "\t" + m.group(5) + "\t"
					+ m.group(6) + "\t" + m.group(7)+"\n");
		}
		System.out.print(result);
		File file = new File(logFile);
		try {
			FileWriter fw;
			
			String initStr = "Time\tFile\tAllCandidates\tDuplicated\tDownloadFailed\tAvailableFile\tFilesThatContainCodeClone\tDifferentLicenses\tDifferentCopyrights\n";
			if (!file.exists()) {
				fw = new FileWriter(file);
				
				fw.write(initStr);
				fw.close();
			}
			
			fw = new FileWriter(file,true);
			fw.write(result);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */
	public OpenCCFinderCmd() {
		// initialize();

		config = new ConfigDialog();
		ocf = new OpenCCFinder();

		config.readConfigFile();
		config.readConfigFile2();
		chckbxUseFilename.setSelected(config.isUsingFilename());

		initialize();

	}

	public void openFile() {
		String codeString = "";
		File file = new File(inputFile);
		try {
			Scanner scanner = new Scanner(new File(file.getAbsolutePath()));
			logText.append("Open file: " + file.getAbsolutePath() + "\n");
			while (scanner.hasNextLine()) {
				codeString += scanner.nextLine() + "\n";
			}
			textArea.setText(codeString);
			logText.append("Tokenize...");
			ocf.setQueryContent(textArea.getText());
			ocf.tokenize(config);
			config.setLanguage(file.getName().split("\\.")[1]);
			textField_1.setText(file.getName());
			logText.append("ok.\n");
		} catch (IOException ex) {
			logText.append("problem accessing file" + file.getAbsolutePath());
		}

	}

	public void shuffle() {
		if (ocf.getKeywords() != null && ocf.getKeywords().length > 0) {
			ocf.shuffleTokens();
			// tm = new MyDefaultTableModel(ocf.getKeywordsStr(),
			// columnNames);
			// table.setModel(tm);
			logText.append("Keyword rank shuffled.\n");
		} else {
			logText.append("No keywords.\n");
		}
	}

	public void search() {
		// logText.append("Current top keywords:\n");
		// logText.append(ocf.getTopKeywordsStrs(config.getMaxKeywordNum()));
		logText.append(DateFormat.getTimeInstance().format(new Date()) + "\n");
		logText.append("Searching...\n");
		ocf.doQuery(config, config.isUsingFilename() ? textField_1.getText()
				: "", true);
		logText.append("Finish.\n");
		logText.append(DateFormat.getTimeInstance().format(new Date()) + "\n");
		updateJtree(ocf.getResultsMap(), resultsTree);
	}

	public void analyze() {
		if (root.getLeafCount() <= 1) {
			logText.append("No cadidates.\n");
		} else {
			logText.append("Current configuration:\n");
			logText.append(config.getConfigInfo());
			ocf.analyse(textArea.getText(), root, config);
			// logText.append("Analyzing ccfinder result...\n");
			ExportAutomatically();
			// logText.append("ok.\n");
		}
	}

	private void initialize() {
		textArea = new TextArea();
		// panel_4.add(textArea);
		textArea.setText("Input query code here.");

		chckbxUseFilename = new JCheckBox("use filename");
		textField_1 = new JTextField();
		resultsTree = new JTree(root);

		logText = new TextArea();
		logText.setBackground(Color.WHITE);
		logText.setEditable(false);
		ocf.setLogText(logText);
	}

	protected void ExportAutomatically() {
		// TODO Auto-generated method stub
		String content = ocf.getResultContent();
		// logText.append("Save file: "
		// + file.getAbsolutePath() + "\n");
		FileWriter fw;
		try {
			File file = new File("./log/"
					+ textField_1.getText().split("\\.")[0] + "-result.txt");
			fw = new FileWriter(file);
			fw.write(content);
			fw.close();
			File file2 = new File("./log/"
					+ textField_1.getText().split("\\.")[0] + "-log.txt");
			fw = new FileWriter(file2);
			content = logText.getText();
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private void updateJtree(HashMap<String, ArrayList<ResultInfo>> resultsMap,
			JTree resultsTree2) {

		// resultsTree.removeAll();
		clearNodeData(root);
		root = new CheckNode("Results");
		resultsTree = new MyJCheckBoxTree(root);
		Iterator<Entry<String, ArrayList<ResultInfo>>> iter = resultsMap
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ArrayList<ResultInfo>> entry = iter.next();
			String key = (String) entry.getKey();

			ArrayList<ResultInfo> values = resultsMap.get(key);
			// System.out.println("\nkeyÅF" + key);
			CheckNode queryNode = new CheckNode(key);
			// nodes.add(queryNode);
			// int l = nodes.size();
			root.add(queryNode);
			Iterator<ResultInfo> iter2 = values.iterator();
			while (iter2.hasNext()) {
				ResultInfo resultInfo = iter2.next();
				CheckNode urlNode = new CheckNode(resultInfo);
				// nodes.add(urlNode);
				// System.out.println(resultInfo.pckg + resultInfo.path);
				// System.out.println(resultInfo.url);
				// int m = nodes.size();
				queryNode.add(urlNode);
				// nodes.get(l-1).add(nodes.get(m-1));
			}
		}

		// resultsTree2.repaint();
		// TODO Auto-generated method stub

	}

	private void clearNodeData(CheckNode parent) {
		for (int i = 0; i < parent.getChildCount(); ++i) {
			CheckNode node = (CheckNode) parent.getChildAt(i);
			clearNodeData(node);
		}
		parent.removeAllChildren();
	}
}
