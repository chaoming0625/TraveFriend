/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

/**
 * @author ChaoMing
 */
public class ByteWriter {
    public static void writeByte(byte[] dst ,int offset,byte data) {
        dst[offset] = data;
    }
    public static byte[] getBytes(short data){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }  
   public static void writeShort(byte[] dst ,int offset,short data) {
        dst[offset] = (byte) (data & 0xff);
        dst[offset+1] = (byte) ((data & 0xff00) >> 8);
    }
   
    public static byte[] getBytes(int data)  {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    } 
    public static void writeInt(byte[] dst ,int offset,int data)  {
        dst[offset] = (byte) (data & 0xff);
        dst[offset+1] = (byte) ((data & 0xff00) >> 8);
        dst[offset+2] = (byte) ((data & 0xff0000) >> 16);
        dst[offset+3] = (byte) ((data & 0xff000000) >> 24);
    }       
    public static byte[] getBytes(long data)  {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }    
    public static void writeLong(byte[] dst ,int offset,long data)  {
        dst[offset] = (byte) (data & 0xff);
        dst[offset+1] = (byte) ((data >> 8) & 0xff);
        dst[offset+2] = (byte) ((data >> 16) & 0xff);
        dst[offset+3] = (byte) ((data >> 24) & 0xff);
        dst[offset+4] = (byte) ((data >> 32) & 0xff);
        dst[offset+5] = (byte) ((data >> 40) & 0xff);
        dst[offset+6] = (byte) ((data >> 48) & 0xff);
        dst[offset+7] = (byte) ((data >> 56) & 0xff);
    }    
    public static byte[] getBytes(float data)  {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }
    public static void writeFloat(byte[] dst ,int offset,float data){
        int intBits = Float.floatToIntBits(data);
        writeInt(dst,offset,intBits);
    }
    public static byte[] getBytes(double data)  {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }   
    public static void writeDouble(byte[] dst ,int offset,double data){
        long intBits = Double.doubleToLongBits(data);
        writeLong(dst,offset,intBits);
    }    
}
