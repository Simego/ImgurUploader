package com.thesimego.imguruploader.dao;

import com.thesimego.imguruploader.entity.Link;
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
public class LinkDAO {

    public void insert(String link, String description, String date) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("INSERT INTO links ('link','date') VALUES ('" + link + "','" + date + "');");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }
    
    public void updateDescription(String link, String description) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("UPDATE links SET description = '" + description + "' WHERE link = '" + link + "';");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }
    
    public void delete(String link) {
        Statement stmt = Connection.openConnection();
        try {
            stmt.executeUpdate("DELETE FROM links WHERE link = '" + link + "';");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
    }

    public List<Link> list() {
        Statement stmt = Connection.openConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Link> linkList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select * from links order by id desc");
            while (rs.next()) {
                Link link = new Link();
                link.setId(rs.getLong("id"));
                link.setLink(rs.getString("link"));
                link.setDescription(rs.getString("description"));
                link.setDate(sdf.parse(rs.getString("date")));
                linkList.add(link);
            }
        } catch (ParseException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
        Connection.closeConnection();
        return linkList;
    }

}
