package com.thesimego.imguruploader.system;

import com.thesimego.imguruploader.entity.Imgur;
import com.thesimego.imguruploader.view.MainView;
import flexjson.JSONDeserializer;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Simego
 */
public class Functions {

    private final ClipboardData clipboardData = new ClipboardData();
    private final MainView jframe;

    public Functions(MainView jframe) {
        this.jframe = jframe;
    }

    public Imgur getImgurData(String clientID, boolean activeWindowOnly) {
        try {
            String content = getContent(clientID, activeWindowOnly);

            JSONDeserializer<Imgur> streamDes = new JSONDeserializer<>();
            Imgur imgur = streamDes.deserialize(content, Imgur.class);

            return imgur;
        } catch (Exception ex) {
            jframe.output("[ERROR] " + ex.getMessage(), jframe.getInfoTextArea());
            return null;
        }
    }

    public String getContent(String clientID, boolean activeWindowOnly) throws Exception {
        //create base64 image
        BufferedImage image;

        if (activeWindowOnly) {
            printActiveWindow();
            InputStream is = getImageInputStream(clipboardData.getImageData());
            image = ImageIO.read(is);
        } else {
            image = getScreenBufferedImage();
        }

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encodeBase64String(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

        URL url;

        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        return stb.toString();
    }

    private BufferedImage getScreenBufferedImage() throws AWTException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = robot.createScreenCapture(screenRect);
        return capture;
    }

    private InputStream getImageInputStream(Image clipboardImage) throws IOException {
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

    public void printActiveWindow() {
        Robot robot;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalArgumentException("No robot");
        }

        // Press Alt + PrintScreen
        // (Windows shortcut to take a screen shot of the active window)
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void saveClientID(String clientID) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientid.txt", false))) {
                writer.write(clientID);
            }
        } catch (IOException ex) {
            jframe.output("[ERROR] " + ex.getMessage(), jframe.getInfoTextArea());
        }
    }

    public String loadClientID() {
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

}
