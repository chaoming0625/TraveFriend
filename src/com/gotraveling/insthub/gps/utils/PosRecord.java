/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 *
 * @author ChaoMing
 */
public class PosRecord {
	public double longitude;
	/** < Longitude(经度) in NDEG - +/-[degree][min].[sec/60] */
	public double latitude;
	/** < Latitude(纬度) in NDEG - +/-[degree][min].[sec/60] */
	public float altitude;
	/** < Antenna altitude above/below mean sea level (geoid) in meters */
	public long SatTime;
	/** < UTC of position */
	public short nsSatTime;
	public byte sig;
	/**
	 * < GPS quality indicator (0 = Invalid; 1 = Fix; 2 = Differential, 3 =
	 * Sensitive)
	 */
	public byte fix;
	/**
	 * < Operating mode, used for navigation (1 = Fix not available; 2 = 2D; 3 =
	 * 3D)
	 */
	public byte inuse;
	public byte inview;
	public short pdop;
	/** < Position Dilution Of Precision */
	public short hdop = 0;
	/** < Horizontal Dilution Of Precision */
	public short vdop = 0;
	/** < Vertical Dilution Of Precision */
	public float direction = 0.00f;
	public float speed = 0.000f;

	public PosRecord() {

	}

	public static final short SIZE = (8 + 8 + 4 + 8 + 2 + 4 + 2 + 2 + 2 + 4);

	public boolean good() {
		return (sig == 1) || (sig == 2);
	}

	public void read(DataInputStream in, int len) throws IOException {
		byte[] data = new byte[len];
		len = in.read(data);
		if (len != data.length)
			throw new EOFException();
		ByteReader reader = new ByteReader(data, 0);
		longitude = reader.readDouble();
		latitude = reader.readDouble();
		altitude = reader.readFloat();
		SatTime = reader.readLong();
		nsSatTime = reader.readShort();
		sig = reader.readByte();
		fix = reader.readByte();
		inuse = reader.readByte();
		inview = reader.readByte();
		pdop = reader.readShort();
		hdop = reader.readShort();
		vdop = reader.readShort();
		try {
			speed = reader.readFloat();
		} catch (EOFException e) {
			speed = 0.000f;
		}
		// direction = in.readFloat();
		// direction = in.readFloat();
	}

	public void write(DataOutputStream out) throws IOException {
		/*
		 * out.writeDouble(longitude); out.writeDouble(latitude);
		 * out.writeFloat(altitude); out.writeLong(SatTime);
		 * out.writeShort(nsSatTime); out.writeByte(sig); out.writeByte(fix);
		 * out.writeByte(inuse); out.writeByte(inview); out.writeShort(pdop);
		 * out.writeShort(hdop); out.writeShort(vdop);
		 * //out.writeFloat(direction);
		 */
		byte[] data = new byte[SIZE];
		ByteWriter.writeDouble(data, 0, longitude);
		ByteWriter.writeDouble(data, 8, latitude);
		ByteWriter.writeFloat(data, 16, altitude);
		ByteWriter.writeLong(data, 20, SatTime);
		ByteWriter.writeShort(data, 28, nsSatTime);
		data[30] = sig;
		data[31] = fix;
		ByteWriter.writeByte(data, 32, inuse);
		ByteWriter.writeByte(data, 33, inview);
		ByteWriter.writeShort(data, 34, pdop);
		ByteWriter.writeShort(data, 36, hdop);
		ByteWriter.writeShort(data, 38, vdop);
		ByteWriter.writeFloat(data, 40, speed);
		out.write(data);
	}
}
