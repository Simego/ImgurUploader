package com.thesimego.imguruploader.system;

import com.thesimego.imguruploader.view.Main;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Simego
 */
public class Functions {

    private static final ClipboardData clipboardData = new ClipboardData();

    public static String getIdFromLink(String link) {
        return link.replaceAll("http(|s)://imgur.com/.*?/", "");
    }

    public static void saveClientID(String clientID) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientid.txt", false))) {
                writer.write(clientID);
            }
        } catch (IOException ex) {
        }
    }

    public static String loadClientID() {
        try {
            String clientID;
            try (BufferedReader reader = new BufferedReader(new FileReader("clientid.txt"))) {
                clientID = reader.readLine();
            }
            return clientID;
        } catch (IOException ex) {
            return "";
        }
    }

    public static BufferedImage getPrintScreen(boolean activeWindowOnly) throws IOException, UnsupportedFlavorException, InterruptedException, AWTException {
        if (activeWindowOnly) {
            printActiveWindow();
            InputStream is = getImageInputStream(clipboardData.getImageData());
            return ImageIO.read(is);
        } else {
            BufferedImage im = getScreenBufferedImage();
            clipboardData.setImageData(im);
            return im;
        }
    }

    private static BufferedImage getScreenBufferedImage() throws AWTException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = robot.createScreenCapture(screenRect);
        return capture;
    }

    private static InputStream getImageInputStream(Image clipboardImage) throws IOException {
        BufferedImage bi = new BufferedImage(clipboardImage.getWidth(null), clipboardImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(clipboardImage, 0, 0, null);
        bg.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos);
        baos.flush();

        byte[] imageInByte = baos.toByteArray();
        baos.close();

        InputStream is = new ByteArrayInputStream(imageInByte);
        return is;
    }

    private static void printActiveWindow() {
        Robot robot;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalArgumentException("No robot");
        }

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

}
