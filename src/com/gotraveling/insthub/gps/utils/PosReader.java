/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 *
 * @author ChaoMing
 */
public class PosReader {
    protected  String mFileName;
    private DataInputStream mHandle;
    private int mTrackIndex;
    private int mPosIndex;
    private int mPosMax;
    private PosHead mHead = new PosHead();
    private PosRecord mRecord = new PosRecord();
    private GpsInfo mNmea = new GpsInfo();
    private TransBean mBean ;
    private String mTimeFmt = "%Y-%m-%dT%H:%M:%S.%s%Z";;
    public PosReader(TransBean bean){
        mBean = bean;
    }
    protected String fmtTime(String fmt,long time,int ns){
        String fmt2 ;
        if( (fmt!=null) && (fmt.length()>0) ) fmt2 = fmt; else fmt2 =mTimeFmt;
        StringBuffer buffer =   new StringBuffer("");
        String[] data = TimeUtils.getTime(time,"yyyy:MM:dd:HH:hh:mm:ss:SSS").split(":");
            for(int i=0;i<fmt2.length();i++){
                if(fmt2.charAt(i)=='%'){
                    i++;
                    switch(fmt2.charAt(i)){
                        //%a
                        //Abbreviated weekday name
                        //%A
                        //Full weekday name
                        //%b
                        //Abbreviated month name
                        //%B
                        //Full month name
                        //%c
                        //Date and time representation appropriate for locale
                        case 'c':
                            break;
                        //%d
                        //Day of month as decimal number (01 �C 31)
                        case 'd':
                            buffer.append(data[2]);
                            break;
                        //%H
                        //Hour in 24-hour format (00 �C 23)
                        case 'H':
                            buffer.append(data[3]);
                            break;
                        //%I
                        //Hour in 12-hour format (01 �C 12)
                        case 'I':
                            buffer.append(data[4]);
                            break;
                        //%j
                        //Day of year as decimal number (001 �C 366)
                        //%m
                        //Month as decimal number (01 �C 12)
                        case 'm':
                            buffer.append(data[1]);
                            break;
                        //%M
                        //Minute as decimal number (00 �C 59)
                        case 'M':
                            buffer.append(data[5]);
                            break;
                        //%p
                        //Current locale's A.M./P.M. indicator for 12-hour clock
                        //%S
                        //Second as decimal number (00 �C 59)
                        case 'S':
                            buffer.append(data[6]);
                            break;
                        case 's':
                            buffer.append(String.format("%03d",ns));
                            break;
                        //%U
                        //Week of year as decimal number, with Sunday as first day of week (00 �C 53)
                        //%w
                        //Weekday as decimal number (0 �C 6; Sunday is 0)
                        //%W
                        //Week of year as decimal number, with Monday as first day of week (00 �C 53)
                        //%x
                        //Date representation for current locale
                        //%X
                        //Time representation for current locale
                        //%y
                        //Year without century, as decimal number (00 �C 99)
                        case 'y':
                            buffer.append(data[0].substring(2));
                            break;
                        //%Y
                        //Year with century, as decimal number
                        case 'Y':
                            buffer.append(data[0]);
                            break;
                        //%z, %Z
                        //Either the time-zone name or time zone abbreviation, depending on registry settings; no characters if time zone is unknown
                        case 'z':
                        case 'Z':
                            if( !mBean.mUseLocal ) buffer.append(fmt2.charAt(i));
                            break;
                        default:
                            buffer.append(fmt2.charAt(i));
                    }
                } else
                    buffer.append(fmt2.charAt(i));
            }
        return buffer.toString();
    }
//    #define OutVar(a,fmt2,var) if( _stricmp(name,a)==0 ){ sprintf(buffer,fmt?fmt:fmt2,var); return 1; }
protected String FavorValue(String fmt,String name)
{
    if( name.compareToIgnoreCase("lon")==0){
        return String.format(fmt==null?"%.6f":fmt,GpsMath.ndeg2degree(mNmea.lon));
    }
    if( name.compareToIgnoreCase("lat")==0){
        return String.format(fmt==null?"%.6f":fmt,GpsMath.ndeg2degree(mNmea.lat));
    }

    //OutVar("lon","%.6f",mNmea.longitude);
    //OutVar("lat","%.6f",mNmea.latitude);
    if( name.compareToIgnoreCase("alti")==0){
        return String.format(fmt==null?"%.1f":fmt,mNmea.elv);
    }
    if( name.compareToIgnoreCase("sattime")==0){
        return fmtTime(fmt,mNmea.utc_t,(int)(mNmea.utc_t%1000));
    }
    if( name.compareToIgnoreCase("sig")==0){
        return String.format(fmt==null?"%d":fmt,mNmea.sig);
    }
    if( name.compareToIgnoreCase("inuse")==0){
        return String.format(fmt==null?"%d":fmt,mNmea.satinfo.inuse);
    }
    if( name.compareToIgnoreCase("inview")==0){
        return String.format(fmt==null?"%d":fmt,mNmea.satinfo.inview);
    }
    if( name.compareToIgnoreCase("pdop")==0){
        return String.format(fmt==null?"%.2f":fmt,mNmea.PDOP);
    }
    if( name.compareToIgnoreCase("hdop")==0){
        return String.format(fmt==null?"%.2f":fmt,mNmea.HDOP);
    }
    if( name.compareToIgnoreCase("vdop")==0){
        return String.format(fmt==null?"%.2f":fmt,mNmea.VDOP);
    }
    if( name.compareToIgnoreCase("fix")==0){
        if( mNmea.fix==2 )		return "2d";
        else  if( mNmea.fix==3 ) return "3d";
        else if( mNmea.fix==1 ) return "none";
        else return Integer.toString(mNmea.fix);
    }
    return "";
}
    protected  String PosValue(String fmt,String name) {
        if( name == null ) return "";
        if( name.length()<1 ) return "";
        if( fmt.length()<1 ) fmt = null;
        if( name.compareToIgnoreCase("lon")==0)
            return String.format(fmt!=null?fmt:"%.6f",GpsMath.ndeg2degree(mRecord.longitude));

        if( name.compareToIgnoreCase("lat")==0)
            return String.format(fmt!=null?fmt:"%.6f",GpsMath.ndeg2degree(mRecord.latitude));
        //OutVar("lon","%.6f",mData->longitude);
        //OutVar("lat","%.6f",mData->latitude);
        if( name.compareToIgnoreCase("alti")==0)
            return String.format(fmt != null ? fmt : "%.1f", mRecord.altitude);
        if( name.compareToIgnoreCase("sattime")==0)
            return fmtTime(fmt, mRecord.SatTime * 1000, mRecord.nsSatTime);
        if( name.compareToIgnoreCase("sig")==0)
            return String.format(fmt != null ? fmt : "%d", mRecord.sig);
        if( name.compareToIgnoreCase("inuse")==0)
            return String.format(fmt!=null?fmt:"%d",mRecord.inuse);
        if( name.compareToIgnoreCase("inview")==0)
            return String.format(fmt!=null?fmt:"%d",mRecord.inview);
        if( name.compareToIgnoreCase("pdop")==0)
            return String.format(fmt!=null?fmt:"%.2f",mRecord.pdop/100.0);
        if( name.compareToIgnoreCase("hdop")==0)
            return String.format(fmt!=null?fmt:"%.2f",mRecord.hdop/100.0);
        if( name.compareToIgnoreCase("vdop")==0)
            return String.format(fmt!=null?fmt:"%.2f",mRecord.vdop/100.0);
        if( name.compareToIgnoreCase("fix")==0){
            if( mRecord.fix==2 )		return "2d";
            else  if( mRecord.fix==3 ) return "3d";
            else if( mRecord.fix==1 ) return "none";
            else return Integer.toString(mRecord.fix);
        }
        if( name.compareToIgnoreCase("spd")==0){
            return String.format(fmt!=null?fmt:"%.3f",mRecord.speed);
        }
        return "";
    }

