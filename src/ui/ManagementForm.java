package ui;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import utils.ExceptionUtils;
import model.Anime;
import model.Episode;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridLayout;
import java.awt.Font;

@SuppressWarnings("serial")
public class ManagementForm extends JFrame
{

	// Components
	private JPanel contentPane;
	private JButton btOpen;
	private static JTable tableEpisodes;

	// Others stuffs
	private static NumberFormat mNumberFormat = new DecimalFormat("00");
	private Anime mAnime;
	private static List<Episode> mListEpisodes;
	private static DefaultTableModel mDefaultTableModel;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ManagementForm frame = new ManagementForm();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public ManagementForm()
	{
		mAnime = MainProcess.getAnime();

		setTitle("[GERENCIAMENTO] " + mAnime.getName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 645, 344);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0)
			{
				if (PlayerForm.VISIBLE)
				{
					PlayerForm.fillTable();
					PlayerForm.selectNextToWatch();
					PlayerForm.showViewedEpisodeCount();
				}
			}
		});

		JLabel lbAnimeName = new JLabel("Anime: " + mAnime.getName());
		lbAnimeName.setForeground(Color.WHITE);
		lbAnimeName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbAnimeName.setBounds(64, 30, 205, 14);
		contentPane.add(lbAnimeName);

		JLabel lbManagement = new JLabel("Gerenciar Epis\u00F3dios");
		lbManagement.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbManagement.setForeground(Color.WHITE);
		lbManagement.setBounds(64, 11, 205, 14);
		contentPane.add(lbManagement);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBounds(250, 18, 379, 23);
		buttonsPanel.setLayout(new GridLayout(0, 5, 0, 0));
		contentPane.add(buttonsPanel);

		btOpen = new JButton("Abrir");
		btOpen.setOpaque(false);
		btOpen.setEnabled(false);
		buttonsPanel.add(btOpen);

		JButton btNew = new JButton("Novo");
		btNew.setOpaque(false);
		btNew.addActionListener((e) ->
		{
			int dialogResult = JOptionPane.showOptionDialog(null, "Como deseja inserir novos episódios?", "Escolha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Manual", "Regular Expression" }, "default");

			if (dialogResult == JOptionPane.OK_OPTION)
			{
				AddEditEpisodeForm lAddEditEpisodeForm = new AddEditEpisodeForm(AddEditEpisodeForm.MODE_INSERT);
				lAddEditEpisodeForm.setLocationRelativeTo(null);
				lAddEditEpisodeForm.setModal(true);
				lAddEditEpisodeForm.setVisible(true);
			}
			else
			{
				AddEpisodeRegexForm lAddEpisodeRegexForm = new AddEpisodeRegexForm();
				lAddEpisodeRegexForm.setLocationRelativeTo(null);
				lAddEpisodeRegexForm.setModal(true);
				lAddEpisodeRegexForm.setVisible(true);
			}
		});
		buttonsPanel.add(btNew);

		JButton btEdit = new JButton("Editar");
		btEdit.setOpaque(false);
		btEdit.addActionListener((e) ->
		{
			AddEditEpisodeForm lAddEditEpisodeForm = new AddEditEpisodeForm(AddEditEpisodeForm.MODE_UPDATE, mListEpisodes.get(tableEpisodes.getSelectedRow()));
			lAddEditEpisodeForm.setLocationRelativeTo(null);
			lAddEditEpisodeForm.setModal(true);
			lAddEditEpisodeForm.setVisible(true);
		});
		buttonsPanel.add(btEdit);

		JButton btDelete = new JButton("Deletar");
		btDelete.setOpaque(false);
		btDelete.addActionListener((a) ->
		{
			int[] indexes = tableEpisodes.getSelectedRows();
			String episodesString = episodesToString(indexes);

			int dialogResult = JOptionPane.showConfirmDialog(null, "Confirme a exclusão dos episódios selecionados:" + episodesString, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (dialogResult == JOptionPane.YES_OPTION)
			{
				for (int i = 0; i < indexes.length; i++)
				{
					Episode actualEpisode = mListEpisodes.get(indexes[i]);

					try
					{
						MainProcess.MAIN_DATABASE.deleteEpisode(actualEpisode);
					}
					catch (ClassNotFoundException | SQLException e)
					{
						ExceptionUtils.showExceptionDialog(e);
					}
				}
				fillTable();
			}
		});
		buttonsPanel.add(btDelete);

		JButton btSettings = new JButton("Anime");
		btSettings.addActionListener((e) ->
		{
			AnimeSettingsForm lAnimeSettingsForm = new AnimeSettingsForm();
			lAnimeSettingsForm.setLocationRelativeTo(null);
			lAnimeSettingsForm.setModal(true);
			lAnimeSettingsForm.setVisible(true);
		});
		btSettings.setOpaque(false);
		buttonsPanel.add(btSettings);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 71, 619, 237);
		contentPane.add(scrollPane);

		tableEpisodes = new JTable() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int column)
			{
				switch (column)
				{
					case 0:
						return String.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					default:
						return Boolean.class;
				}
			}
		};
		tableEpisodes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					AddEditEpisodeForm lAddEditEpisodeForm = new AddEditEpisodeForm(AddEditEpisodeForm.MODE_UPDATE, mListEpisodes.get(tableEpisodes.getSelectedRow()));
					lAddEditEpisodeForm.setLocationRelativeTo(null);
					lAddEditEpisodeForm.setModal(true);
					lAddEditEpisodeForm.setVisible(true);
				}
			}
		});
		scrollPane.setViewportView(tableEpisodes);

		JLabel lbHeader = new JLabel("");
		lbHeader.setBounds(0, 0, 640, 60);
		lbHeader.setIcon(new ImageIcon(getClass().getResource("/gerenciar_header.png")));
		contentPane.add(lbHeader);

		start();
	}

	public void start()
	{
		try
		{
			mListEpisodes = MainProcess.MAIN_DATABASE.getEpisodes();
			fillTable();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	public static void fillTable()
	{
		fillEpisodeList();
		mDefaultTableModel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		mDefaultTableModel.addColumn("ID");
		mDefaultTableModel.addColumn("Episódio");
		mDefaultTableModel.addColumn("Nome");
		mDefaultTableModel.addColumn("Caminho");
		mDefaultTableModel.addColumn("Assistido");

		for (Episode e : mListEpisodes)
		{
			Object[] lRow = new Object[] { e.getId(), "Episódio #" + mNumberFormat.format(e.getNumber()), e.getName(), e.getFile().toString(), e.isViewed() };
			mDefaultTableModel.addRow(lRow);
		}

		tableEpisodes.setModel(mDefaultTableModel);
	}

	private static void fillEpisodeList()
	{
		try
		{
			mListEpisodes = MainProcess.MAIN_DATABASE.getEpisodes();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	public static void selectEpisodeInTable(Episode episode)
	{
		int episodeIndex = mListEpisodes.indexOf(episode);
		tableEpisodes.setRowSelectionInterval(episodeIndex, episodeIndex);
	}

	private String episodesToString(int[] indexes)
	{
		String result = "";

		for (int i = 0; i < indexes.length; i++)
			result += "\n" + mListEpisodes.get(indexes[i]).toString();

		return result;
	}
}
