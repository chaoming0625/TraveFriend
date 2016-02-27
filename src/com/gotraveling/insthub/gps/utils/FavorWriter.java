package com.gotraveling.insthub.gps.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ChaoMing on 2015/4/16.
 */
public class FavorWriter {
    protected DataOutputStream mHandle=null;
    public FavorWriter(){

    }
    public void Open() throws IOException {
        String name = NaviPrefs.GetDataPath();
        name +=("current.gd");
        File file = new File(name);
        long len = file.length();
        mHandle = new DataOutputStream(new FileOutputStream(file,true) );
        if( len ==0 ){
            PosHead head = new PosHead();
            head.recordtime = TimeUtils.getCurrentTimeInLong();
            head.packsize = PosRecord.SIZE;
            head.type = 0;
            head.write(mHandle);
        }
    }
    public void Close(){
        if( mHandle!=null){
            try {
                mHandle.close();
            }catch (Exception e){}
            mHandle = null;
        }
    }
    public void Record(PosRecord pInfo){
        try {
            pInfo.write(mHandle);
        } catch (IOException e) {

        }
    }
}
