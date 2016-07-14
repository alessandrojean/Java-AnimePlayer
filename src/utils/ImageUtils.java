package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils
{

	public static ImageIcon toImageIcon(File file, int width, int height) throws IOException
	{
		ImageIcon lImageIcon = null;

		BufferedImage lBufferedImage = ImageIO.read(file);
		if (lBufferedImage != null)
		{
			BufferedImage lBufferedImageResized = resizeImage(lBufferedImage, width, height);
			lImageIcon = new ImageIcon(lBufferedImageResized);
		}

		return lImageIcon;
	}

	private static BufferedImage resizeImage(BufferedImage image, int width, int height)
	{
		BufferedImage lBufferedImage = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
		Graphics2D lGraphics2D = (Graphics2D) lBufferedImage.createGraphics();
		lGraphics2D.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		lGraphics2D.drawImage(image, 0, 0, width, height, null);
		lGraphics2D.dispose();
		return lBufferedImage;
	}

}
