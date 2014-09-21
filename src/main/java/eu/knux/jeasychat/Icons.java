package eu.knux.jeasychat;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 17/09/14.
 */
public class Icons {

    private static HashMap<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();

    public static void loadIcons() {
        iconMap.clear();
        loadFiles(Resources.smileyFile);
        for (String key : iconMap.keySet()) {
            Main.console.log(Level.FINE, "Image chargée: " + key);
        }
    }

    private static void loadFiles(String zipname) {
        try {
            ZipInputStream zin = new ZipInputStream(
                    new FileInputStream(zipname));
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.getName().endsWith(".png")) {
                    String name = entry.getName();
                    iconMap.put(name.substring(0, name.lastIndexOf(".png")), new ImageIcon(readImage(zin)));
                }
                zin.closeEntry();
            }
            zin.close();
        } catch (IOException e) {
        }
    }

    private static byte[] readImage(ZipInputStream zin) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(zin);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384]; // need to be preciser :(
        while((nRead = bis.read(data, 0, data.length)) != -1) {
            bos.write(data, 0, nRead);
        }
        bos.flush();
        return bos.toByteArray();
    }

}