    public void Open(String name) throws IOException{
        Close();
        mFileName = name;
        File f = new File(name);
        if( !( f.exists() && f.isFile()) ) throw new FileNotFoundException(name);
        long len = f.length();
        mHandle = new DataInputStream(new FileInputStream(f));
        mHead.read(mHandle);
        mPosMax = (int) ((len-PosHead.SIZE)/mHead.packsize);

        if( mHead.type==1 ){
           // if( mHead.packsize!= GpsInfo.SIZE ) {
                Close();
                throw new StreamCorruptedException();
            //}
        }
        mTrackIndex =0;
        mPosIndex =0;

    }
    public void Close(){
        if( mHandle!=null) try {
            mHandle.close();
        } catch (IOException e) {
        }
        mHandle = null;
    }
    public int TrackNum(){
        if( mBean.mIngorTrk ) return 1;
        return mHead.tracks+1;

    }
    public int NextTrack(){
        if( mBean.mIngorTrk ){
            if( mTrackIndex>0 ) return 0;
        } else
        if( mTrackIndex>mHead.tracks ) return 0;
        mTrackIndex++;
//	mPosIndex =0;
        return 1;
    }
    public int NextPos()  {
        if( mHandle==null ) return 0;
        if( (mHead.tracks!=0) && (mTrackIndex<=mHead.tracks) ){
            if( mPosIndex == mHead.index[mTrackIndex-1] ) return 0;
        }
        while( true ){
            try {
                if(mHead.type==1 )     mNmea.read(mHandle,mHead.packsize);
                else mRecord.read(mHandle,mHead.packsize);
            } catch (IOException e) {
                Close();
                return 0;
            }
            mPosIndex++;
            if( mHead.type==1 ) return 1;
            if( mBean.mIngorBad ){
                if( mRecord.good() ) return 1;
            } else return 1;
        }
    }
    public String Value(String fmt,String name)
    {
        if( name.compareToIgnoreCase("trktime")==0 ) {
             return fmtTime(fmt,mHead.recordtime*1000,0);
        }
        if( name.compareToIgnoreCase("trkno")==0 ) return Integer.toString(mTrackIndex);
        if( name.compareToIgnoreCase("trkcnt")==0 ) return Integer.toString(TrackNum());
        if( name.compareToIgnoreCase("poscnt")==0 ) return  Integer.toString(mPosMax);
        if( name.compareToIgnoreCase("posno")==0 ) return Integer.toString(mPosIndex);
        if( mHead.type==1 ) return FavorValue(fmt,name);
        return PosValue(fmt,name);
    }
    public void SetTimeLocal(boolean a) { mBean.mUseLocal= a; };
    public void SetTimeFmt(String fmt){
        if( fmt!=null ){
            fmt = fmt.trim();
            if( fmt.length()>1 ){
                mTimeFmt = fmt;
                return;
            }
        }
        mTimeFmt="%Y-%d-%m %H:%M:%S.%s";
    }
    public void Reset() throws IOException {
        mTrackIndex =0;
        mHandle.close();
        mHandle = new DataInputStream( new FileInputStream(mFileName));
        mHead.read(mHandle);
    }
}
