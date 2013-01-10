package myUIUtils;

/*Example modified from http://www.crionics.com/products/opensource/faq/swing_ex/JTableExamples2.html
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class CheckNodeTreeExample extends JFrame {
	public CheckNodeTreeExample() {
		super("CheckNode TreeExample");
		String[] strs = { "swing", // 0
				"platf", // 1
				"basic", // 2
				"metal", // 3
				"JTree" }; // 4

		CheckNode[] nodes = new CheckNode[strs.length];
		for (int i = 0; i < strs.length; i++) {
			nodes[i] = new CheckNode(strs[i]);
		}
		
		nodes[3].setSelected(true);
		JTree tree = new JTree(nodes[0]);
		tree.setCellRenderer(new CheckRenderer());
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.addMouseListener(new NodeSelectionListener(tree));
		JScrollPane sp = new JScrollPane(tree);

	
		nodes[0].add(nodes[1]);
		nodes[1].add(nodes[2]);
		nodes[1].add(nodes[3]);
		nodes[0].add(nodes[4]);

		getContentPane().add(sp, BorderLayout.CENTER);
	
		
	}

	class NodeSelectionListener extends MouseAdapter {
		JTree tree;

		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}

		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);
			// TreePath path = tree.getSelectionPath();
			if (path != null) {
				CheckNode node = (CheckNode) path.getLastPathComponent();
				boolean isSelected = !(node.isSelected());
				node.setSelected(isSelected);
				if (node.getSelectionMode() == CheckNode.DIG_IN_SELECTION) {
					if (isSelected) {
						tree.expandPath(path);
					} else {
						tree.collapsePath(path);
					}
				}
				((DefaultTreeModel) tree.getModel()).nodeChanged(node);
				// I need revalidate if node is root. but why?
				if (row == 0) {
					tree.revalidate();
					tree.repaint();
				}
			}
		}
	}
/*
	class ModePanel extends JPanel implements ActionListener {
		CheckNode[] nodes;
		JRadioButton b_single, b_dig_in;

		ModePanel(CheckNode[] nodes) {
			this.nodes = nodes;
			setLayout(new GridLayout(2, 1));
			setBorder(new TitledBorder("Check Mode"));
			ButtonGroup group = new ButtonGroup();
			add(b_dig_in = new JRadioButton("DIG_IN "));
			add(b_single = new JRadioButton("SINGLE "));
			group.add(b_dig_in);
			group.add(b_single);
			b_dig_in.addActionListener(this);
			b_single.addActionListener(this);
			b_dig_in.setSelected(true);
		}

		public void actionPerformed(ActionEvent e) {
			int mode;
			if (b_single == e.getSource()) {
				mode = CheckNode.SINGLE_SELECTION;
			} else {
				mode = CheckNode.DIG_IN_SELECTION;
			}
			for (int i = 0; i < nodes.length; i++) {
				nodes[i].setSelectionMode(mode);
			}
		}
	}

*/	
	class ButtonActionListener implements ActionListener {
		CheckNode root;
		JTextArea textArea;

		ButtonActionListener(final CheckNode root, final JTextArea textArea) {
			this.root = root;
			this.textArea = textArea;
		}

		public void actionPerformed(ActionEvent e) {
			Enumeration menum = root.breadthFirstEnumeration();
			while (menum.hasMoreElements()) {
				CheckNode node = (CheckNode) menum.nextElement();
				if (node.isSelected()) {
					TreeNode[] nodes = node.getPath();
					textArea.append("\n" + nodes[0].toString());
					for (int i = 1; i < nodes.length; i++) {
						textArea.append("/" + nodes[i].toString());
					}
				}
			}
		}
	}

	public static void main(String args[]) {
		CheckNodeTreeExample frame = new CheckNodeTreeExample();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}
