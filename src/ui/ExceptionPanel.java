package ui;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.JLabel;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import java.awt.Font;

@SuppressWarnings("serial")
public class ExceptionPanel extends JPanel
{

	/**
	 * Create the panel.
	 */
	public ExceptionPanel(String message)
	{
		setLayout(null);
		
		JLabel lblError = new JLabel("An unexpected error has occurred:");
		lblError.setBounds(0, 0, 450, 19);
		lblError.setFont(new Font("Tahoma", Font.BOLD, 15));
		add(lblError);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setLocation(0, 24);
		scrollPane.setSize(450, 257);
		add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setEditable(false);
		textArea.setText(message);
		scrollPane.setViewportView(textArea);
		
		JLabel lblIssue = new JLabel("Please report this as an issue in GitHub.");
		lblIssue.setBounds(0, 286, 450, 14);
		add(lblIssue);
		
		setSize(450, 300);
		setPreferredSize(new Dimension(450, 300));
		textArea.setCaretPosition(0);
	}

}
