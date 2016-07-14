package ui;

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

import model.Anime;
import utils.AnimeUtils;
import utils.ExceptionUtils;
import database.Database;

public class MainProcess
{
	private static final String PARAM_ADM = "-adm";
	
	public static final Database MAIN_DATABASE = new Database();
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (args.length != 0)
			{
				if (args[0].equals(PARAM_ADM))
				{
					LoginForm lLoginForm = new LoginForm();
					lLoginForm.setLocationRelativeTo(null);
					lLoginForm.setModal(true);
					lLoginForm.setVisible(true);
				}
			}
			else
			{
				File f = new File(Database.FILE_NAME);
				if(!f.exists())
					MAIN_DATABASE.createTables();
				PlayerForm frame = new PlayerForm(AnimeUtils.getAnimeFromProperties());
				frame.setVisible(true);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Anime getAnime()
	{
		try
		{
			return AnimeUtils.getAnimeFromProperties();
		}
		catch (IOException e)
		{
			ExceptionUtils.showExceptionDialog(e);
		}
		return null;
	}
	
}
