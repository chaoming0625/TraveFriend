/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author ChaoMing
 */
public class PosWriter {
    private DataOutputStream mHandle;
    private int mSize;
    private long mRecordTime;
    private int mCallTicks;
    private int mInvalidTicks;
    private String mFileName;
    private Date mOpenTime;
    protected PosHead mHead = new PosHead();
    public PosWriter(){
        
    }

    public void Open() throws IOException {
        mFileName = NaviPrefs.GetDataPath();
        mOpenTime = new Date();
        switch( NaviPrefs.mLogMode ){
            case 0://����¼
                return;
            case 1://ÿ��һ��
                mFileName += TimeUtils.getTime(mOpenTime,"yyyyMMdd");
                mFileName +=(".gd");
                break;
            case 2:
                mFileName +=TimeUtils.getTime(mOpenTime,"yyyyMMddHHmmss");
                mFileName +=(".gd");
                break;
            case 3:
                mFileName += NaviPrefs.mLogName;
                if( mFileName.indexOf(".gd")<2 ) mFileName +=(".gd");
                break;
        }
        mSize = 0;
        File f = new File(mFileName);
        //mRecordTime = CTime::GetCurrentTime();
        mInvalidTicks = 0;
        mCallTicks = 0;
        mRecordTime = 0;
        long length = f.length();
        if( NaviPrefs.mLogMode==3 ){
            if( f.isFile() && f.exists() ) f.delete();
            length = 0;
        }
        if( length < PosHead.SIZE ){
            if( f.exists() && f.isFile() ) {
                f.delete();
                length =0;
            }
        }

        if( length !=0 ){
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            mSize = (int)(length-PosHead.SIZE)/PosRecord.SIZE;
            try {
                mHead.read(in);
            } finally{
                in.close();
            }
        }
        mHandle = new DataOutputStream(new FileOutputStream(f,true) );
        if( length==0 ){
            mHead.recordtime = TimeUtils.getCurrentTimeInLong();
            mHead.magic = 0x4744;
            mHead.packsize = PosRecord.SIZE;
            mHead.write(mHandle);
        }

    }
    public boolean isReady(){ return ( mHandle!=null); }
    public void Close(){
        try {
            if( mHandle!=null) mHandle.close();
        } catch (IOException e) {
        }
        mHandle = null;
    }
    public void Record(PosRecord info){
        if(NaviPrefs.mLogMode==0) return ;
        if( mHandle==null) return;
        mCallTicks++;
        if( mCallTicks==NaviPrefs.mRecordRate ) mCallTicks = 0;
        if( mCallTicks!=0 ) return;
        int d = 1;
        if( info.good() ) mInvalidTicks =0;
        else {
            mInvalidTicks ++;
            if( mInvalidTicks>NaviPrefs.mInvalidPos ) d=0;
        }
        if( d!=0){
            Check();
            try {
                info.write(mHandle);
            } catch (IOException e) {

            }
        }
    }
    public String Name(){ return mFileName; }
    public void NewTrack(){
        if(NaviPrefs.mLogMode==0) return ;
        if( mHead.tracks<PosHead.TRACK_MAX ){
            mHead.index[mHead.tracks]=mSize;
            mHead.tracks++;
            try {
                mHandle.close();
                mHandle = new DataOutputStream(new FileOutputStream(mFileName, false));
                mHead.write(mHandle);
                mHandle.close();
                mHandle = new DataOutputStream(new FileOutputStream(mFileName, true));
            }catch (IOException e){
                mHandle = null;
            }
        }

    }
    public void Check(){
        if( NaviPrefs.mLogMode == 1 ){
            if( TimeUtils.getDays(mOpenTime)!=  TimeUtils.getDays(new Date())){
                Close();
                try {
                    Open();
                } catch (IOException e) {

                }
            }
        }
    }
}
