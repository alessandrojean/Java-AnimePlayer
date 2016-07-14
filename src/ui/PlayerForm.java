package ui;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.math.RoundingMode;

import javax.swing.JButton;

import java.awt.GridLayout;

import model.Anime;
import model.Episode;

import org.apache.commons.io.IOUtils;

import utils.ExceptionUtils;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class PlayerForm extends JFrame
{

	// Components
	private JPanel contentPane;
	public static JTable tableEpisodes;
	private JScrollPane scrollPane;

	// JLabels
	private static JLabel lbViewedEpisodes;
	private JLabel lbPoster, lbAnimeName, lbAnime;

	// Others stuffs
	private static NumberFormat mNumberFormat = new DecimalFormat("00");
	private static DefaultTableModel mDefaultTableModel;
	private static List<Episode> mListEpisodes;
	private Anime mAnime;

	public static boolean VISIBLE = false;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					PlayerForm frame = new PlayerForm(new Anime("Psycho-Pass", "Gen Urobuchi", "p-p.jpg"));
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public PlayerForm(Anime a)
	{
		mAnime = MainProcess.getAnime();
		mNumberFormat.setRoundingMode(RoundingMode.DOWN);

		setTitle("[PLAYER] " + mAnime.getName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 390);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		lbViewedEpisodes = new JLabel("??/??");
		lbViewedEpisodes.setForeground(Color.WHITE);
		lbViewedEpisodes.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbViewedEpisodes.setBounds(373, 21, 39, 16);
		contentPane.add(lbViewedEpisodes);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 71, 317, 284);
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
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					Episode episodeSelected = mListEpisodes.get(row);

					if (Boolean.parseBoolean(tableEpisodes.getValueAt(row, 2).toString()) == false)
					{
						tableEpisodes.setValueAt(true, row, 2);
						checkViewed(episodeSelected);
					}

					showViewedEpisodeCount();
					selectNextToWatch();

					openFile(episodeSelected);
				}
			}
		});
		tableEpisodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tableEpisodes);

		JButton btManage = new JButton("Gerenciar");
		btManage.setOpaque(false);
		btManage.setBounds(420, 17, 79, 23);
		btManage.setAlignmentX(1.0f);
		btManage.addActionListener((e) ->
		{
			LoginForm lLoginForm = new LoginForm();
			lLoginForm.setLocationRelativeTo(null);
			lLoginForm.setModal(true);
			lLoginForm.setVisible(true);
		});
		contentPane.add(btManage);

		JPanel panelPoster = new JPanel();
		panelPoster.setBounds(337, 71, 163, 248);
		panelPoster.setBorder(new CompoundBorder(new LineBorder(new Color(192, 192, 192)), new LineBorder(new Color(255, 255, 255), 4)));
		panelPoster.setLayout(null);
		contentPane.add(panelPoster);

		lbPoster = new JLabel();
		lbPoster.setBounds(5, 5, 153, 238);
		lbPoster.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		panelPoster.add(lbPoster);

		JPanel panelAnimeName = new JPanel();
		panelAnimeName.setBounds(337, 325, 163, 30);
		panelAnimeName.setBorder(new CompoundBorder(new LineBorder(new Color(192, 192, 192)), new LineBorder(new Color(255, 255, 255), 4)));
		panelAnimeName.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(panelAnimeName);

		lbAnimeName = new JLabel(mAnime.getName());
		lbAnimeName.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		lbAnimeName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbAnimeName.setHorizontalAlignment(SwingConstants.CENTER);
		panelAnimeName.add(lbAnimeName);

		JLabel lbPlayer = new JLabel("Player");
		lbPlayer.setBounds(57, 11, 205, 14);
		lbPlayer.setForeground(Color.WHITE);
		lbPlayer.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(lbPlayer);

		lbAnime = new JLabel("Anime: " + mAnime.getName());
		lbAnime.setBounds(57, 30, 205, 14);
		lbAnime.setForeground(Color.WHITE);
		lbAnime.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lbAnime);

		JLabel lbHeader = new JLabel("");
		lbHeader.setBounds(0, 0, 510, 60);
		lbHeader.setIcon(new ImageIcon(getClass().getResource("/escolha_header.png")));
		contentPane.add(lbHeader);

		start();
	}

	private void start()
	{
		try
		{
			mListEpisodes = MainProcess.MAIN_DATABASE.getEpisodes();
			fillTable();
			selectNextToWatch();
			showViewedEpisodeCount();
			showImageInPoster(mAnime);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}

	}

	private void showImageInPoster(Anime anime)
	{
		try
		{
			lbPoster.setIcon(ImageUtils.toImageIcon(new File(anime.getPhoto()), lbPoster.getWidth(), lbPoster.getHeight()));
		}
		catch (IOException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	private void checkViewed(Episode episode)
	{
		try
		{
			episode.setViewed(true);
			MainProcess.MAIN_DATABASE.updateEpisode(episode);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	private void openFile(Episode episode)
	{
		try
		{
			Desktop.getDesktop().open(episode.getFile());
		}
		catch (IOException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
	}

	public static void showViewedEpisodeCount()
	{
		int quant = 0;
		for (int i = 0; i < tableEpisodes.getRowCount(); i++)
		{
			if (tableEpisodes.getValueAt(i, 2) == Boolean.TRUE)
				quant++;
		}

		lbViewedEpisodes.setText(mNumberFormat.format(quant) + "/" + mNumberFormat.format(tableEpisodes.getRowCount()));
	}

	public static void selectNextToWatch()
	{
		for (int i = 0; i < tableEpisodes.getRowCount(); i++)
		{
			if (Boolean.parseBoolean(tableEpisodes.getValueAt(i, 2).toString()) == false)
			{
				tableEpisodes.setRowSelectionInterval(i, i);
				break;
			}
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
		mDefaultTableModel.addColumn("Episódio");
		mDefaultTableModel.addColumn("Nome");
		mDefaultTableModel.addColumn("Assistido");

		if (mListEpisodes != null)
			for (Episode e : mListEpisodes)
			{
				Object[] lRow = new Object[] { "Episódio #" + mNumberFormat.format(e.getNumber()), e.getName(), e.isViewed() };
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

	// ????
	public static String Extrair(String arquivo)
	{
		String tDir = System.getProperty("java.io.tmpdir") + "/" + arquivo;

		File f = new File(tDir);
		if (!f.exists())
		{
			try
			{
				InputStream in = PlayerForm.class.getClassLoader().getResourceAsStream(arquivo);
				OutputStream out = new FileOutputStream(tDir);

				IOUtils.copy(in, out);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return tDir;
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		VISIBLE = visible;
	}
}
