package myUIUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import myUIUtils.CheckNodeTreeExample.NodeSelectionListener;


public class MyJCheckBoxTree extends JTree{

	public MyJCheckBoxTree(CheckNode root){
		
		super(root);
		setCellRenderer(new CheckRenderer());
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Angled");
		addMouseListener(new NodeSelectionListener(this));
		
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
}
