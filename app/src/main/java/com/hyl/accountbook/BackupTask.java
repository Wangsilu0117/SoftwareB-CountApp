package com.hyl.accountbook;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupTask extends AsyncTask<String, Void, Integer> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restoreDatabase";
    private Context mContext;

    public BackupTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        // 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db　　　　 // 默认路径是 /data/data/(包名)/databases/*.db
        File dbFile = mContext.getDatabasePath("/data/data/com.hyl.accountbook/databases/accountbook.db");
        //Environment
        //                .getExternalStorageDirectory().getAbsolutePath()
        //                +
        //
        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "accountbook_Backup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                fileCopy(dbFile, backup);
                Looper.prepare();
                Toast.makeText(mContext, "数据备份成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return Log.d("backup", "ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(mContext, "数据备份失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return Log.d("backup", "fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                Looper.prepare();
                Toast.makeText(mContext, "数据恢复成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return Log.d("restore", "success");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(mContext, "数据恢复失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return Log.d("restore", "fail");
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}