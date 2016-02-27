/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

/**
 *
 * @author ChaoMing
 */
public class GpsException  extends Exception {

    public GpsException() {
        super();
    }
 
    public GpsException(String message) {
        super(message);
    }
 
    public GpsException(Throwable cause) {
        super(cause);
    }
 
    public GpsException(String message, Throwable cause) {
        super(message, cause);
    }
}
