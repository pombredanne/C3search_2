import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.TextField;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import java.awt.TextArea;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.ScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Label;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTree;

import myUIUtils.CheckNode;
import myUIUtils.MyJCheckBoxTree;

import searcher.ResultInfo;

import java.awt.Button;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Choice;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class AppWin {

	private OpenCCFinder ocf;
	private JFrame frmCSearchV;
	private JTable table;
	private TextArea textArea;
	private TextArea logText;
	private JTree resultsTree;
	private JPanel panel_1;
	private JScrollPane treesp;
	private TableModel tm;
	private TableModel tm2;
	private String[] columnNames = { "keyword", "Frequency"/* , "Tfidf" */};
	private String[] columnNames2 = { "rank", "searchEngine", "url", "Path",
			"license", "copyRight", "lineNumber", "coverRatio",
			"lastModifyTime" };
	private ConfigDialog config;
	private CodeDetailDialog cdd;
	private CheckNode root = new CheckNode("Results");
	private JTable table_1;
	private JTextField textField_1;
	private JCheckBox chckbxUseFilename;

	// private ArrayList<CheckNode> nodes = new ArrayList<CheckNode>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWin window = new AppWin();
					window.frmCSearchV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppWin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	class MyDefaultTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyDefaultTableModel(Object[][] objects, String[] strings) {
			super(objects, strings);
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}

	private void initialize() {
		config = new ConfigDialog();
		cdd = new CodeDetailDialog();
		config.readConfigFile();
		ocf = new OpenCCFinder();
		tm = new MyDefaultTableModel(new String[][] { { "", ""/* , "" */},

		}, columnNames);
		tm2 = new MyDefaultTableModel(new String[][] { { "", "", "", "", "",
				"", "", "", "" },

		}, columnNames);

		frmCSearchV = new JFrame();
		frmCSearchV.setTitle("OpenCCFinder");
		frmCSearchV.setBounds(100, 100, 800, 640);
		frmCSearchV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();

		frmCSearchV.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("OpenFile");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String codeString = "";
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						// What to do with the file, e.g. display it in a
						// TextArea
						Scanner scanner = new Scanner(new File(file
								.getAbsolutePath()));
						logText.append("Open file: " + file.getAbsolutePath()
								+ "\n");
						while (scanner.hasNextLine()) {
							codeString += scanner.nextLine() + "\n";
						}
						textArea.setText(codeString);
						logText.append("Tokenize...");
						ocf.setQueryContent(textArea.getText());
						ocf.tokenize(config);
						tm = new MyDefaultTableModel(ocf.getKeywordsStr(),
								columnNames);
						table.setModel(tm);
						config.setLanguage(file.getName().split("\\.")[1]);
						textField_1.setText(file.getName());
						logText.append("ok.\n");
					} catch (IOException ex) {
						logText.append("problem accessing file"
								+ file.getAbsolutePath());
					}
				} else {
					logText.append("File access cancelled by user.\n");
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmNewMenuItem = new JMenuItem("Export results");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.exists()) {
						try {
							// What to do with the file, e.g. display it in a
							// TextArea
							String content = ocf.getResultContent();
							logText.append("Save file: "
									+ file.getAbsolutePath() + "\n");
							FileWriter fw = new FileWriter(file);
							fw.write(content);
							fw.close();
							logText.append("ok.\n");

						} catch (IOException ex) {
							logText.append("problem saving file"
									+ file.getAbsolutePath()+"\n");
						}
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "File exists:"+file.getAbsolutePath());
						logText.append("Export failed:\nFile "+file.getAbsolutePath()+" already exists\n");
						
					}
				}else{
					logText.append("File save cancelled by user.\n");
				}
			}
		});
		mnFile.add(mntmNewMenuItem);

		JMenu mnConfig = new JMenu("config");
		menuBar.add(mnConfig);

		JMenuItem mntmTokenize = new JMenuItem("Config");
		mntmTokenize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				config.setVisible(true);
			}
		});
		mnConfig.add(mntmTokenize);

		JMenu mnNewMenu = new JMenu("clear");
		menuBar.add(mnNewMenu);

		JMenuItem mntmReset = new JMenuItem("Clear");
		mnNewMenu.add(mntmReset);
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ocf.clear();
				clearNodeData(root);
				root = new CheckNode("Results");
				textArea.setText("");
				table_1.setModel(new MyDefaultTableModel(new Object[][] {},
						new String[] { "rank", "searchEngine", "url", "Path",
								"license", "copyRight", "lineNumber",
								"coverRatio", "lastModifyTime" }));
				tm = new MyDefaultTableModel(new Object[][] {}, new String[] {
						"keyword", "Frequency" });
				table.setModel(tm);
				textField_1.setText("");
				logText.append("Clear.\n");
				updateJtree(ocf.getResultsMap(), resultsTree);
			}
		});

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmCSearchV.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setToolTipText("Tips here");
		tabbedPane.addTab("query", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		textArea = new TextArea();
		panel_4.add(textArea);
		textArea.setText("Input query code here.");

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_5, BorderLayout.SOUTH);

		JButton btnTokenize_1 = new JButton("Tokenize");
		btnTokenize_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logText.append("Tokenize...\n");
				ocf.setQueryContent(textArea.getText());
				ocf.tokenize(config);
				tm = new MyDefaultTableModel(ocf.getKeywordsStr(), columnNames);
				table.setModel(tm);
				logText.append("ok.\n");
			}
		});
		btnTokenize_1.setHorizontalAlignment(SwingConstants.LEFT);
		panel_5.add(btnTokenize_1);

		Label label_1 = new Label("query code");
		panel_4.add(label_1, BorderLayout.NORTH);

		Panel panel_2 = new Panel();
		panel.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 0));

		table = new JTable();
		table.setToolTipText("Double click to move keyword to top; Right click to bottom.");
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent me) {
				String temp = "";
				int selectedRow = table.getSelectedRow();
				if(me.getButton()==MouseEvent.BUTTON1&&selectedRow>=0){
					if(me.getClickCount()==2){
						logText.append("Keyword rank changed: \""+table.getValueAt(selectedRow, 0).toString()+"\" to top.\n");
						ocf.adjustKeywordToTop(selectedRow);
						tm = new MyDefaultTableModel(ocf.getKeywordsStr(),
								columnNames);
						table.setModel(tm);
						
					}
				}else
				if(me.getButton()==MouseEvent.BUTTON3&&selectedRow>=0){
					logText.append("Keyword rank changed: \""+table.getValueAt(selectedRow, 0).toString()+"\" to bottom.\n");
					ocf.adjustKeywordToBottom(selectedRow);
					tm = new MyDefaultTableModel(ocf.getKeywordsStr(),
							columnNames);
					table.setModel(tm);

				}
				
			}
			
		});
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(250, 70));
		panel_2.add(sp);
		table.setFillsViewportHeight(true);
		table.setModel(new MyDefaultTableModel(new Object[][] {}, new String[] {
				"keyword", "Frequency"/* , "Tfidf" */
		}));

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		Label label = new Label("Search attributes");
		panel_2.add(label, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logText.append("Current top keywords:\n");
				logText.append(ocf.getTopKeywordsStrs(config.getMaxKeywordNum()));
				logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
				logText.append("Searching...\n");
				ocf.doQuery(config,
						chckbxUseFilename.isSelected() ? textField_1.getText()
								: "",false);
				logText.append("Finish.\n");
				logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
				updateJtree(ocf.getResultsMap(), resultsTree);
				// panel_1.requestFocus();
				// panel_1.grabFocus();
			}

		});

		JButton btnShuffle = new JButton("Shuffle keyword");
		btnShuffle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ocf.getKeywords() != null && ocf.getKeywords().length > 0) {
					ocf.shuffleTokens();
					tm = new MyDefaultTableModel(ocf.getKeywordsStr(),
							columnNames);
					table.setModel(tm);
					logText.append("Keyword rank shuffled.\n");
				} else {
					logText.append("No keywords.\n");
				}
			}
		});

		chckbxUseFilename = new JCheckBox("use filename");
		chckbxUseFilename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField_1.setEditable(chckbxUseFilename.isSelected());
			}
		});

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setEditable(false);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3
				.setHorizontalGroup(gl_panel_3
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel_3
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panel_3
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																textField_1,
																GroupLayout.DEFAULT_SIZE,
																127,
																Short.MAX_VALUE)
														.addGroup(
																gl_panel_3
																		.createSequentialGroup()
																		.addComponent(
																				btnShuffle,
																				GroupLayout.DEFAULT_SIZE,
																				131,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_panel_3
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																chckbxUseFilename)
														.addComponent(
																btnNewButton,
																GroupLayout.PREFERRED_SIZE,
																85,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		gl_panel_3
				.setVerticalGroup(gl_panel_3
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel_3
										.createSequentialGroup()
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												gl_panel_3
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																textField_1,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chckbxUseFilename))
										.addGap(8)
										.addGroup(
												gl_panel_3
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																btnShuffle)
														.addComponent(
																btnNewButton))
										.addContainerGap()));
		panel_3.setLayout(gl_panel_3);

		panel_1 = new JPanel();
		tabbedPane.addTab("Cadidates", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

		resultsTree = new JTree(root);
		treesp = new JScrollPane(resultsTree);
		panel_1.add(treesp);

		Panel panel_8 = new Panel();
		panel_1.add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Analyze");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (root.getLeafCount() <= 1) {
					logText.append("No cadidates.\n");
				} else {
					logText.append("Current configuration:\n");
					logText.append(config.getConfigInfo());
					ocf.analyse(textArea.getText(), root, config);
					// logText.append("Analyzing ccfinder result...\n");
					tm2 = new MyDefaultTableModel(ocf.getResultTableStr(),
							columnNames2);
					table_1.setModel(tm2);
					ExportAutomatically();
					// logText.append("ok.\n");
				}
			}

		});
		panel_8.add(btnNewButton_1, BorderLayout.WEST);

		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("results", null, panel_6, null);
		panel_6.setLayout(new BorderLayout(0, 0));
		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedColumn = table_1.getSelectedRow();
					String filename = (String) table_1.getValueAt(
							selectedColumn, 3);
					cdd.getTextInfo(filename);
					cdd.updateCodeInfo(filename);
					cdd.setVisible(true);
				}
			}

		});
		table_1.setToolTipText("Double Click to view source codes");
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// table_1.setEnabled(true);
		// table_1.setFocusable(true);
		table_1.setModel(new MyDefaultTableModel(new Object[][] {},
				new String[] { "rank", "searchEngine", "url", "Path",
						"license", "copyRight", "lineNumber", "coverRatio",
						"lastModifyTime" }));
		JScrollPane scrollPane = new JScrollPane(table_1);
		panel_6.add(scrollPane, BorderLayout.CENTER);

		JPanel panel_7 = new JPanel();
		frmCSearchV.getContentPane().add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));

		logText = new TextArea();
		logText.setBackground(Color.WHITE);
		logText.setEditable(false);
		panel_7.add(logText, BorderLayout.CENTER);
		ocf.setLogText(logText);

		TextField textField = new TextField();
		textField.setEditable(false);
		panel_7.add(textField, BorderLayout.SOUTH);

		JToolBar toolBar = new JToolBar();
		panel_7.add(toolBar, BorderLayout.NORTH);

		JLabel lblLog = new JLabel("Log   ");
		toolBar.add(lblLog);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logText.setText("");
			}
		});
		toolBar.add(btnClear);

		JButton btnSaveAsTxt = new JButton("Save as...");
		btnSaveAsTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.exists()) {
						try {
							// What to do with the file, e.g. display it in a
							// TextArea
							String content = logText.getText();
							logText.append("Save file: "
									+ file.getAbsolutePath() + "\n");
							FileWriter fw = new FileWriter(file);
							fw.write(content);
							fw.close();
							logText.append("ok.\n");

						} catch (IOException ex) {
							logText.append("problem saving file"
									+ file.getAbsolutePath()+"\n");
						}
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "File exists:"+file.getAbsolutePath());
						logText.append("Export failed:\nFile "+file.getAbsolutePath()+" already exists\n");
						
					}
				}else{
					logText.append("File save cancelled by user.\n");
				}
			}
		});
		toolBar.add(btnSaveAsTxt);

	}

	protected void ExportAutomatically() {
		// TODO Auto-generated method stub
		String content = ocf.getResultContent();
		//logText.append("Save file: "
		//		+ file.getAbsolutePath() + "\n");
		FileWriter fw;
		try {
			fw = new FileWriter(new File("./log/"+textField_1.getText().split("\\.")[0]+"-result"));
			fw.write(content);
			fw.close();
			fw = new FileWriter(new File("./log/"+textField_1.getText().split("\\.")[0]+"-log"));
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
		panel_1.remove(treesp);
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
		treesp = new JScrollPane(resultsTree);

		panel_1.add(treesp);
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
