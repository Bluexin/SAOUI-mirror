package com.bluexin.saoui;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class VersionChecker { // TODO: handle indev vs public

    private static int getLocaleVer() throws IOException
    {
        InputStream input = VersionChecker.class.getResourceAsStream("/assets/saoui/version.txt");
        if (input == null) throw new IOException("InputStream null!");
        String content = IOUtils.toString(input, StandardCharsets.UTF_8).replace("\r\n", "").replace("\n", "");
        IOUtils.closeQuietly(input);

        return Integer.parseInt(content);
    }

    private static int getRemoteVer() throws IOException {
        InputStream input = new URL("https://drone.io/github.com/Bluexin/SAOUI/files/build/libs/version.txt").openStream();
        String content = IOUtils.toString(input, StandardCharsets.UTF_8).replace("\r\n", "").replace("\n", "");
        IOUtils.closeQuietly(input);

        return Integer.parseInt(content);
    }

    private static String getChanges() throws IOException {
        InputStream input = new URL("https://drone.io/github.com/Bluexin/SAOUI/files/build/libs/latestChanges.txt").openStream();
        String content = IOUtils.toString(input, StandardCharsets.UTF_8);
        IOUtils.closeQuietly(input);

        return content;
    }

    public static String getUpdateNotif() {
        String msg = "";
        int locale = -1, remote = -1;

        try {
            locale = getLocaleVer();
        } catch (IOException e) {
            msg += "Something went wrong when checking for the locale SAO UI version";
        }
        try {
            remote = getRemoteVer();
        } catch (IOException e) {
            if (!msg.equals("")) msg += '\n';
            msg += "Something went wrong when checking for the remote SAO UI version";
        }

        if (locale != -1 && remote != -1) {
            if (locale == remote) msg = "The SAO UI is up-to-date.";
            else {
                msg = "The SAO UI is not up-to-date.\n";
                msg += "Current version: " + locale + ". Latest version: " + remote + ".\n";
                try {
                    msg += "Latest changes:\n" + getChanges();
                } catch (IOException e) {
                    msg += "Error when reading latest changes.";
                }
            }
        }

        return msg;
    }
}
