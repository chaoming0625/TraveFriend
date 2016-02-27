package com.gotraveling.insthub.gps.utils;

import java.io.EOFException;



public class ByteReader {
	protected int last;
	protected byte[] mData;
	public ByteReader(byte[] array,int begin){
		last = begin;
		mData = array;
	}
        public final float readFloat() throws EOFException {   
          return Float.intBitsToFloat(readInt());   
        }   

        public final double readDouble() throws EOFException {   
          return Double.longBitsToDouble(readLong());   
        }           
	public final long readLong() throws EOFException {
    	if( mData.length<(last+8)) throw new  EOFException();
    	int i= last;
    	last +=8;
        return (((long)mData[i+7] << 56) +
                ((long)(mData[i+6] & 255) << 48) +
                ((long)(mData[i+5] & 255) << 40) +
                ((long)(mData[i+4] & 255) << 32) +
                ((long)(mData[i+3] & 255) << 24) +
                ((mData[i+2] & 255) << 16) +
                ((mData[i+1] & 255) <<  8) +
                ((mData[i] & 255) <<  0));	
	}
	public final int readInt() throws EOFException {
    	if( mData.length<(last+4)) throw new EOFException();
    	int i= last;
    	last +=4;
        int ch1 = mData[i+3]& 255;
        int ch2 = mData[i+2]& 255;
        int ch3 = mData[i+1]& 255;
        int ch4 = mData[i]& 255;
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));		
	}
        public final  short readShort() throws EOFException{
            if( mData.length<(last+2)) throw new EOFException();
            int ch1 = mData[last+1]&255;
            int ch2 = mData[last]&255;
            last +=2;
            return (short)( (ch1 << 8) + (ch2 << 0));  	
        }
        public final byte readByte() throws EOFException{
            if( mData.length<(last+1)) throw new EOFException();
            int i= last;
            last ++;
            return mData[i];
        }
	public final  String readString(int length) throws EOFException{
    	if( mData.length<(last+length))  throw new EOFException();
    	int i= last;
    	last +=length;
    	byte[] a = new byte[length];
    	System.arraycopy(mData,i,a,0,length);  	
    	for( i=0;i<length;i++) if( a[i]==0 )break;
    	
    	return new String(a,0,i);    	
	}
	public final  String readStringU(int length) throws EOFException{
    	if( mData.length<(last+length))  throw new EOFException();
    	int i= last;
    	last +=length;
    	byte[] a = new byte[length];
    	System.arraycopy(mData,i,a,0,length);  	
    	return new String(a,0,i);    	
	}	
    public final  byte[] readArray(int length) throws EOFException{
    	if( mData.length<(last+length))  throw new EOFException();
    	int i= last;
    	last +=length;
    	byte[] a = new byte[length];
    	System.arraycopy(mData,i,a,0,length);
    	return a;
    }    
    public void skip(int length) throws EOFException {
    	if( mData.length<(last+length))  throw new EOFException();
    	last +=length;    	
    }
    public final  byte readByte(int length) throws EOFException {
    	if( mData.length<(last+1)) throw new EOFException();
    	int ch = mData[last++]& 255;
    	return (byte)(ch);
        }
    public final static long readLong(byte[] array,int begin) throws EOFException {
    	if( array.length<(begin+8)) throw new EOFException();
        return (((long)array[begin+7] << 56) +
                ((long)(array[begin+6] & 255) << 48) +
                ((long)(array[begin+5] & 255) << 40) +
                ((long)(array[begin+4] & 255) << 32) +
                ((long)(array[begin+3] & 255) << 24) +
                ((array[begin+2] & 255) << 16) +
                ((array[begin+1] & 255) <<  8) +
                ((array[begin] & 255) <<  0));
    }
    public final static int readInt(byte[] array,int begin) throws EOFException {
    	if( array.length<(begin+4)) throw new EOFException();
        int ch1 = (array[begin+3]&0x000000FF);
        int ch2 = array[begin+2]&0x000000FF;
        int ch3 = array[begin+1]&0x000000FF;
        int ch4 = array[begin]&0x000000FF;
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    public final static int readShort(byte[] array,int begin) throws EOFException{
    	if( array.length<(begin+2)) throw new EOFException();
    	int ch1 = array[begin+1]&0x000000FF;
        int ch2 = array[begin]&0x000000FF;
        return ( (ch1 << 8) + (ch2 << 0));  	
    }
    public final static String readString(byte[] array,int begin,int length) throws EOFException{
    	if( array.length<(begin+length))  throw new EOFException();
    	byte[] a = new byte[length];
    	System.arraycopy(array,begin,a,0,length);  	
    	int i;
    	for( i=0;i<length;i++) if( a[i]==0 )break;
    	
    	return new String(a,0,i);
    }
    public final static String readStringU(byte[] array,int begin,int length) throws EOFException{
    	if( array.length<(begin+length))  throw new EOFException();
    	byte[] a = new byte[length];
    	System.arraycopy(array,begin,a,0,length);  	
    	return new String(a);
    }
    
    public final static byte[] readArray(byte[] array,int begin,int length) throws EOFException{
    	if( array.length<(begin+length))  throw new EOFException();
    	byte[] a = new byte[length];
    	System.arraycopy(array,begin,a,0,length);
    	return a;
    }    
    public final static byte readByte(byte[] array,int begin) throws EOFException {
    	if( array.length<(begin+1) )  throw new EOFException();
    	int ch = array[begin]&0x000000FF;
    	return (byte)(ch);
        }
}
