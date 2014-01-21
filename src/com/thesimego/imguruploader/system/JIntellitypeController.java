package com.thesimego.imguruploader.system;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.thesimego.imguruploader.dao.ImageDAO;
import com.thesimego.imguruploader.entity.AlbumEN;
import com.thesimego.imguruploader.entity.imgur.ImgurImage;
import com.thesimego.imguruploader.view.Main;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Simego
 */
public class JIntellitypeController implements HotkeyListener, IntellitypeListener {

    private Main mainView;

    private static final int PRINT_SCREEN = 91;

    public JIntellitypeController(Main mainView) {
        this.mainView = mainView;
        
        try {
            initJIntellitype();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Init Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
    }

    private void initJIntellitype() {
        //<editor-fold defaultstate="collapsed" desc="Init JIntellitype">
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
            mainView.outputInfo("After START, this system will override the selected SHORTCUT function until it STOP.");
            mainView.outputInfo("If you can't upload images on 'default user', click the Help button and find out how to create a ClientID.");
        } catch (HeadlessException ex) {
            mainView.outputError("Either you are not on Windows, or there is a problem with the JIntellitype library!");
        }
        //</editor-fold>
    }
    
    public void startKeyCapture() {
        //<editor-fold defaultstate="collapsed" desc="Start Key Capture (JIntellitype)">
        int comboIndex = mainView.getComboShortcut().getSelectedIndex();
        switch (comboIndex) {
            case 0:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, 'C');
                break;
            case 1:
                JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, 0, 44);
                break;
        }

        mainView.outputInfo("System started, screenshots will be uploaded to your Imgur account.");
        mainView.outputInfo("Hotkey: " + mainView.getComboShortcut().getSelectedItem().toString());
        //</editor-fold>
    }

    public void stopKeyCapture() {
        //<editor-fold defaultstate="collapsed" desc="Stop Key Capture (JIntellitype)">
        JIntellitype.getInstance().unregisterHotKey(PRINT_SCREEN);
        mainView.outputInfo("System stopped.");
        //</editor-fold>
    }

    @Override
    public void onHotKey(int aIdentifier) {
        //<editor-fold defaultstate="collapsed" desc="JIntellitype onHotKey Override Method">
        ImgurImage imgur;
        AlbumEN album = mainView.getSelectedAlbum();

        imgur = mainView.getImgurClient().doImageUpload(mainView.getClientID(), mainView.getCheckBoxPrintActiveWindow().isSelected(), album == null ? null : album.getDeletehash());

        if (imgur != null) {
            mainView.outputInfo("Upload complete.");
            mainView.outputInfo(imgur.getData().toString());
            ImageDAO.insert(imgur.getData().getLink(), imgur.getData().getDateString(), imgur.getData().getDeletehash());
            mainView.updateImageTable();
        } else {
            mainView.outputError("Problem trying to upload the image, maybe wrong client id or connection issue? try again.");
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
    
}
