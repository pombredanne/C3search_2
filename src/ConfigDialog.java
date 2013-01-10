import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.TextArea;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ConfigDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	public static final int CODE = 0;
	public static final int COMMENT = 1;
	public static final int CODE_AND_COMMENT = 2;
	public static final int FRENQUENCY = 0;
	public static final int TFIDF = 1;
	public static final int LANG_JAVA=0;
	public static final int LANG_C=1;
	public static final int LANG_CPP=2;
	public final String[] LANGUAGE = {"java","c","c"/*cpp is c?*/};
	
	private TextArea textArea;
	private String configStr = "";
	private int targetType = CODE;
	private int sortType = FRENQUENCY;
	private int minSize = 2;
	private int frequencyOrRandom=0;
	private int iterationNumber = 5;
	
	private boolean googleSelected=true;
	private boolean searchcoSelected=true;
	private boolean githubSelected=true;
	
	private int maxResultNum=50;
	private int maxKeywordNum=10;
	private boolean usingFilename = false;
	private int language=0;
	
	private final JRadioButton rdbtnCode;
	private final JRadioButton rdbtnComment;
	private final JRadioButton rdbtnCodecomment;
	private final JRadioButton rdbtnFrequency;
	private final JRadioButton rdbtnTfidf;
	private final JSpinner spinner;
	private final JCheckBox chckbxGoogleCodeSearch;
	private final JCheckBox chckbxGithub;
	private final JCheckBox chckbxKoders;
	private final JSpinner spinner_1;
	private final JSpinner spinner_2;
	private final JComboBox comboBox;
	
	static String defaultFilter = "bool\nboolean\nbyte\nchar\nint\nlong\nshort\nfloat\ndouble\nprivate\nprotected\npublic\nabstract\nfinal\nnative\nstatic\ntry\ncatch\nfinal\nthrow\nbreak\ncase\ncontinue\ndefault\ndo\nwhile\nfor\nswitch\nif\nelse\nelseif\nendif\nifdef\nclass\nextends\nimport\nnew\nreturn\nvoid\nString\nstring\nsuper\ntrue\nfalse\nnull\nobject\ninclude\ntest\ntest2\nthis\nwrite\n";

	public static void main(String[] args) {
		try {
			ConfigDialog dialog = new ConfigDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigDialog() {

		
		setTitle("Config");
		setBounds(100, 100, 460, 400);
		getContentPane().setLayout(new BorderLayout());
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Tokenize", null, panel, null);
				panel.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, BorderLayout.WEST);
					GridBagLayout gbl_panel_1 = new GridBagLayout();
					gbl_panel_1.columnWidths = new int[] { 125, -9 };
					gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0 };
					gbl_panel_1.columnWeights = new double[] { 0.0,
							Double.MIN_VALUE };
					gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
							0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
					panel_1.setLayout(gbl_panel_1);
					JLabel lblNewLabel = new JLabel("Target");
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.gridheight = 2;
					gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
					gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
					gbc_lblNewLabel.gridx = 0;
					gbc_lblNewLabel.gridy = 0;
					panel_1.add(lblNewLabel, gbc_lblNewLabel);
					rdbtnCode = new JRadioButton("code");
					GridBagConstraints gbc_rdbtnCode = new GridBagConstraints();
					gbc_rdbtnCode.fill = GridBagConstraints.BOTH;
					gbc_rdbtnCode.insets = new Insets(0, 0, 5, 0);
					gbc_rdbtnCode.gridx = 0;
					gbc_rdbtnCode.gridy = 2;
					panel_1.add(rdbtnCode, gbc_rdbtnCode);
					rdbtnCode.setSelected(true);

					rdbtnComment = new JRadioButton("comment");
					GridBagConstraints gbc_rdbtnComment = new GridBagConstraints();
					gbc_rdbtnComment.fill = GridBagConstraints.BOTH;
					gbc_rdbtnComment.insets = new Insets(0, 0, 5, 0);
					gbc_rdbtnComment.gridx = 0;
					gbc_rdbtnComment.gridy = 3;
					panel_1.add(rdbtnComment, gbc_rdbtnComment);

					rdbtnCodecomment = new JRadioButton("code&comment");
					GridBagConstraints gbc_rdbtnCodecomment = new GridBagConstraints();
					gbc_rdbtnCodecomment.fill = GridBagConstraints.BOTH;
					gbc_rdbtnCodecomment.insets = new Insets(0, 0, 5, 0);
					gbc_rdbtnCodecomment.gridx = 0;
					gbc_rdbtnCodecomment.gridy = 4;
					panel_1.add(rdbtnCodecomment, gbc_rdbtnCodecomment);
					{
						JLabel lblSortStrategy = new JLabel("Sort Strategy");
						GridBagConstraints gbc_lblSortStrategy = new GridBagConstraints();
						gbc_lblSortStrategy.fill = GridBagConstraints.BOTH;
						gbc_lblSortStrategy.insets = new Insets(0, 0, 5, 0);
						gbc_lblSortStrategy.gridx = 0;
						gbc_lblSortStrategy.gridy = 6;
						panel_1.add(lblSortStrategy, gbc_lblSortStrategy);
					}
					rdbtnFrequency = new JRadioButton("Frequency");
					GridBagConstraints gbc_rdbtnFrequency = new GridBagConstraints();
					gbc_rdbtnFrequency.fill = GridBagConstraints.BOTH;
					gbc_rdbtnFrequency.insets = new Insets(0, 0, 5, 0);
					gbc_rdbtnFrequency.gridx = 0;
					gbc_rdbtnFrequency.gridy = 7;
					panel_1.add(rdbtnFrequency, gbc_rdbtnFrequency);
					rdbtnFrequency.setSelected(true);
					rdbtnTfidf = new JRadioButton("Random");
					GridBagConstraints gbc_rdbtnTfidf = new GridBagConstraints();
					gbc_rdbtnTfidf.fill = GridBagConstraints.BOTH;
					gbc_rdbtnTfidf.insets = new Insets(0, 0, 5, 0);
					gbc_rdbtnTfidf.gridx = 0;
					gbc_rdbtnTfidf.gridy = 8;
					panel_1.add(rdbtnTfidf, gbc_rdbtnTfidf);
					ButtonGroup group1 = new ButtonGroup();
					ButtonGroup group2 = new ButtonGroup();
					group1.add(rdbtnCode);
					group1.add(rdbtnComment);
					group1.add(rdbtnCodecomment);

					JPanel panel_2 = new JPanel();
					panel.add(panel_2, BorderLayout.CENTER);
					panel_2.setLayout(new BorderLayout(0, 0));

					JLabel lblKeywordFilter = new JLabel("Keyword filter");
					panel_2.add(lblKeywordFilter, BorderLayout.NORTH);

					JPanel panel_3 = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
					flowLayout.setAlignment(FlowLayout.RIGHT);
					panel_2.add(panel_3, BorderLayout.SOUTH);

					JButton btnSave = new JButton("save");
					btnSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							configStr = textArea.getText();
							saveFile();
							JOptionPane.showMessageDialog(null, "Save ok.",
									"Message Dialog.",
									JOptionPane.INFORMATION_MESSAGE);
						}

						private void saveFile() {

							try {
								Writer writer = null;
								File f = new File("keywordFilter.txt");
								writer = new BufferedWriter(new FileWriter(f));
								writer.write(configStr);
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});
					panel_3.add(btnSave);

					textArea = new TextArea();
					panel_2.add(textArea, BorderLayout.CENTER);
					group2.add(rdbtnFrequency);
					group2.add(rdbtnTfidf);
					
					JLabel lblTokenSize = new JLabel("Min token length");
					GridBagConstraints gbc_lblTokenSize = new GridBagConstraints();
					gbc_lblTokenSize.insets = new Insets(0, 0, 5, 0);
					gbc_lblTokenSize.anchor = GridBagConstraints.WEST;
					gbc_lblTokenSize.gridx = 0;
					gbc_lblTokenSize.gridy = 10;
					panel_1.add(lblTokenSize, gbc_lblTokenSize);
					
					spinner = new JSpinner();
					spinner.setModel(new SpinnerNumberModel(2, 0, 10, 1));
					GridBagConstraints gbc_spinner = new GridBagConstraints();
					gbc_spinner.anchor = GridBagConstraints.WEST;
					gbc_spinner.insets = new Insets(0, 0, 5, 0);
					gbc_spinner.gridx = 0;
					gbc_spinner.gridy = 11;
					panel_1.add(spinner, gbc_spinner);

				}
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("search", null, panel, null);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
				gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
				gbl_panel.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				
				JLabel lblExtermSearchEngine = new JLabel("External search engine");
				GridBagConstraints gbc_lblExtermSearchEngine = new GridBagConstraints();
				gbc_lblExtermSearchEngine.anchor = GridBagConstraints.WEST;
				gbc_lblExtermSearchEngine.insets = new Insets(0, 0, 5, 5);
				gbc_lblExtermSearchEngine.gridx = 0;
				gbc_lblExtermSearchEngine.gridy = 0;
				panel.add(lblExtermSearchEngine, gbc_lblExtermSearchEngine);
				
				chckbxGithub = new JCheckBox("github");
				chckbxGithub.setSelected(true);
				GridBagConstraints gbc_chckbxSparse = new GridBagConstraints();
				gbc_chckbxSparse.anchor = GridBagConstraints.WEST;
				gbc_chckbxSparse.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxSparse.gridx = 0;
				gbc_chckbxSparse.gridy = 1;
				panel.add(chckbxGithub, gbc_chckbxSparse);
				
				chckbxGoogleCodeSearch = new JCheckBox("Google code search");
				chckbxGoogleCodeSearch.setSelected(true);
				GridBagConstraints gbc_chckbxGoogleCodeSearch = new GridBagConstraints();
				gbc_chckbxGoogleCodeSearch.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxGoogleCodeSearch.gridx = 1;
				gbc_chckbxGoogleCodeSearch.gridy = 1;
				panel.add(chckbxGoogleCodeSearch, gbc_chckbxGoogleCodeSearch);
				
				chckbxKoders = new JCheckBox("search[code]");
				chckbxKoders.setSelected(true);
				GridBagConstraints gbc_chckbxKoders = new GridBagConstraints();
				gbc_chckbxKoders.anchor = GridBagConstraints.WEST;
				gbc_chckbxKoders.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxKoders.gridx = 2;
				gbc_chckbxKoders.gridy = 1;
				panel.add(chckbxKoders, gbc_chckbxKoders);
				
				JLabel lblMaxResultNumber = new JLabel("Max result number");
				GridBagConstraints gbc_lblMaxResultNumber = new GridBagConstraints();
				gbc_lblMaxResultNumber.anchor = GridBagConstraints.WEST;
				gbc_lblMaxResultNumber.insets = new Insets(0, 0, 5, 5);
				gbc_lblMaxResultNumber.gridx = 0;
				gbc_lblMaxResultNumber.gridy = 2;
				panel.add(lblMaxResultNumber, gbc_lblMaxResultNumber);
				
				spinner_1 = new JSpinner();
				spinner_1.setModel(new SpinnerNumberModel(50, 0, 99, 10));
				GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
				gbc_spinner_1.anchor = GridBagConstraints.WEST;
				gbc_spinner_1.insets = new Insets(0, 0, 5, 5);
				gbc_spinner_1.gridx = 1;
				gbc_spinner_1.gridy = 2;
				panel.add(spinner_1, gbc_spinner_1);
				
				JLabel lblMaxKeywordNumber = new JLabel("Max keyword number");
				GridBagConstraints gbc_lblMaxKeywordNumber = new GridBagConstraints();
				gbc_lblMaxKeywordNumber.anchor = GridBagConstraints.WEST;
				gbc_lblMaxKeywordNumber.insets = new Insets(0, 0, 5, 5);
				gbc_lblMaxKeywordNumber.gridx = 0;
				gbc_lblMaxKeywordNumber.gridy = 3;
				panel.add(lblMaxKeywordNumber, gbc_lblMaxKeywordNumber);
				
				spinner_2 = new JSpinner();
				spinner_2.setModel(new SpinnerNumberModel(10, 1, 20, 1));
				GridBagConstraints gbc_spinner_2 = new GridBagConstraints();
				gbc_spinner_2.anchor = GridBagConstraints.WEST;
				gbc_spinner_2.insets = new Insets(0, 0, 5, 5);
				gbc_spinner_2.gridx = 1;
				gbc_spinner_2.gridy = 3;
				panel.add(spinner_2, gbc_spinner_2);
				
				JLabel lblLanguage = new JLabel("Language");
				GridBagConstraints gbc_lblLanguage = new GridBagConstraints();
				gbc_lblLanguage.anchor = GridBagConstraints.WEST;
				gbc_lblLanguage.insets = new Insets(0, 0, 5, 5);
				gbc_lblLanguage.gridx = 0;
				gbc_lblLanguage.gridy = 4;
				panel.add(lblLanguage, gbc_lblLanguage);
				
				comboBox = new JComboBox();
				comboBox.setModel(new DefaultComboBoxModel(new String[] {"Java", "C++", "C"}));
				comboBox.setSelectedIndex(0);
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.insets = new Insets(0, 0, 5, 5);
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 0;
				gbc_comboBox.gridy = 5;
				panel.add(comboBox, gbc_comboBox);
			}
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						targetType = rdbtnCode.isSelected()?0:(rdbtnComment.isSelected()?1:2);
						sortType = rdbtnFrequency.isSelected()?0:1;
						configStr = textArea.getText();
						minSize = (Integer) spinner.getValue();
						googleSelected = chckbxGoogleCodeSearch.isSelected();
						githubSelected = chckbxGithub.isSelected();
						searchcoSelected = chckbxKoders.isSelected();
						language = comboBox.getSelectedIndex();
						maxResultNum = (Integer) spinner_1.getValue();
						maxKeywordNum = (Integer) spinner_2.getValue();
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				// getRootPane().setDefaultButton(okButton);

				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void readConfigFile() {
		// TODO Auto-generated method stub
		try {
			// What to do with the file, e.g. display it in a TextArea
			File file = new File("keywordFilter.txt");
			if(!file.exists()){
				FileWriter fw = new FileWriter(file);
				fw.write(defaultFilter);
				fw.flush();
				fw.close();
			}
			Scanner scanner = new Scanner(new File("keywordFilter.txt"));
			while (scanner.hasNextLine()) {
				configStr += scanner.nextLine() + "\n";
			}
			textArea.setText(configStr);

		} catch (IOException ex) {
			System.out.println("problem accessing file keywordFilter.txt");
		}

	}

	public String[] getConfigStr() {
		return configStr.split("\n");
	}

	public int getTargetType() {
		return targetType;
	}

	public int getSortType() {
		return sortType;
	}

	public int getMinSize() {
		return minSize;
	}

	public boolean isGoogleSelected() {
		return googleSelected;
	}

	public void setGoogleSelected(boolean googleSelected) {
		this.googleSelected = googleSelected;
	}

	public boolean isKodersSelected() {
		return searchcoSelected;
	}

	public void setKodersSelected(boolean kodersSelected) {
		this.searchcoSelected = kodersSelected;
	}



	public boolean isSparsSelected() {
		return githubSelected;
	}

	public int getMaxResultNum() {
		return maxResultNum;
	}

	public int getMaxKeywordNum() {
		return maxKeywordNum;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(String string) {
		// TODO Auto-generated method stub
		if(string.equals("java")){
			language=0;
		}else
		if(string.equals("cpp")||string.equals("cc")||string.equals("h")){
			language=1;
		}else
		if(string.equals("c")){
			language=2;
		}else{
			language=-1;
		}
		comboBox.setSelectedIndex(language);
		
	}

	public String getConfigInfo() {
		// TODO Auto-generated method stub
		return "Using search engine:"+(isSparsSelected()?"spars. ":"")+(isGoogleSelected()?"google code search. ":"")+(isKodersSelected()?"koders.":"")
		+"\nMax Keyword Number:"+maxKeywordNum+"\nMax Result Number:"+maxResultNum+"\nToken min size:"+"\n";
	}

	public void readConfigFile2() {
		// TODO Auto-generated method stub
		try {
			// What to do with the file, e.g. display it in a TextArea
			File file = new File("Config.txt");
			
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line.trim().length()==0||line.startsWith("#"))continue;
				String[] configs = line.split(" ");
				if(configs[0].equals("TokenizeTarget")){
					targetType = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("SortStrategy")){
					frequencyOrRandom = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("TokenMinimunSize")){
					minSize = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("UsingGoogleCodeSearch")){
					googleSelected = Boolean.valueOf(configs[1]);
				}else if(configs[0].equals("UsingGithubSearch")){
					githubSelected = Boolean.valueOf(configs[1]);
				}else if(configs[0].equals("UsingSearchco")){
					searchcoSelected = Boolean.valueOf(configs[1]);
				}else if(configs[0].equals("MaxResultNumberPerEngine")){
					maxResultNum = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("MaxKeyWordNumber")){
					maxKeywordNum = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("Language")){
					language = Integer.valueOf(configs[1]);
				}else if(configs[0].equals("UsingFileName")){
					usingFilename = Boolean.valueOf(configs[1]);
				}else if(configs[0].equals("IterationNumber")){
					iterationNumber = Integer.valueOf(configs[1]);
				}
			}
			textArea.setText(configStr);

		} catch (IOException ex) {
			System.out.println("problem accessing file Config.txt");
		}
		
	}

	public boolean isUsingFilename() {
		return usingFilename;
	}

	public void setUsingFilename(boolean usingFilename) {
		this.usingFilename = usingFilename;
	}

	public int getIterationNumber() {
		return iterationNumber;
	}

	public void setIterationNumber(int iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	public int getFrequencyOrRandom() {
		return frequencyOrRandom;
	}
	

}
