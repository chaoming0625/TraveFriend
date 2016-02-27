/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

import android.content.Context;

/**
 *
 * @author ChaoMing
 */
public class Course {
    protected long begin=0;
    /**
     * < distance between last validate position
     */
    protected RadianPos latest_pos = null;
    protected double curSpeed;
    protected boolean good=false;
    protected int curTicks=0;
    protected int lastTicks=0;
    protected double curCourse=0.00;
    protected double lastCourse=0.00;
    protected long last=System.currentTimeMillis();

    public boolean fixSpeed(){
        long k = System.currentTimeMillis();
        if( ( k-last )>1000 ) {
            last = k;
            if( curSpeed!=0.0000000) {
                curSpeed=0.0000000;
                return true;
            }
        }
        return false;
    }
    public  void CalcDistance( GpsInfo nmea ) {
        double distance;
        good=false;
        last=System.currentTimeMillis();
        if( begin ==0 ){
            if( nmea.good()  ){
                latest_pos = nmea.getRadianPos();
                begin = nmea.utc_t;
            }
        } else {
            long t = (nmea.utc_t - begin)/1000;
            if( t>0 ){
                if( nmea.good()  ){
                    //������Чλ�ý��м���
                    RadianPos cur=  nmea.getRadianPos();
                    distance = latest_pos.distance(cur);
                    begin = nmea.utc_t;
                    latest_pos = cur;
                    curTicks += t;
                    if( distance<0.1 ) curSpeed=0.0;
                     else    curSpeed = distance * 3.6 / t;
                    curCourse += distance;
                    good=true;
                } else {
                    if( t>1 ) {
                        begin = 0;
                        curTicks += t;
                        curSpeed = 0.00;
                    }
                }
            } else {
                curSpeed = 0.00;
            }
        }
    }


    public Course(){

    }
    public float getSpeed(){
        if(good) return (float)curSpeed;
        return 0.000f;
    }
    //ƽ���ٶ�
    public String Speed() {
        String text;
        if( curTicks!=0 ) text=String.format("%03.1f", (curCourse / curTicks) * (3.6000) * NaviPrefs.SpeedMultiplier[NaviPrefs.mUnitSpeed]);
        else text="0.0";
        return text;
    }

    //˲���ٶ�
    public String CurSpeed() {
        String text;
        if( good) {
            if (curSpeed > 100.00f)
                text = String.format("%03.0f", curSpeed * NaviPrefs.SpeedMultiplier[NaviPrefs.mUnitSpeed]);
            else text = String.format("%02.1f", curSpeed * NaviPrefs.SpeedMultiplier[NaviPrefs.mUnitSpeed]);
        } else text="------";
        return text;
    }

    public String TotalCourse() {//�����
        String text;
        text=String.format("%.02f", (lastCourse + curCourse) / 1000.0 * NaviPrefs.DistanceMultiplier[NaviPrefs.mUnitDistance]);
        return text;
    }

    public String CurCourse() {//�������
        String text;
        text = String.format("%.02f",curCourse/1000.0*NaviPrefs.DistanceMultiplier[NaviPrefs.mUnitDistance]);
        return text;
    }

    public String TimeSnap() {//����ʱ��
        String text;
        text = String.format("%d:%d:%d",curTicks/3600,(curTicks%3600)/60,curTicks%60);
        return text;
    }


    public void Open() {
        lastCourse = NaviPrefs.mTotalCourse;
        lastTicks = NaviPrefs.mTotalTicks;
        ZeroCourse();
    }

    public void Close(Context ct) {
        begin = 0;
        NaviPrefs.mTotalCourse =lastCourse+curCourse;
        NaviPrefs.mTotalTicks = lastTicks+curTicks;
        NaviPrefs.SaveData(ct);
    }

    //����������
    public void ResetCourse() {
        begin = 0;
        curCourse =0;
        curSpeed = 0.0;
        curTicks =0;
        lastCourse = NaviPrefs.mTotalCourse=0;
        lastTicks = NaviPrefs.mTotalTicks=0;
    }
    //������̸�λ
    public void ZeroCourse() {
        begin = 0;
        NaviPrefs.mTotalCourse =lastCourse+curCourse;
        NaviPrefs.mTotalTicks = lastTicks+curTicks;
        lastCourse = NaviPrefs.mTotalCourse;
        lastTicks = NaviPrefs.mTotalTicks;
        curSpeed = 0.0;
        curCourse =0;
        curTicks =0;
    }
}
