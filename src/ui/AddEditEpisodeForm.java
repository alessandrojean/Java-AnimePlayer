package ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import utils.ExceptionUtils;
import model.Anime;
import model.Episode;
import java.io.File;
import java.awt.Font;

@SuppressWarnings("serial")
public class AddEditEpisodeForm extends JDialog
{

	// Components
	private final JPanel contentPanel = new JPanel();
	private JCheckBox checkBoxViewed;

	// JTextFields
	private JTextField textFieldID, textFieldNumber, textFieldName, textFieldPath;

	// JButtons
	private JButton btCancel, btOK;

	// Others stuffs
	private int actualMode;
	private Episode updatingEpisode;

	// Constants
	public static final int MODE_INSERT = 1, MODE_UPDATE = 2;

	public static void main(String[] args)
	{
		try
		{
			AddEditEpisodeForm lAddEditEpisodeForm = new AddEditEpisodeForm(MODE_INSERT);
			lAddEditEpisodeForm.setLocationRelativeTo(null);
			lAddEditEpisodeForm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			lAddEditEpisodeForm.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public AddEditEpisodeForm(int mode)
	{
		actualMode = mode;

		initComponents();
		start();
	}

	public AddEditEpisodeForm(int mode, Episode episode)
	{
		actualMode = mode;
		updatingEpisode = episode;

		initComponents();
		start();
	}

	private void initComponents()
	{
		Anime lAnime = MainProcess.getAnime();

		setTitle(actualMode == MODE_INSERT ? "[INSERINDO] Novo anime" : "[EDITANDO] " + lAnime.getName());
		setResizable(false);
		setBounds(100, 100, 325, 376);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lbAnimeName = new JLabel("Anime: " + lAnime.getName());
		lbAnimeName.setForeground(Color.WHITE);
		lbAnimeName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbAnimeName.setBounds(57, 30, 205, 14);
		contentPanel.add(lbAnimeName);

		JLabel lbActualMode = new JLabel((actualMode == MODE_INSERT ? "Novo" : "Editar") + " Episódio");
		lbActualMode.setForeground(Color.WHITE);
		lbActualMode.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbActualMode.setBounds(57, 11, 205, 14);
		contentPanel.add(lbActualMode);

		JLabel lbHeader = new JLabel("");
		lbHeader.setIcon(new ImageIcon(getClass().getResource("/alteracao_header.png")));
		lbHeader.setBounds(0, 0, 320, 60);
		contentPanel.add(lbHeader);

		JPanel panel = new JPanel();
		panel.setBounds(0, 60, 320, 288);
		contentPanel.add(panel);

		JLabel lbId = new JLabel("ID:");
		JLabel lbNumber = new JLabel("N\u00FAmero:");
		JLabel lbName = new JLabel("Nome:");
		JLabel lbPath = new JLabel("Caminho:");

		textFieldID = new JTextField();
		textFieldID.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		textFieldID.setBackground(Color.WHITE);
		textFieldID.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldID.setEditable(false);
		textFieldID.setColumns(10);

		textFieldNumber = new JTextField();
		textFieldNumber.setBackground(Color.WHITE);
		textFieldNumber.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		textFieldNumber.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldNumber.setEditable(false);
		textFieldNumber.setColumns(10);

		textFieldName = new JTextField();
		textFieldName.setColumns(10);

		textFieldPath = new JTextField();
		textFieldPath.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		textFieldPath.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldPath.setBackground(Color.WHITE);
		textFieldPath.setColumns(10);

		JSeparator separatorOne = new JSeparator();
		JSeparator separatorTwo = new JSeparator();
		JSeparator separatorThree = new JSeparator();
		JSeparator separatorFour = new JSeparator();

		JButton btOpenFile = new JButton("Abrir");
		btOpenFile.addActionListener((e) ->
		{
			JFileChooser lJFileChooser = new JFileChooser();
			if (!(actualMode == MODE_INSERT))
				lJFileChooser.setSelectedFile(updatingEpisode.getFile());

			if (lJFileChooser.showOpenDialog(AddEditEpisodeForm.this) == JFileChooser.APPROVE_OPTION)
				textFieldPath.setText(lJFileChooser.getSelectedFile().toString());
		});

		checkBoxViewed = new JCheckBox("Visto");

		btCancel = new JButton("Cancelar");
		btCancel.addActionListener((e) ->
		{
			setVisible(false);
			dispose();
		});
		btCancel.setActionCommand("Cancel");

		btOK = new JButton("OK");
		btOK.addActionListener((e) ->
		{
			if (actualMode == MODE_INSERT)
			{
				Episode insertedEpisode = insertEpisode();

				setVisible(false);
				dispose();
				ManagementForm.fillTable();
				ManagementForm.selectEpisodeInTable(insertedEpisode);
			}
			else
			{
				editEpisode();

				setVisible(false);
				dispose();
				ManagementForm.fillTable();
				ManagementForm.selectEpisodeInTable(updatingEpisode);
			}
		});
		btOK.setActionCommand("OK");
		getRootPane().setDefaultButton(btOK);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(checkBoxViewed, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(textFieldID, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(lbId, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(separatorOne, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(lbNumber, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(textFieldNumber, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(separatorTwo, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(lbName, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(separatorThree, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(lbPath, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addGroup(gl_panel.createSequentialGroup().addComponent(textFieldPath, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btOpenFile, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)).addComponent(separatorFour, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addComponent(textFieldName, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addComponent(btOK, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btCancel))).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(lbId).addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorOne, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lbNumber).addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorTwo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lbName).addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorThree, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lbPath).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(textFieldPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(btOpenFile, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorFour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(checkBoxViewed).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btCancel).addComponent(btOK)).addContainerGap()));
		panel.setLayout(gl_panel);
	}

	private void start()
	{
		if (actualMode == MODE_INSERT)
		{
			textFieldID.setText(String.valueOf(getLastEpisodeId() + 1));
			textFieldNumber.setText(String.valueOf(getLastEpisodeNumber() + 1));
		}
		else
		{
			textFieldID.setText(String.valueOf(updatingEpisode.getId()));
			textFieldNumber.setText(String.valueOf(updatingEpisode.getNumber()));
			textFieldName.setText(updatingEpisode.getName());
			textFieldPath.setText(updatingEpisode.getFile().toString());
			checkBoxViewed.setSelected(updatingEpisode.isViewed());
		}
	}

	private Episode insertEpisode()
	{
		try
		{
			Episode episode = new Episode();
			episode.setName(textFieldName.getText());
			episode.setNumber(Integer.parseInt(textFieldNumber.getText()));
			episode.setFile(new File(textFieldPath.getText()));
			episode.setViewed(checkBoxViewed.isSelected());

			MainProcess.MAIN_DATABASE.insertEpisode(episode);
			return episode;
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}

		return null;
	}

	private void editEpisode()
	{
		try
		{
			updatingEpisode.setName(textFieldName.getText());
			updatingEpisode.setNumber(Integer.parseInt(textFieldNumber.getText()));
			updatingEpisode.setFile(new File(textFieldPath.getText()));
			updatingEpisode.setViewed(checkBoxViewed.isSelected());

			MainProcess.MAIN_DATABASE.updateEpisode(updatingEpisode);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	private int getLastEpisodeId()
	{
		int lastID = 0;

		try
		{
			List<Episode> episodeList = MainProcess.MAIN_DATABASE.getEpisodes();
			if (episodeList != null)
				for (Episode e : episodeList)
					if (e.getId() > lastID)
						lastID = e.getId();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}

		return lastID;
	}

	private int getLastEpisodeNumber()
	{
		int lastNumber = 0;

		try
		{
			List<Episode> episodeList = MainProcess.MAIN_DATABASE.getEpisodes();
			if (episodeList != null)
				for (Episode e : episodeList)
					if (e.getNumber() > lastNumber)
						lastNumber = e.getNumber();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}

		return lastNumber;
	}
}
