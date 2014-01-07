package com.thesimego.imguruploader.view;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.thesimego.imguruploader.dao.LinkDAO;
import com.thesimego.imguruploader.entity.Imgur;
import com.thesimego.imguruploader.entity.Link;
import com.thesimego.imguruploader.system.Functions;
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
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Simego
 */
public class MainView extends javax.swing.JFrame implements HotkeyListener, IntellitypeListener {

    private static final int PRINT_SCREEN = 91;
    private final LinkDAO linkDAO = new LinkDAO();
    private final Functions functions = new Functions(this);
    private TrayIcon trayIcon;
    private SystemTray tray;

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
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainView main = new MainView();
                main.setVisible(true);
            }
        });
    }

    public MainView() {
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        setLocationRelativeTo(null);
        updateTable();
        createTrayFunctions();
        try {
            initJIntellitype();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Init Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        txtClientID.setText(functions.loadClientID());
        btnParar.setEnabled(false);
    }

    private void initJIntellitype() {
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
            output("[INFO] If you choose PRINT SCREEN as shortcut,the normal PRINT SCREEN won't work\nafter you START the program and until you STOP it.", txtAreaInfo);
        } catch (HeadlessException ex) {
            output("Either you are not on Windows, or there is a problem with the JIntellitype library!", txtAreaInfo);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnIniciar = new javax.swing.JButton();
        btnParar = new javax.swing.JButton();
        txtClientID = new javax.swing.JTextField();
        lblClientID = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaInfo = new javax.swing.JTextArea();
        tbtnPrintType = new javax.swing.JToggleButton();
        btnHelpInfo = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableImages = new javax.swing.JTable();
        comboShortcut = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Imgur Uploader by Simego");
        setName("Imgur Uploader by Simego"); // NOI18N
        setResizable(false);

        btnIniciar.setText("Start");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnParar.setText("Stop");
        btnParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararActionPerformed(evt);
            }
        });

        txtClientID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClientIDFocusLost(evt);
            }
        });

        lblClientID.setText("Imgur Client ID:");

        jLabel2.setText("Images: (double-click to open link or edit description)");

        jLabel3.setText("Console:");

        txtAreaInfo.setEditable(false);
        txtAreaInfo.setColumns(20);
        txtAreaInfo.setRows(5);
        jScrollPane2.setViewportView(txtAreaInfo);

        tbtnPrintType.setText("Print only active window?");

        btnHelpInfo.setText("Help / Information");
        btnHelpInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpInfoActionPerformed(evt);
            }
        });

        tableImages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Link", "Description", "Date"
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
        tableImages.setCellSelectionEnabled(true);
        tableImages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableImagesMouseClicked(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblClientID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtClientID)
                                .addGap(2, 2, 2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnIniciar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnParar))
                            .addComponent(comboShortcut, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnHelpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tbtnPrintType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar)
                    .addComponent(btnParar)
                    .addComponent(txtClientID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClientID)
                    .addComponent(tbtnPrintType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboShortcut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(btnHelpInfo))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararActionPerformed
        stopKeyCapture();
        btnIniciar.setEnabled(true);
        btnParar.setEnabled(false);
    }//GEN-LAST:event_btnPararActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        if (txtClientID.getText().trim().isEmpty()) {
            output("Please type your Imgur Client ID.", txtAreaInfo);
        } else {
            startKeyCapture();
            btnParar.setEnabled(true);
            btnIniciar.setEnabled(false);
        }
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void tableImagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableImagesMouseClicked
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
                        linkDAO.updateDescription(table.getValueAt(table.getSelectedRow(), 0).toString(), newDescription);
                    }
                    break;
            }
        }
    }//GEN-LAST:event_tableImagesMouseClicked

    private void btnHelpInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpInfoActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Simego/ImgurUploader/blob/master/README.md"));
        } catch (IOException | URISyntaxException ex) {
            outputError(ex.getMessage());
        }
    }//GEN-LAST:event_btnHelpInfoActionPerformed

    private void tableImagesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableImagesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected entries?", "Delete entry", JOptionPane.YES_NO_OPTION) == 0) {
                JTable table = (JTable) evt.getSource();
                for (int i : table.getSelectedRows()) {
                    linkDAO.delete(table.getValueAt(i, 0).toString());
                }
                updateTable();
            }
        }
    }//GEN-LAST:event_tableImagesKeyPressed

    private void txtClientIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClientIDFocusLost
        functions.saveClientID(txtClientID.getText());
    }//GEN-LAST:event_txtClientIDFocusLost

    private void startKeyCapture() {
        int comboIndex = comboShortcut.getSelectedIndex();
        switch (comboIndex) {
            case 0:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, 'C');
                break;
            case 1:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, 0, 44);
                break;
        }

        output("System started, screenshots will be uploaded to your Imgur account.", txtAreaInfo);
        output("Hotkey: " + comboShortcut.getSelectedItem().toString(), txtAreaInfo);
    }

    private void stopKeyCapture() {
        JIntellitype.getInstance().unregisterHotKey(PRINT_SCREEN);
        output("System stopped.", txtAreaInfo);
    }

    @Override
    public void onHotKey(int aIdentifier) {
        Imgur imgur;
        if (tbtnPrintType.isSelected()) {
            imgur = functions.getImgurData(txtClientID.getText(), true);
        } else {
            imgur = functions.getImgurData(txtClientID.getText(), false);
        }
        if (imgur != null) {
            output("[INFO] Upload complete.", txtAreaInfo);
            output("[INFO] " + imgur.getData().toString(), txtAreaInfo);
            linkDAO.insert(imgur.getData().getLink(), null, imgur.getData().getDateString());
            updateTable();
        } else {
            outputError("Problem trying to upload the image, maybe wrong client id or connection issue? try again.");
        }
    }

    @Override
    public void onIntellitype(int aCommand) {
//        switch (aCommand) {
//            default:
//                System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
//                break;
//        }
    }

    private void updateTable() {
        DefaultTableModel dtm = (DefaultTableModel) tableImages.getModel();
        //dtm.setNumRows(0);
        dtm.setRowCount(0);
        for (Link link : linkDAO.list()) {
            addTableRow(link.getLink(), link.getDescription(), link.getDateString());
        }
    }

    private void addTableRow(String link, String description, String date) {
        DefaultTableModel jtm = (DefaultTableModel) tableImages.getModel();
        jtm.addRow(new Object[]{link, description, date});
    }

    public JTextArea getInfoTextArea() {
        return txtAreaInfo;
    }

    public void output(String text, JTextArea ta) {
        ta.append(text);
        ta.append("\n");
    }

    public void outputError(String text) {
        txtAreaInfo.append("[ERROR] " + text);
        txtAreaInfo.append("\n");
    }

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
            generateActionTrayIcon();
            trayIcon.setImageAutoSize(true);
        } else {
            outputError("System tray not supported.");
        }

        generateWindowStateListener();
    }

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

    private void generateActionTrayIcon() {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHelpInfo;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnParar;
    private javax.swing.JComboBox comboShortcut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblClientID;
    private javax.swing.JTable tableImages;
    private javax.swing.JToggleButton tbtnPrintType;
    private javax.swing.JTextArea txtAreaInfo;
    private javax.swing.JTextField txtClientID;
    // End of variables declaration//GEN-END:variables

}
