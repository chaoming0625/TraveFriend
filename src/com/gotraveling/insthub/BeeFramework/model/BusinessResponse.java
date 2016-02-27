package com.gotraveling.insthub.BeeFramework.model;

import com.gotraveling.external.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public interface BusinessResponse
{    
	public abstract void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException;
}
