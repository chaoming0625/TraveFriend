/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 *
 * @author ChaoMing
 */
public class LineReader {
    protected ArrayList mData;
    protected int mLast;
    public LineReader(){
        mData = new ArrayList();
        mLast= 0;
    }
    
    public void open(String name) throws IOException{
        open(new FileReader(name));
    }
    public void open(File in) throws IOException{
        open( new FileReader(in));
    }
    public void open(Reader in) throws IOException{
        BufferedReader reader = new BufferedReader(in);
        String line; 
        close();
        while ((line = reader.readLine()) != null) mData.add(line);
    }
    public void close(){
        mData.clear();
        mLast = 0;
    }
    public boolean eof(){
        return mLast>=mData.size();
    }
    public int size(){ return mData.size(); }
    public int pos(){ return mLast; }
    public void setPos(int pos){
        if(pos<0) pos =0;
        mLast = pos;
    }
    public String readLine() {
        if( mLast> mData.size() ) return null;
        String tmp= (String)mData.get(mLast);
        mLast ++;
        return tmp;
    }
    
}
