package it.polimi.tiw.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfilePictureGenerator
{
    private static int size = 200;
    private static float textScale = 1.8f;
    private static Random random = new Random();
    private static AtomicInteger lastRandom = new AtomicInteger(0);

    private ProfilePictureGenerator() {}

    public static String generateImage(char c) throws IOException
    {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(getRandomColor());
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(255, 255, 255));
        int textSize = (int)(size/textScale);
        g.setFont(new Font("Menlo", Font.BOLD, textSize));
        String letter = String.valueOf(c).toUpperCase();
        int letterWidth = g.getFontMetrics().stringWidth(letter);
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(letter, (size - letterWidth)/2, (size - metrics.getHeight())/2 + metrics.getAscent());
        g.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        return "data:image/png;base64,"+Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private static Color getRandomColor()
    {
        return new Color(getRandomInt(), getRandomInt(), getRandomInt());
    }

    private static synchronized int getRandomInt()
    {
        int next;
        if(lastRandom.get() < 50)next = 50 + random.nextInt(206);
        else if(lastRandom.get() > 200)next = random.nextInt(200);
        else next = random.nextInt(256);

        lastRandom.set(next);
        return next;
    }
}
