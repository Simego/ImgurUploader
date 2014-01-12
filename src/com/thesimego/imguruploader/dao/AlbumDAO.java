package com.thesimego.imguruploader.dao;

import com.thesimego.imguruploader.entity.AlbumEN;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Simego
 */
public class AlbumDAO {

    public static void insert(String title, String link, String deleteHash) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("INSERT INTO albums ('title','link','delete_hash') VALUES ('" + title + "','" + link + "','" + deleteHash + "');");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public static void delete(String link) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("DELETE FROM albums WHERE link = '" + link + "';");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public static AlbumEN select(String link) {
        Statement stmt = Connection.openConnection();
        AlbumEN album = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums WHERE link = '" + link + "';");
            while (rs.next()) {
                album = new AlbumEN();
                album.setId(rs.getLong("id"));
                album.setTitle(rs.getString("title"));
                album.setLink(rs.getString("link"));
                album.setDeletehash(rs.getString("delete_hash"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
        return album;
    }

    public static List<AlbumEN> list() {
        Statement stmt = Connection.openConnection();
        List<AlbumEN> albumList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums ORDER BY id DESC;");
            while (rs.next()) {
                AlbumEN album = new AlbumEN();
                album.setId(rs.getLong("id"));
                album.setTitle(rs.getString("title"));
                album.setLink(rs.getString("link"));
                albumList.add(album);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
        return albumList;
    }

}
