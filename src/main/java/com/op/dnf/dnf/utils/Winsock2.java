package com.op.dnf.dnf.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
/**
 * @author xiawei
 * @version 1.0
 * @date 2023/11/30 10:52
 */
public class Winsock2  {


    public interface Load extends Library {
        com.sun.jna.platform.win32.Winsock2 INSTANCE = (com.sun.jna.platform.win32.Winsock2) Native.load("ws2_32", com.sun.jna.platform.win32.Winsock2.class, W32APIOptions.ASCII_OPTIONS);

        int gethostname(byte[] var1, int var2);


    }
}
