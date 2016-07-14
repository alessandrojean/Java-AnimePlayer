package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils
{

	private static final String FILE_NAME = "config.properties";
	private static Properties mProperties;

	public static void saveProperty(String key, String value) throws IOException
	{
		if (mProperties == null)
			mProperties = new Properties();

		createFile();

		try (OutputStream lOutputStream = new FileOutputStream(FILE_NAME))
		{
			mProperties.put(key, value);
			mProperties.store(lOutputStream, null);
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	public static String getProperty(String key, String defaultValue) throws IOException
	{
		String value = null;
		if (mProperties == null)
			mProperties = new Properties();

		createFile();

		try (InputStream lInputStream = new FileInputStream(FILE_NAME))
		{
			mProperties.load(lInputStream);
			value = mProperties.getProperty(key, defaultValue);
		}
		catch (IOException e)
		{
			throw e;
		}

		return value;
	}

	private static void createFile() throws IOException
	{
		File lFile = new File(FILE_NAME);
		if (!lFile.exists())
		{
			lFile.getParentFile().mkdirs();
			lFile.createNewFile();
		}
	}

}
