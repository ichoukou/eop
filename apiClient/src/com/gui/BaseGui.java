package com.gui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 界面基类
 * 
 * @author huaungtianfu
 * 
 */
public class BaseGui extends JFrame {

	private static final long serialVersionUID = 1771697248850432083L;

	protected javax.swing.JLabel createJLabel(String text, int x, int y,
			int width, int height) {
		JLabel jLabel = new javax.swing.JLabel();
		jLabel.setBounds(x, y, width, height);
		jLabel.setText(text + ":");
		return jLabel;
	}

	protected javax.swing.JTextField createJTextField(int x, int y, int width,
			int height, boolean enabled) {
		JTextField jTextField = new javax.swing.JTextField();
		jTextField.setBounds(x, y, width, height);
		jTextField.setEditable(enabled);
		return jTextField;
	}

	protected javax.swing.JButton createJButton(String text, int x, int y,
			int width, int height) {
		JButton jButton = new javax.swing.JButton();
		jButton.setBounds(x, y, width, height);
		jButton.setText(text);
		return jButton;
	}

	protected javax.swing.JTextArea createJTextArea(String text, int rows,
			int columns, Font font,int x, int y, int width,
			int height,boolean edit) {
		JTextArea jTextArea = new JTextArea();
		jTextArea.setText(text);
		jTextArea.setRows(rows);
		jTextArea.setColumns(columns);
		jTextArea.setFont(font);
		jTextArea.setBounds(x, y, width, height);
		jTextArea.setEditable(edit);
		return jTextArea;
	}

}
