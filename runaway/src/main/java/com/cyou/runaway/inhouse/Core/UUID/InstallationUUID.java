package com.cyou.runaway.inhouse.Core.UUID;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Created by Gang on 2016/10/9.
 */

public class InstallationUUID
{
    protected static String msUUID = null;
    protected static final String INSTALLATION = "INSTALLATION";

    public synchronized static String getUUID(Context context)
    {
        if (null == msUUID)
        {
            File installation = new File(context.getFilesDir(), INSTALLATION);

            try
            {
                if (!installation.exists())
                    writeInstallationFile(installation);

                msUUID = readInstallationFile(installation);

            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        return msUUID;
    }

    protected static String readInstallationFile(File installation) throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) file.length()];
        file.readFully(bytes);
        file.close();
        return new String(bytes);
    }

    protected static void writeInstallationFile(File installation) throws IOException
    {
        FileOutputStream out = new FileOutputStream(installation);
        String id = java.util.UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
