package com.gotraveling.insthub.gps.utils;

import java.io.IOException;

import android.location.GpsSatellite;

/**
 * Created by ChaoMing on 2015/4/17.
 */
public class SatelliteInfo {
	public class nmeaSATELLITE {
		public byte id = 0;
		/** < Satellite PRN number (���Ǳ��), 01 to 32 */
		public byte in_use = 0;
		/** < Used in position fix */
		public float elv = 0.0f;
		/** < Elevation in degrees(��������), 90 maximum */
		public float azimuth = 0.0f;
		/** < Azimuth, degrees from true north(���Ƿ�λ��), 000 to 359 */
		public float sig = 0.0f;
		/** < Signal, 00-99 dB */
		public boolean hasEph = false;
		/** �����Ƿ��������� */
		public boolean hasAlm = false;

		/** �����Ƿ����ڽ��ڵ�GPS�������� */
		public nmeaSATELLITE() {
		}

		public void zero() {
			id = in_use = 0;
			elv = sig = azimuth = 0.00f;
		}
	};

	public static final int MAXSAT = 32;
	// public static final short SIZE = 14* MAXSAT+2;
	public byte inuse = 0;
	/** < Number of satellites in use (not those in view) */
	public byte inview = 0;
	/** < Total number of satellites in view */
	public byte hasfix = 0;
	public nmeaSATELLITE[] sat = new nmeaSATELLITE[MAXSAT];;

	/** < Satellites information */
	public SatelliteInfo() {
		for (int i = 0; i < MAXSAT; i++)
			sat[i] = new nmeaSATELLITE();
	}

	/**
	 * Operating mode, used for navigation ,����״̬
	 * 
	 * @return (1 = Fix not available; 2 = 2D; 3 = 3D)
	 */
	public int getFix() {
		switch (inuse) {
		case 0:
		case 1:
		case 2:
			return 1;
		case 3:
			return 2;
		default:
			;
		}
		return 3;
	}

	public void reset() {
		inuse = 0;
		inview = 0;
		for (int i = 0; i < sat.length; i++)
			sat[i] = null;
		sat = null;
	}

	public void read(ByteReader in) throws IOException {
		inuse = (byte) in.readInt();
		inview = (byte) in.readInt();
		for (int i = 0; i < MAXSAT; i++) {
			sat[i].id = (byte) in.readInt();
			sat[i].in_use = (byte) in.readInt();
			sat[i].elv = in.readInt();
			sat[i].azimuth = in.readInt();
			sat[i].sig = in.readInt();
		}
	}

	public void write(ByteWriter out) throws IOException {

		for (int i = 0; i < MAXSAT; i++) {

		}
	}

	public void set(GpsSatellite a) {
		int indx = a.getPrn() - 1;
		if (indx < 0)
			return;
		if (indx < MAXSAT) {
			sat[indx].id = (byte) (indx + 1);
			sat[indx].azimuth = a.getAzimuth();
			sat[indx].sig = a.getSnr();
			sat[indx].elv = a.getElevation();
			sat[indx].hasEph = a.hasEphemeris();
			sat[indx].hasAlm = a.hasAlmanac();
			inview++;
			if (a.usedInFix()) {
				sat[indx].in_use = 1;
				if (a.hasAlmanac())
					hasfix++;
				inuse++;
			} else
				sat[indx].in_use = 0;
		}
	}
}
