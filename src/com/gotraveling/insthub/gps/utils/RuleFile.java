/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ChaoMing
 */
public class RuleFile {
	public LineReader mFile;
	private FileWriter mDimFile;
	private String mData;

	protected void OutputTrack(PosReader input) throws IOException {
		int trbegin = mFile.pos();
		for (int i = 0; i < input.TrackNum(); i++) {
			mFile.setPos(trbegin);
			if (input.NextTrack() == 0)
				break;
			while (!mFile.eof()) {
				mData = mFile.readLine();
				if ((mData == null) || (mData.length() < 1))
					continue;
				if (mData.charAt(0) == '%') {
					if (mData.length() < 2)
						continue;
					if (mData.charAt(1) == '%')
						break; // block end command
					trimAll();
					if (mData.length() > 1) {
						if (mData.compareToIgnoreCase("%position") == 0)
							OutputPos(input);
						if (mData.compareToIgnoreCase("%local") == 0)
							input.SetTimeLocal(true);
						if (mData.compareToIgnoreCase("%gmt") == 0)
							input.SetTimeLocal(false);
					}
				} else
					OutputVar(input);
			}
		}
	}

	protected void OutputPos(PosReader input) throws IOException {
		int posbegin = mFile.pos();
		while (input.NextPos() != 0) {
			mFile.setPos(posbegin);
			while (!mFile.eof()) {
				mData = mFile.readLine();
				if ((mData == null) || (mData.length() < 1))
					continue;
				if (mData.charAt(0) == '%') {
					if (mData.length() < 2)
						continue;
					if (mData.charAt(1) == '%')
						break; // block end command
				} else
					OutputVar(input);
			}
		}
	}

	protected void OutputVar(PosReader input) throws IOException {
		int len = mData.length();
		String name, fmt;
		String data;
		int k;
		for (int i = 0; i < len; i++) {
			if ((mData.charAt(i) == '$') && (mData.charAt(i + 1) == '{')) {
				i += 2;
				if (i < len) {
					k = i;
					while (k < len)
						if (mData.charAt(k) == '}')
							break;
						else
							k++;
					name = mData.substring(i, k);
					i = k;
					name = name.trim();
					k = name.indexOf(':');
					if (k > 0) {
						fmt = name.substring(k);
						name = name.substring(0, k);
					} else
						fmt = "";
					data = input.Value(fmt, name);
					if ((data != null) && data.length() > 0)
						mDimFile.write(data);
				}
			} else
				mDimFile.write(mData.charAt(i));
		}
		mDimFile.write("\n");
	}

	public RuleFile() {
		mFile = null;
		mDimFile = null;
	}

	public void Open(String name) throws GpsException {
		mFile = new LineReader();
		try {
			mFile.open(name);
		} catch (IOException e) {
			mFile.close();
			throw new GpsException("file [" + name + "] not exist");
		}
	}

	protected void trimAll() {
		int k = mData.indexOf("//");
		if (k >= 0)
			mData = mData.substring(0, k);
		if (mData.length() < 1)
			return;
		mData = mData.trim();
	}

	public void Convert(PosReader input, String name) throws IOException {
		mDimFile = new FileWriter(name);
		try {
			while (!mFile.eof()) {
				mData = mFile.readLine();
				if ((mData == null) || (mData.length() < 1))
					continue;
				if (mData.charAt(0) == '%') {
					if (mData.length() < 2)
						continue;
					if (mData.charAt(1) == '%')
						continue;
					// command line;
					trimAll();
					if (mData.compareToIgnoreCase("%local") == 0)
						input.SetTimeLocal(true);
					if (mData.compareToIgnoreCase("%gmt") == 0)
						input.SetTimeLocal(false);
					if (mData.compareToIgnoreCase("%track") == 0)
						OutputTrack(input);
					if (mData.compareToIgnoreCase("%position") == 0)
						OutputPos(input);
					if (mData.startsWith("%timefmt")) {
						if (Character.isWhitespace(mData.charAt(8))) {
							int k = mData.indexOf('{');
							if (k > 0) {
								int j = mData.indexOf('}');
								if (j > 0) {
									String a = mData.substring(k, j);
									input.SetTimeFmt(a);
								}
							}
						}
						continue;
					}

				} else
					OutputVar(input);
			}
		} finally {
			mDimFile.close();
			mDimFile = null;
		}
	}

	public void Close() {
		if (mFile != null)
			mFile.close();
		mFile = null;
	}
}
