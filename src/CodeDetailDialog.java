import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.BoxLayout;
import java.awt.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JLabel;

import searcher.ResultInfo;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class CodeDetailDialog extends JDialog {
	ResultInfo ri;
	JLabel lblNewLabel;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	DefaultStyledDocument document;
	boolean[] coverFlags = null;

	// static String htmlpre =
	// "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=shift-jis\"><title> </title></head><body>";
	// static String htmlpre = "<html><head></head><body>";
	// static String htmlpost = "</body></html>";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CodeDetailDialog dialog = new CodeDetailDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.getTextInfo("http___wrathofthetaboos_googlecode_com_svn_trunk_jme_src_com_jme_image_Texture_java");
			dialog.updateCodeInfo("http___wrathofthetaboos_googlecode_com_svn_trunk_jme_src_com_jme_image_Texture_java");
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getTextInfo(String filename) {
		// TODO Auto-generated method stub
		String id = "";
		try {
			boolean flag1 = false;// #begin{file description}
			boolean flag2 = false;// #begin{set}"
			boolean flag3 = false;// #0.0"

			Scanner scanner = new Scanner(new File("ccfinderoutput.txt"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#begin{file description}"))
					flag1 = true;
				if (line.startsWith("#end{file description}"))
					flag1 = false;
				if (line.startsWith("#begin{set}"))
					flag2 = true;
				if (line.startsWith("#end{set}")) {
					flag2 = false;
					flag3 = false;
				}

				if (line.startsWith("#")) {
					continue;
				}
				String[] lineinfo = line.split("\t");
				if (flag1) {
					if (lineinfo[3].endsWith(transfer(filename))) {
						id = lineinfo[0];

						if (coverFlags == null) {
							coverFlags = new boolean[Integer
									.valueOf(lineinfo[1])];

							Arrays.fill(coverFlags, false);
						}
					}
				}
				if (flag2&&!flag3) {
					if (line.startsWith("0.0")) {
						flag3 = true;
						
					} else {
						continue;

					}
				}
				if (flag3) {
					if (lineinfo[0].equals(id)) {
						int startToken = Integer
								.valueOf(lineinfo[1].split(",")[0]);
						int endToken = Integer
								.valueOf(lineinfo[2].split(",")[0]);

						for (int i = startToken; i <= endToken; i++) {

							coverFlags[i-1] = true;
							//System.out.println("true");

						}
					}

				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Create the dialog.
	 */
	public CodeDetailDialog() {
		setBounds(100, 100, 636, 509);
		// document = new DefaultStyledDocument();
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel);
			panel.setLayout(new BorderLayout(0, 0));

			lblNewLabel = new JLabel("");
			panel.add(lblNewLabel, BorderLayout.NORTH);

			scrollPane = new JScrollPane();
			panel.add(scrollPane, BorderLayout.CENTER);

			// textPane.setContentType("text/plain");
			// scrollPane.setViewportView(textPane);

		}
	}

	public void setResultInfo(ResultInfo ri) {
		// TODO Auto-generated method stub
		this.ri = ri;
	}

	public void updateCodeInfo(String filename) {
		// TODO Auto-generated method stub
		lblNewLabel.setText(filename);
		// StringBuffer sb = new StringBuffer("");
		String tempstr = "";
		document = new DefaultStyledDocument();
		textPane = new JTextPane(document);
		try {
			
			SimpleAttributeSet attribute = new SimpleAttributeSet();

			Scanner scanner = new Scanner(new File("./cache/"
					+ transfer(filename)));
			int count = 0;
			while (scanner.hasNextLine()) {

				tempstr = scanner.nextLine() + "\n";
				if (coverFlags[count++]) {
					attribute.addAttribute(StyleConstants.Background,
							Color.lightGray);
				} else {
					attribute.addAttribute(StyleConstants.Background,
							Color.WHITE);
				}
				document.insertString(document.getLength(), tempstr, attribute);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String output = "<color=\"blue\">"+sb.toString()+"</color>";
		// sb.append("<color=\"blue\">testcolor</color>");
		// sb.append(htmlpost);
		// textPane.setText(sb.toString());
		textPane.setCaretPosition(0);
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		
		coverFlags=null;
	}

	public String transfer(String string) {
		string = string.replace(':', '_');
		string = string.replace('/', '_');
		string = string.replace('.', '_');
		string = string.replace('?', '_');
		string = string.replace('=', '_');
		string = string.replace('#', '_');
		string = string.replaceAll(" ", "%20");
		string = string.replaceAll("&amp;", "_");
		if(string.length()>150){
			string = string.substring(0,75)+string.substring(string.length()-76,string.length()-1);
		}
		return string;
	}

}
