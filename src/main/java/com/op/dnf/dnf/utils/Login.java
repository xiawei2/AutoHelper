package com.op.dnf.dnf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author xiawei
 * @version 1.0
 * @date 2023/11/28 20:20
 */
public class Login {
    public static String ao = getMainBordId_windows();

    static String hass = "";
    public static String[] login(String username, String password) {
        String lines = "";
        try {
            System.out.println(username);
            username = new String(getUTF8BytesFromGBKString(username),"UTF-8");
            String encode = URLEncoder.encode(username, "utf-8");
            hass = MD5Utils.stringToMD5(username + password + ao + "中文123说的是多大111");
            String getURL = "https://www.fzzixue.com/kb.php?user=" + encode + "&password=" + password + "&hass=" + hass + "&ao=" + ao + "&soft=10";
            getURL = new String(getUTF8BytesFromGBKString(getURL),"UTF-8");
            URL getUrl = new URL(getURL);
            URLConnection conn=getUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            lines = reader.readLine();
            reader.close();
        }catch (Exception e){
            System.out.println(e);
        }
        String[] split = lines.split("\\|\\|");
        return split;
    }
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    public static String getMainBordId_windows() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + " (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + " Wscript.Echo objItem.ProcessorId \n"
                    + " exit for ' do the first cpu only! \n" + "Next \n";
// + " exit for \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();

    }

}
