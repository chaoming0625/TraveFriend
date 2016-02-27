package com.gotraveling.insthub.gps.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import android.location.Location;
import android.location.LocationProvider;

/**
 * Created by ChaoMing on 2015/4/16.
 */
public class GpsInfo {

    public static short SIZE = 784;

    public long utc_t =0;
    public long lastFixTime=0;
    public boolean pos_change=false;
    public boolean sat_change = false;
    /**
     *  GPS quality indicator (0 = Invalid; 1 = Fix; 2 = Differential(���), 3 = Sensitive��Ӧ)��λ����Ϣ
     */
    public byte     sig=0;
    /**< Latitude(γ��) in NDEG - +/-[degree][min].[sec/60] */
    public double  lat=0.0;
    /**< Longitude(����) in NDEG - +/-[degree][min].[sec/60] */
    public double  lon=0.0;
    /**< Antenna altitude(����) above/below mean sea level (geoid) in meters */
    public double  elv=0.0;

    /**
     *  Operating mode, used for navigation (1 = Fix not available; 2 = 2D; 3 = 3D),����״̬
     */
    public byte     fix=0;
    /**
     *  Position Dilution Of Precision���ۺ�λ�þ������ӣ�0.5 �\ 99.9��
     */
    public double  PDOP=0.0;
    /**
     * Horizontal Dilution Of Precision��ˮƽ�������ӣ�0.5 �\ 99.9��
     */
    public double  HDOP=0.0;
    /**
     *  Vertical Dilution Of Precision����ֱ�������ӣ�0.5 �\ 99.9��
     */
    public double  VDOP=0.0;
    /**
     *  �������ʣ�Speed over the ground in kilometers/hour
     */
    public double  speed=0.0;
    /**
     * ���溽��,Track angle in degrees True ,000.0~359.9
     * */
    public float  direction=0.0f;
    /**
     *  ��ƫ�Ƿ��� Magnetic variation degrees (Easterly var. subtracts from true course)
     */
    public float  declination=0.0f;
    /**
     *  Satellites information
     */
    protected SatelliteInfo satinfo = new SatelliteInfo();
    public GpsInfo(){
    }

    public void setSatis(SatelliteInfo l){
        switch(l.inuse){
            case 0:
            case 1:
            case 2:  fix = 1;break;
            case 3: fix = 2;break;
            default: fix = 3;
        }
        if(l.hasfix>0 ){
            if( sig == 1 ) sig = 2;
        }
        satinfo = l;
        if( good() && lastFixTime==0 ) lastFixTime = System.currentTimeMillis();
    }
    public void zero(){
        utc_t=0;
        sig=0;fix=0;PDOP=HDOP=VDOP=0.0;
        lat=lon=0.0; elv=speed=0.0; direction=declination=0.0f;
        lastFixTime =0;
        satinfo = new SatelliteInfo();
    }
    public boolean good(){ return (sig==1)||(sig==2) ; }
    public SatelliteInfo getSatinfo(){ return satinfo; }
    public void read(DataInputStream in,int len) throws IOException{
        byte[] data = new byte[len];
        len = in.read(data);
        if( len!= data.length) throw new EOFException();
        ByteReader reader = new ByteReader(data,0);
        reader.skip(40);
        int hsec = reader.readInt();
        reader.skip(4);
        utc_t = reader.readLong()*1000+hsec;
        sig = (byte)reader.readInt();
        fix = (byte) reader.readInt();
        PDOP = reader.readDouble();
        HDOP = reader.readDouble();
        VDOP = reader.readDouble();
        lat = reader.readDouble();
        lon = reader.readDouble();
        elv = reader.readDouble();
        speed = reader.readDouble();
        direction = (float)reader.readDouble();
        declination = (float)reader.readDouble();
        satinfo.read(reader);
    }

    public RadianPos getRadianPos()
    {
        RadianPos pos = new RadianPos();
        pos.lat = (lat);
        pos.lon = (lon);
        return pos;
    }
    public  void LocChange(Location l){
        if( l==null )          sig = 0;
        utc_t = l.getTime();
        this.sig = 1;
        if(satinfo.hasfix>0 ) sig = 2;

        this.direction = l.getBearing();
        this.elv = l.getAltitude();
        this.lat = l.getLatitude();
        this.lon = l.getLongitude();
        this.PDOP = l.getAccuracy();

        pos_change = true;
    }
    public void SATChange(int status){
        switch (status) {
            //GPS״̬Ϊ�ɼ�ʱ
            case LocationProvider.AVAILABLE:
                sig = 1;
                break;
            //GPS״̬Ϊ��������ʱ
            case LocationProvider.OUT_OF_SERVICE:
                sig = 0;
                break;
            //GPS״̬Ϊ��ͣ����ʱ
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                sig =6;
                break;
        }
        sat_change = true;
    }
    public PosRecord getRecord(){
        PosRecord x= new PosRecord();
        x.sig  = sig;
        x.fix = fix;
        x.altitude = (float)elv;
        x.latitude =GpsMath.degree2ndeg( lat);
        x.longitude = GpsMath.degree2ndeg(lon) ;
        x.SatTime = utc_t/1000;
        x.nsSatTime = (short)(utc_t%1000);
        x.inuse  = satinfo.inuse;
        x.inview = satinfo.inview;
        //x.hdop = (short)(HDOP*100);
        //x.vdop = (short)(VDOP*100);
        x.pdop = (short)(PDOP*100);
        x.direction = direction;
        return x;
    }

    public String GpsTime() {//GPSʱ��
        String text=   TimeUtils.getTime(utc_t);
        return text;
    }

    public String Altitude() {//����
        String text;
        if( good() ) {
            if (elv > 1000.0) text = String.format("%.0f", elv);
            else text = String.format("%.1f", elv);
        } else text="----.---";
        return text;
    }

    public String Latitude() {//γ��
        String text;
        if( good() )
        text = String.format("%4.5f",(lat));
        else text="-----.------";
        return text;
    }

    public String Longitude() {//����
        String text;
        if( good() )
        text = String.format("%5.5f",(lon));
        else text="------.------";
        return text;
    }

    public String Orientation() {//����
        String text;
        if( good() )
        text = String.format("%.2f",direction);
        else text="---.---";
        return text;
    }

    public int Fix() { //��λ����ģʽ
        return (fix);
    }



    public String Satellites() {
        String text;
        text = String.format("%02d/%02d",satinfo.inuse,satinfo.inview);
        return text;
    }

    public String DOP() {
        String text;
        if( good() )
        //text = String.format("HDOP:%.1f PDOP:%.1f VDOP:%.1f",HDOP,PDOP,VDOP);
        text = String.format("%.1f",PDOP);
        else text = "---.--";
        return text;
    }
}
