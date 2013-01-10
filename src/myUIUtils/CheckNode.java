package myUIUtils;

/*Example modified from http://www.crionics.com/products/opensource/faq/swing_ex/JTableExamples2.html

 original one has bug. 
 */

import java.util.*;
import javax.swing.tree.*;

public class CheckNode extends DefaultMutableTreeNode {

	public final static int SINGLE_SELECTION = 0;
	public final static int DIG_IN_SELECTION = 4;
	protected int selectionMode;
	protected boolean isSelected;

	public CheckNode() {
		this(null);
	}

	public CheckNode(Object userObject) {
		this(userObject, true, true);
	}

	public CheckNode(Object userObject, boolean allowsChildren,
			boolean isSelected) {
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
		setSelectionMode(DIG_IN_SELECTION);
	}

	public void setSelectionMode(int mode) {
		selectionMode = mode;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;

		if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
			Enumeration menum = children.elements();
			while (menum.hasMoreElements()) {
				CheckNode node = (CheckNode) menum.nextElement();
				node.setSelected(isSelected);
			}
		}
	}

	public boolean isSelected() {
		return isSelected;
	}
	// If you want to change "isSelected" by CellEditor,
	/*
	 * public void setUserObject(Object obj) { if (obj instanceof Boolean) {
	 * setSelected(((Boolean)obj).booleanValue()); } else {
	 * super.setUserObject(obj); } }
	 */

	 
}