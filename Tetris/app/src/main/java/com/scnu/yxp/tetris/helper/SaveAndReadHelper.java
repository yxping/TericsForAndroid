package com.scnu.yxp.tetris.helper;

import android.content.Context;
import android.util.Log;

import com.scnu.yxp.tetris.logic.Bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by yxp on 2015/10/21.
 */
public class SaveAndReadHelper {

    /**
     * 存储数据到/data/data/package/files/bean文件中
     * @param context
     * @param bean
     */
    public static void saveObjectDataToFile(Context context, Bean bean) {
        if(context.deleteFile("bean")) {
            Log.i("delete bean", "successful");
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput("bean", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(bean);
            Log.i("save data", "successful");
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("save data", "failure");
    }

    /**
     * 从bean文件中读取数据
     * @param context
     * @return
     */
    public static Bean readObjectDataFromFile(Context context){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("bean");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.i("read data", "successful");
            return (Bean)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("read data", "failure");
        return null;
    }

}
