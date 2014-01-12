package com.thesimego.imguruploader.view;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.thesimego.imguruploader.dao.AlbumDAO;
import com.thesimego.imguruploader.dao.ImageDAO;
import com.thesimego.imguruploader.entity.AlbumEN;
import com.thesimego.imguruploader.entity.ImageEN;
import com.thesimego.imguruploader.entity.imgur.ImgurImage;
import com.thesimego.imguruploader.system.ImgurClient;
import java.awt.AWTException;
import java.awt.Desktop;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Simego
 */
public class Main extends javax.swing.JFrame implements HotkeyListener, IntellitypeListener {

    private static final int PRINT_SCREEN = 91;

    private final ImgurClient imgurClient = new ImgurClient(this);

    private TrayIcon trayIcon;

    private SystemTray tray;

    private final String defaultClientID = "YOUR_CLIENT_ID_HERE";

    //<editor-fold defaultstate="collapsed" desc="Main">
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main main = new Main();
                main.setVisible(true);
            }

        });
    }
    //</editor-fold>

    public Main() {
        //<editor-fold defaultstate="collapsed" desc="MainView Constructor">
        initComponents();
        createTrayFunctions();
        initSystem();
        //</editor-fold>
    }

    private void initSystem() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        setLocationRelativeTo(null);

        tableImages.getColumnModel().getColumn(0).setPreferredWidth(90);
        tableImages.getColumnModel().getColumn(1).setPreferredWidth(130);
        tableImages.getColumnModel().getColumn(2).setPreferredWidth(25);
        tableAlbums.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableAlbums.getColumnModel().getColumn(1).setPreferredWidth(100);

        updateImageTable();
        updateAlbumTable();

        try {
            initJIntellitype();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Init Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        btnStop.setEnabled(false);
    }

    private void initJIntellitype() {
        //<editor-fold defaultstate="collapsed" desc="Init JIntellitype">
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
            outputInfo("After START, this system will override the selected SHORTCUT function until it STOP.");
            outputInfo("If you can't upload images on 'default user', click the Help button and find out how to create a ClientID.");
        } catch (HeadlessException ex) {
            outputError("Either you are not on Windows, or there is a problem with the JIntellitype library!");
        }
        //</editor-fold>
    }

    private void startKeyCapture() {
        //<editor-fold defaultstate="collapsed" desc="Start Key Capture (JIntellitype)">
        int comboIndex = comboShortcut.getSelectedIndex();
        switch (comboIndex) {
            case 0:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, 'C');
                break;
            case 1:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, 0, 44);
                break;
        }

        outputInfo("System started, screenshots will be uploaded to your Imgur account.");
        outputInfo("Hotkey: " + comboShortcut.getSelectedItem().toString());
        //</editor-fold>
    }

    private void stopKeyCapture() {
        //<editor-fold defaultstate="collapsed" desc="Stop Key Capture (JIntellitype)">
        JIntellitype.getInstance().unregisterHotKey(PRINT_SCREEN);
        outputInfo("System stopped.");
        //</editor-fold>
    }

    @Override
    public void onHotKey(int aIdentifier) {
        //<editor-fold defaultstate="collapsed" desc="JIntellitype onHotKey Override Method">
        ImgurImage imgur;
        AlbumEN album = getSelectedAlbum();

        imgur = imgurClient.doImageUpload(getClientID(), chkbPrintActWin.isSelected(), album == null ? null : album.getDeletehash());

        if (imgur != null) {
            outputInfo("Upload complete.");
            outputInfo(imgur.getData().toString());
            ImageDAO.insert(imgur.getData().getLink(), imgur.getData().getDateString(), imgur.getData().getDeletehash());
            updateImageTable();
        } else {
            outputError("Problem trying to upload the image, maybe wrong client id or connection issue? try again.");
        }
        //</editor-fold>
    }

    @Override
    public void onIntellitype(int aCommand) {
        //<editor-fold defaultstate="collapsed" desc="JIntellitype onIntellitype Override Method">
//        switch (aCommand) {
//            default:
//                System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
//                break;
//        }
        //</editor-fold>
    }

    private AlbumEN getSelectedAlbum() {
        if (tableAlbums.getSelectedRow() == -1) {
            return null;
        } else {
            return AlbumDAO.select(tableAlbums.getValueAt(tableAlbums.getSelectedRow(), 1).toString());
        }
    }

    private ImageEN getSelectedImage() {
        if (tableImages.getSelectedRow() == -1) {
            return null;
        } else {
            return ImageDAO.select(tableImages.getValueAt(tableImages.getSelectedRow(), 0).toString());
        }

    }

    public void updateImageTable() {
        //<editor-fold defaultstate="collapsed" desc="Update Image Table">
        DefaultTableModel dtm = (DefaultTableModel) tableImages.getModel();
        dtm.setRowCount(0);
        for (ImageEN link : ImageDAO.list()) {
            addTableRow(tableImages, new Object[]{link.getLink(), link.getDescription(), link.getDateString()});
        }
        //</editor-fold>
    }

    public void updateAlbumTable() {
        //<editor-fold defaultstate="collapsed" desc="Update Album Table">
        DefaultTableModel dtm = (DefaultTableModel) tableAlbums.getModel();
        dtm.setRowCount(0);
        for (AlbumEN album : AlbumDAO.list()) {
            addTableRow(tableAlbums, new Object[]{album.getTitle(), album.getLink()});
        }
        //</editor-fold>
    }

    private void addTableRow(JTable table, Object[] obj) {
        //<editor-fold defaultstate="collapsed" desc="Add Table Row">
        DefaultTableModel jtm = (DefaultTableModel) table.getModel();
        jtm.addRow(obj);
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="Create Tray Functions (Tray icon functionality)">
    private void createTrayFunctions() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            outputError("Unable to set LookAndFeel.");
        }

        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
            PopupMenu popup = new PopupMenu();
            generateTrayPopupMenu(popup);
            trayIcon = new TrayIcon(image, getTitle(), popup);
            generateTrayIconAction();
            trayIcon.setImageAutoSize(true);
        } else {
            outputError("System tray not supported.");
        }

        generateWindowStateListener();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Generate WindowStateListener (Tray icon functionality)">
    private void generateWindowStateListener() {
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        outputError("Unable to add to tray.");
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        outputError("Unable to add to system tray.");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }

        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Generate Tray Icon Action (Tray icon functionality)">
    private void generateTrayIconAction() {
        trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    setVisible(true);
                    try {
                        SystemTray.getSystemTray().remove(trayIcon);
                        setState(NORMAL);
                    } catch (Exception ex) {
                        outputError(ex.getMessage());
                    }
                }
            }

        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Generate Tray PopupMenu (Tray icon functionality)">
    private void generateTrayPopupMenu(PopupMenu popup) {
        MenuItem defaultItem;

        ActionListener openListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            }

        };

        defaultItem = new MenuItem("Open");
        defaultItem.addActionListener(openListener);
        popup.add(defaultItem);

        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        };

        defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(exitListener);
        popup.add(defaultItem);
    }
    //</editor-fold>

    public String getClientID() {
        return defaultClientID;
    }

    public void outputError(String text) {
        txtAreaInfo.append("[ERROR] " + text);
        txtAreaInfo.append("\n");
    }

    public void outputInfo(String text) {
        txtAreaInfo.append("[INFO] " + text);
        txtAreaInfo.append("\n");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaInfo = new javax.swing.JTextArea();
        btnHelpInfo = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableImages = new javax.swing.JTable();
        comboShortcut = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAlbums = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        chkbPrintActWin = new javax.swing.JCheckBox();
        btnNewAlbum = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Imgur Uploader by Simego");
        setName("Imgur Uploader by Simego"); // NOI18N
        setResizable(false);

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        jLabel2.setText("Images: (double-click to open link or edit description)");

        jLabel3.setText("Console:");

        txtAreaInfo.setEditable(false);
        txtAreaInfo.setColumns(20);
        txtAreaInfo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtAreaInfo.setLineWrap(true);
        txtAreaInfo.setRows(5);
        txtAreaInfo.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtAreaInfo);

        btnHelpInfo.setText("Help / Information");
        btnHelpInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpInfoActionPerformed(evt);
            }
        });

        tableImages.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tableImages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Link", "Description", "Date (day/month/year HH:mm:ss)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableImages.setFillsViewportHeight(true);
        tableImages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableImagesMouseClicked(evt);
            }
        });
        tableImages.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tableImagesFocusLost(evt);
            }
        });
        tableImages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableImagesKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tableImages);

        comboShortcut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CTRL + SHIFT + C", "PRINT SCREEN" }));

        jLabel1.setText("Shortcut:");

        tableAlbums.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tableAlbums.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Link"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAlbums.setFillsViewportHeight(true);
        tableAlbums.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAlbumsMouseClicked(evt);
            }
        });
        tableAlbums.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableAlbumsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tableAlbums);

        jLabel4.setText("Albums: (images will be uploaded to the selected album)");

        chkbPrintActWin.setText("Print active window only?");
        chkbPrintActWin.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkbPrintActWin.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnNewAlbum.setText("Create new Album");
        btnNewAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAlbumActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(356, 356, 356)
                                .addComponent(jLabel4)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkbPrintActWin)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(67, 67, 67)
                                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnHelpInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNewAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chkbPrintActWin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(btnHelpInfo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNewAlbum))))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Stop Button Action"> 
        stopKeyCapture();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        comboShortcut.setEnabled(true);
        //</editor-fold>
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Start Button Action">
        startKeyCapture();
        btnStop.setEnabled(true);
        btnStart.setEnabled(false);
        comboShortcut.setEnabled(false);
        //</editor-fold>
    }//GEN-LAST:event_btnStartActionPerformed

    private void tableImagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableImagesMouseClicked
        //<editor-fold defaultstate="collapsed" desc="Image Table Mouseclick (open links/edit description)">
        JTable table = (JTable) evt.getSource();
        if (evt.getClickCount() == 2) {
            switch (table.getSelectedColumn()) {
                case 0:
                    URI uri;
                    try {
                        uri = new URI((String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
                        Desktop.getDesktop().browse(uri);
                    } catch (URISyntaxException | IOException ex) {
                        outputError(ex.getMessage());
                    }
                    break;
                case 1:
                    String newDescription = JOptionPane.showInputDialog(this, "Description [max 20 digits]:", "Change description", JOptionPane.INFORMATION_MESSAGE);
                    if (newDescription != null) {
                        int descSize = newDescription.length();
                        table.setValueAt(newDescription.substring(0, descSize >= 20 ? 20 : descSize), table.getSelectedRow(), table.getSelectedColumn());
                        ImageDAO.updateDescription(table.getValueAt(table.getSelectedRow(), 0).toString(), newDescription.substring(0, descSize >= 20 ? 20 : descSize));
                    }
                    break;
            }
        }
        //</editor-fold>
    }//GEN-LAST:event_tableImagesMouseClicked

    private void btnHelpInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpInfoActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Help Button Action">
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Simego/ImgurUploader/blob/master/README.md"));
        } catch (IOException | URISyntaxException ex) {
            outputError(ex.getMessage());
        }
        //</editor-fold>
    }//GEN-LAST:event_btnHelpInfoActionPerformed

    private void tableImagesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableImagesKeyPressed
        //<editor-fold defaultstate="collapsed" desc="Image Table Keypress (delete entries)">
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected entries?", "Delete Image", JOptionPane.YES_NO_OPTION) == 0) {
                JTable table = (JTable) evt.getSource();
                for (int i : table.getSelectedRows()) {
                    ImageEN image = ImageDAO.select(table.getValueAt(i, 0).toString());
                    if (imgurClient.imageDelete(getClientID(), image.getDeletehash())) {
                        outputInfo("Image " + image.getLink() + " has been deleted.");
                        ImageDAO.delete(table.getValueAt(i, 0).toString());
                    }
                }
                updateImageTable();
            }
        }
        //</editor-fold>
    }//GEN-LAST:event_tableImagesKeyPressed

    private void tableAlbumsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAlbumsMouseClicked
        //<editor-fold defaultstate="collapsed" desc="Album Table Mouseclick (open links)">
        JTable table = (JTable) evt.getSource();
        if (table.rowAtPoint(evt.getPoint()) == -1) {
            table.clearSelection();
        }
        if (evt.getClickCount() == 2) {
            URI uri;
            try {
                uri = new URI(table.getValueAt(table.getSelectedRow(), 1).toString());
                Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException | IOException ex) {
                outputError(ex.getMessage());
            }
        }
        //</editor-fold>
    }//GEN-LAST:event_tableAlbumsMouseClicked

    private void tableAlbumsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableAlbumsKeyPressed
        //<editor-fold defaultstate="collapsed" desc="Album Table Keypress (delete entries)">
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected entries?", "Delete Album", JOptionPane.YES_NO_OPTION) == 0) {
                JTable table = (JTable) evt.getSource();
                for (int i : table.getSelectedRows()) {
                    AlbumEN album = AlbumDAO.select(table.getValueAt(i, 1).toString());
                    if (imgurClient.albumDelete(getClientID(), album.getDeletehash())) {
                        outputInfo("Album " + album.getLink() + " has been deleted.");
                        AlbumDAO.delete(table.getValueAt(i, 1).toString());
                    }
                }
                updateAlbumTable();
            }
        }
        //</editor-fold>
    }//GEN-LAST:event_tableAlbumsKeyPressed

    private void btnNewAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAlbumActionPerformed
        //<editor-fold defaultstate="collapsed" desc="New Album Button Action">
        NewAlbum newAlbumForm = new NewAlbum(this, true);
        newAlbumForm.setVisible(true);
        //</editor-fold>
    }//GEN-LAST:event_btnNewAlbumActionPerformed

    private void tableImagesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tableImagesFocusLost
        JTable table = (JTable) evt.getSource();
        table.clearSelection();
    }//GEN-LAST:event_tableImagesFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHelpInfo;
    private javax.swing.JButton btnNewAlbum;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JCheckBox chkbPrintActWin;
    private javax.swing.JComboBox comboShortcut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableAlbums;
    private javax.swing.JTable tableImages;
    private javax.swing.JTextArea txtAreaInfo;
    // End of variables declaration//GEN-END:variables

}
