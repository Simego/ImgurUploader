package com.thesimego.imguruploader.dao;

import com.thesimego.imguruploader.entity.ImageEN;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Simego
 */
public class ImageDAO {

    public static void insert(String link, String date, String deleteHash) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("INSERT INTO images ('link','date','delete_hash') VALUES ('" + link + "','" + date + "','" + deleteHash + "');");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public static void updateDescription(String link, String description) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("UPDATE images SET description = '" + description + "' WHERE link = '" + link + "';");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public static void delete(String link) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("DELETE FROM images WHERE link = '" + link + "';");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public static ImageEN select(String link) {
        Statement stmt = Connection.openConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ImageEN image = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM images WHERE link = '"+ link +"';");
            image = new ImageEN();
            while (rs.next()) {
                image.setId(rs.getLong("id"));
                image.setLink(rs.getString("link"));
                image.setDescription(rs.getString("description"));
                image.setDate(sdf.parse(rs.getString("date")));
                image.setDeletehash(rs.getString("delete_hash"));
            }
        } catch (ParseException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
        return image;
    }

    public static List<ImageEN> list() {
        Statement stmt = Connection.openConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<ImageEN> imageList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select * from images order by id desc");
            while (rs.next()) {
                ImageEN image = new ImageEN();
                image.setId(rs.getLong("id"));
                image.setLink(rs.getString("link"));
                image.setDescription(rs.getString("description"));
                image.setDate(sdf.parse(rs.getString("date")));
                imageList.add(image);
            }
        } catch (ParseException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
        return imageList;
    }

}
