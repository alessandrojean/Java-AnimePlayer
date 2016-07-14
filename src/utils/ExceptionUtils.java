package utils;

import javax.swing.JOptionPane;

import ui.ExceptionPanel;

public class ExceptionUtils
{

	public static void showExceptionDialog(Exception e)
	{
		StringBuilder sb = new StringBuilder(e.toString());
	    for (StackTraceElement ste : e.getStackTrace()) {
	        sb.append("\n\tat ");
	        sb.append(ste);
	    }
	    String trace = sb.toString();
		JOptionPane.showMessageDialog(null, new ExceptionPanel(trace), "Error", JOptionPane.ERROR_MESSAGE);
	}

}
