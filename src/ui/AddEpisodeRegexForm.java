package ui;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import utils.ExceptionUtils;
import model.Episode;

import java.awt.Font;

@SuppressWarnings("serial")
public class AddEpisodeRegexForm extends JDialog
{
	private JTextField textField_1;
	private JTextField textField_2;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					AddEpisodeRegexForm dialog = new AddEpisodeRegexForm();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddEpisodeRegexForm()
	{
		setResizable(false);
		setBounds(100, 100, 325, 285);
		getContentPane().setLayout(null);
		
		JLabel lblRegex_1 = new JLabel("ReGex");
		lblRegex_1.setForeground(Color.WHITE);
		lblRegex_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRegex_1.setBounds(57, 11, 205, 14);
		getContentPane().add(lblRegex_1);
		
		JLabel label_2 = new JLabel("Anime: "+MainProcess.getAnime().getName());
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_2.setBounds(57, 30, 205, 14);
		getContentPane().add(label_2);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(getClass().getResource("/alteracao_header.png")));
		label_1.setBounds(0, 0, 320, 60);
		getContentPane().add(label_1);

		JPanel panel = new JPanel();
		panel.setBounds(0, 59, 320, 198);
		getContentPane().add(panel);

		JLabel lblRegex = new JLabel("Regex:");

		JSeparator separator = new JSeparator();

		JLabel lblGroupDoEpisdio = new JLabel("Group do Epis\u00F3dio:");

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textField_1.setBackground(Color.WHITE);

		JSeparator separator_1 = new JSeparator();

		JLabel lblPasta = new JLabel("Pasta:");

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textField_2.setBackground(Color.WHITE);

		JButton button = new JButton("Abrir");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (jfc.showOpenDialog(AddEpisodeRegexForm.this) == JFileChooser.APPROVE_OPTION)
				{
					textField_2.setText(jfc.getSelectedFile().toString());
				}
			}
		});

		JSeparator separator_3 = new JSeparator();

		JButton button_1 = new JButton("OK");
		button_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				File[] arquivos = new File(textField_2.getText()).listFiles();

				Pattern p = Pattern.compile(comboBox.getSelectedItem().toString());
				Matcher m;

				String saida = "";

				List<File> eps = new ArrayList<File>();

				for (File f : arquivos)
				{
					if (f.isFile())
					{
						eps.add(f);
					}
				}

				int i = 0;
				for (File f : eps)
				{
					if (f.isFile())
					{
						m = p.matcher(f.getName());

						if (m.matches())
						{
							// Alteracao.inserir(Integer.parseInt(m.group(Integer.parseInt(textField_1.getText()))),
							// f.getPath());
							saida += m.group(Integer.parseInt(textField_1.getText())) + (i == eps.size() - 1 ? "" : ", ");
							// saida+=f.getPath()+"\n";
						}
					}
					i++;
				}

				int res = JOptionPane.showConfirmDialog(null, "<html><h3>Confirme os episódios:</h3><br/>" + saida + "</html>", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (res == JOptionPane.YES_OPTION)
				{
					for (File f : eps)
					{
						if (f.isFile())
						{
							m = p.matcher(f.getName());

							if (m.matches())
							{
								Episode episode = new Episode();
								episode.setNumber(Integer.parseInt(m.group(Integer.parseInt(textField_1.getText()))));
								episode.setFile(f);
								
								try
								{
									MainProcess.MAIN_DATABASE.insertEpisode(episode);
								}
								catch(ClassNotFoundException | SQLException ex)
								{
									ExceptionUtils.showExceptionDialog(ex);
								}
							}
						}
					}
					setVisible(false);
					dispose();
					ManagementForm.fillTable();
				}
				else
				{
					comboBox.requestFocus();
				}

				//
			}
		});
		button_1.setActionCommand("OK");

		JButton button_2 = new JButton("Cancelar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		button_2.setActionCommand("Cancel");

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[]
		{ "^\\[(.*?)\\]_(.*?)_-_(.*?)_\\[(.*?)\\]_[(](.*?)[)].(.*?)$", "^\\[(.*?)\\]_(.*?)_-_(.*?)_\\[(.*?)\\]_\\[(.*?)\\]_[(](.*?)[)].(.*?)$" }));
		comboBox.setEditable(true);
		comboBox.setSelectedIndex(-1);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblRegex, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(lblGroupDoEpisdio, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(lblPasta, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(separator_3, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_2))
						.addComponent(comboBox, 0, 218, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRegex)
					.addGap(2)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblGroupDoEpisdio)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPasta)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(button_2)
						.addComponent(button_1))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

	}
}
