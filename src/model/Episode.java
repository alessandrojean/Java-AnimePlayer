package model;

import java.io.File;

public class Episode
{
	private int id;
	private String name;
	private int number;
	private File file;
	private boolean viewed;

	public Episode()
	{
		super();
	}

	public Episode(int id)
	{
		super();
		this.id = id;
	}

	public Episode(int id, int number)
	{
		super();
		this.id = id;
		this.number = number;
	}

	public Episode(int id, int number, String name)
	{
		super();
		this.id = id;
		this.name = name;
		this.number = number;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public boolean isViewed()
	{
		return viewed;
	}

	public void setViewed(boolean viewed)
	{
		this.viewed = viewed;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + number;
		result = prime * result + (viewed ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Episode other = (Episode) obj;
		if (file == null)
		{
			if (other.file != null)
				return false;
		}
		else if (!file.equals(other.file))
			return false;
		if (id != other.id)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (number != other.number)
			return false;
		if (viewed != other.viewed)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Episode [id=" + id + ", name=" + name + ", number=" + number + "]";
	}
}
