/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author ChaoMing
 */
class PosHead {
    public static final int TRACK_MAX=60;
    public static final short SIZE = TRACK_MAX * 4 +16;
    public short magic=0x4744;        //标志字节
    public long recordtime; //记录�?始时�?,2
    public byte type=0;         //文件类型�?0,轨迹文件�?1，关键点文件,10
    public short packsize=0;     //位置包大�?,11
    //public byte unused[2];    //未用 , 13
    public byte tracks=0;       //轨迹�?,15
    public int index[];     
    public PosHead(){
        this.index = new int[TRACK_MAX];
    }

    public void read(DataInputStream in) throws IOException{
        byte[] data = new byte[SIZE];
        in.read(data);
        ByteReader reader = new ByteReader(data,0);
        magic = reader.readShort();
        recordtime = reader.readLong()*1000;
        type = reader.readByte();
        packsize = reader.readShort();
        reader.readShort();
        tracks = reader.readByte();
        for(int i=0;i<index.length;i++) index[i]=reader.readInt();
    }
    public void write(DataOutputStream out) throws IOException{
        out.writeShort(Short.reverseBytes(magic));
        out.writeLong(Long.reverseBytes(recordtime/1000));
        out.writeByte(type);
        out.writeShort(Short.reverseBytes(packsize));
        out.writeShort(0);
        out.writeByte(tracks);
        for(int i=0;i<index.length;i++)out.writeInt(Integer.reverseBytes(index[i]));
    }
            
}
