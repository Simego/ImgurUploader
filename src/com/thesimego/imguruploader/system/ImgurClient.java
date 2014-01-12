package com.thesimego.imguruploader.system;

import com.thesimego.imguruploader.entity.imgur.Album;
import com.thesimego.imguruploader.entity.imgur.ImgurImage;
import com.thesimego.imguruploader.view.Main;
import flexjson.JSONDeserializer;
import java.awt.AWTException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author drafaelli
 */
public class ImgurClient {

    private Main mainView;

    public ImgurClient(Main view) {
        mainView = view;
    }

    public ImgurImage doImageUpload(String clientID, boolean activeWindowOnly, String album) {
        try {
            BufferedImage image = Functions.getPrintScreen(activeWindowOnly);
            String content = imageUpload(clientID, image, album);

            JSONDeserializer<ImgurImage> streamDes = new JSONDeserializer<>();
            ImgurImage imgur = streamDes.deserialize(content, ImgurImage.class);

            return imgur;
        } catch (UnsupportedFlavorException | IOException | InterruptedException | AWTException ex) {
            if (mainView == null) {
                System.err.println(ex.getMessage());
            } else {
                mainView.outputError(ex.getMessage());
            }
            return null;
        }
    }

    private String imageUpload(String clientID, BufferedImage image, String album) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encodeBase64String(byteImage);
        String data;

        data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");
        if (album != null) {
            data += "&" + URLEncoder.encode("album", "UTF-8") + "=" + URLEncoder.encode(album, "UTF-8");
        }
        //data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("dasdasasdaadsdasdad", "UTF-8");

        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);

        conn.connect();
        StringBuilder stb = new StringBuilder();
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            wr.write(data);
            wr.flush();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
        }

        return stb.toString();
    }

    public boolean imageDelete(String clientID, String deleteHash) {
        try {
            URL url;
            url = new URL("https://api.imgur.com/3/image/" + deleteHash);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);

            conn.connect();
            StringBuilder stb = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
            return true;
            //System.out.println(stb.toString());
        } catch (IOException ex) {
            if (ex instanceof FileNotFoundException) {
                String message = "Image not found at Imgur.com, probably already deleted, local entry deleted.";
                if (mainView != null) {
                    mainView.outputError(message);
                } else {
                    System.err.println(message);
                }
                return true;
            } else {
                if (mainView == null) {
                    System.err.println(ex.getMessage());
                } else {
                    mainView.outputError(ex.getMessage());
                }
                return true;
            }
        }
    }

    public Album doAlbumCreate(String clientID, String title, String layout, String privacy, String cover) {

        if (cover != null && !cover.trim().isEmpty() && !cover.matches("http(|s)\\://(|i\\.)imgur\\.com/.*")) {
            JOptionPane.showMessageDialog(null, "Cover must be an Imgur Image Link.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String content = albumCreate(clientID, title, layout, privacy, cover);

        JSONDeserializer<Album> streamDes = new JSONDeserializer<>();
        Album album = streamDes.deserialize(content, Album.class);
        album.getData().setTitle(title);

        if (album.getData().getDeletehash() == null) {
            JOptionPane.showMessageDialog(null, "Something happened and couldn't create the album, try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return album;
    }

    private String albumCreate(String clientID, String title, String layout, String privacy, String cover) {
        try {
            String data;
            data = URLEncoder.encode("layout", "UTF-8") + "=" + URLEncoder.encode(layout, "UTF-8");
            data += "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode(privacy, "UTF-8");
            data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
            if (cover != null && !cover.trim().isEmpty()) {
                data += "&" + URLEncoder.encode("cover", "UTF-8") + "=" + URLEncoder.encode(cover, "UTF-8");
            }

            URL url;
            url = new URL("https://api.imgur.com/3/album/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);

            conn.connect();
            StringBuilder stb = new StringBuilder();

            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(data);
                wr.flush();
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stb.append(line).append("\n");
                    }
                }
            }

            //System.out.println(stb.toString());
            return stb.toString();
        } catch (IOException ex) {
            if (mainView == null) {
                System.err.println(ex.getMessage());
            } else {
                mainView.outputError(ex.getMessage());
            }
            return null;
        }
    }

    public boolean albumDelete(String clientID, String deleteHash) {
        try {
            URL url;
            url = new URL("https://api.imgur.com/3/album/" + deleteHash);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);

            conn.connect();
            StringBuilder stb = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
            return true;
            //System.out.println(stb.toString());
        } catch (IOException ex) {
            if (ex instanceof FileNotFoundException) {
                String message = "Album not found at Imgur.com, probably already deleted, local entry deleted.";
                if (mainView != null) {
                    mainView.outputError(message);
                } else {
                    System.err.println(message);
                }
                return true;
            } else {
                if (mainView != null) {
                    mainView.outputError(ex.getMessage());
                } else {
                    System.err.println(ex.getMessage());
                }
                return true;
            }
        }
    }

    public Album albumInfo(String clientID, String link) {
        String albumID = Functions.getIdFromLink(link);
        try {
            URL url;
            url = new URL("https://api.imgur.com/3/album/" + albumID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);

            conn.connect();
            StringBuilder stb = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
            System.out.println(stb.toString());
            JSONDeserializer<Album> streamDes = new JSONDeserializer<>();
            Album album = streamDes.deserialize(stb.toString(), Album.class);

            return album;
            //System.out.println(stb.toString());
        } catch (IOException ex) {
            if (mainView == null) {
                System.err.println(ex.getMessage());
            } else {
                mainView.outputError(ex.getMessage());
            }
            return null;
        }
    }

}
