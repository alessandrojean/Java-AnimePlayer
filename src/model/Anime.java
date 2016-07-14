package model;

public class Anime
{
	public static final String KEY_NAME = "anime.name";
	public static final String KEY_CREATOR = "anime.creator";
	public static final String KEY_PHOTO = "anime.photo";
	
	private String name;
	private String creator;
	private String photo;

	public Anime()
	{
		super();
	}

	public Anime(String name, String creator, String photo)
	{
		super();
		this.name = name;
		this.creator = creator;
		this.photo = photo;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getPhoto()
	{
		return photo;
	}

	public void setPhoto(String photo)
	{
		this.photo = photo;
	}

	@Override
	public String toString()
	{
		return "Anime [name=" + name + ", creator=" + creator + ", photo=" + photo + "]";
	}
}
