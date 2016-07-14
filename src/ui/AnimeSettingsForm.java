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
import java.io.File;
import utils.AnimeUtils;
import utils.ExceptionUtils;
import model.Anime;

import java.awt.Font;

@SuppressWarnings("serial")
public class AnimeSettingsForm extends JDialog
{
	// JTextFields
	private JTextField textFieldCreator;
	private JTextField textFieldPhoto;
	private JTextField textFieldName;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					AnimeSettingsForm lAnimeSettingsForm = new AnimeSettingsForm();
					lAnimeSettingsForm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					lAnimeSettingsForm.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public AnimeSettingsForm()
	{
		Anime lAnime = MainProcess.getAnime();

		setResizable(false);
		setBounds(100, 100, 325, 285);
		getContentPane().setLayout(null);

		JLabel lbSettings = new JLabel("Configurações");
		lbSettings.setForeground(Color.WHITE);
		lbSettings.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbSettings.setBounds(57, 11, 205, 14);
		getContentPane().add(lbSettings);

		JLabel lbHeader = new JLabel("");
		lbHeader.setIcon(new ImageIcon(getClass().getResource("/alteracao_header.png")));
		lbHeader.setBounds(0, 0, 320, 60);
		getContentPane().add(lbHeader);

		JPanel panel = new JPanel();
		panel.setBounds(0, 59, 320, 198);
		getContentPane().add(panel);

		JLabel lbAnimeName = new JLabel("Nome do Anime:");
		JLabel lbAnimeCreator = new JLabel("Criador:");
		JLabel lbAnimePhoto = new JLabel("Foto:");

		JSeparator separator = new JSeparator();
		JSeparator separatorOne = new JSeparator();
		JSeparator separatorThree = new JSeparator();

		textFieldName = new JTextField(lAnime.getName());
		textFieldName.setColumns(10);
		textFieldName.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldName.setBackground(Color.WHITE);

		textFieldCreator = new JTextField(lAnime.getCreator());
		textFieldCreator.setColumns(10);
		textFieldCreator.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldCreator.setBackground(Color.WHITE);

		textFieldPhoto = new JTextField(lAnime.getPhoto());
		textFieldPhoto.setColumns(10);
		textFieldPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		textFieldPhoto.setBackground(Color.WHITE);

		JButton btOpenFile = new JButton("Abrir...");
		btOpenFile.addActionListener((e) ->
		{
			JFileChooser lJFileChooser = new JFileChooser();
			lJFileChooser.setSelectedFile(new File("."));

			if (lJFileChooser.showOpenDialog(AnimeSettingsForm.this) == JFileChooser.APPROVE_OPTION)
			{
				textFieldPhoto.setText(lJFileChooser.getSelectedFile().toString());
			}
		});

		JButton btOK = new JButton("OK");
		btOK.addActionListener((e) ->
		{
			if (savePreferences())
				JOptionPane.showMessageDialog(null, "As preferências foram salvas!\nÉ necessário reiniciar o programa para que as mesmas tenham efeito.");

			setVisible(false);
			dispose();
		});
		btOK.setActionCommand("OK");

		JButton btCancel = new JButton("Cancelar");
		btCancel.addActionListener((e) ->
		{
			setVisible(false);
			dispose();
		});
		btCancel.setActionCommand("Cancel");

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lbAnimeName, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(lbAnimeCreator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(textFieldCreator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(separatorOne, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(lbAnimePhoto, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addComponent(textFieldPhoto, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btOpenFile, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)).addComponent(separatorThree, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addComponent(btOK, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btCancel)).addComponent(textFieldName, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(lbAnimeName).addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lbAnimeCreator).addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldCreator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorOne, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lbAnimePhoto).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(textFieldPhoto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(btOpenFile, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addComponent(separatorThree, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(btCancel).addComponent(btOK)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
	}

	public boolean savePreferences()
	{
		try
		{
			Anime anime = new Anime();
			anime.setName(textFieldName.getText());
			anime.setCreator(textFieldCreator.getText());
			anime.setPhoto(textFieldPhoto.getText());

			AnimeUtils.setAnimeInProperties(anime);

			return true;
		}
		catch (Exception e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}

		return false;
	}
}
