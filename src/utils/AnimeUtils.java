package utils;

import java.io.IOException;

import model.Anime;

public class AnimeUtils
{
	public static Anime getAnimeFromProperties() throws IOException
	{
		String name = PropertiesUtils.getProperty(Anime.KEY_NAME, "");
		String creator = PropertiesUtils.getProperty(Anime.KEY_CREATOR, "");
		String photo = PropertiesUtils.getProperty(Anime.KEY_PHOTO, "");

		return new Anime(name, creator, photo);
	}
	
	public static void setAnimeInProperties(Anime anime) throws IOException
	{
		PropertiesUtils.saveProperty(Anime.KEY_NAME, anime.getName());
		PropertiesUtils.saveProperty(Anime.KEY_CREATOR, anime.getCreator());
		PropertiesUtils.saveProperty(Anime.KEY_PHOTO, anime.getPhoto());
	}
}
