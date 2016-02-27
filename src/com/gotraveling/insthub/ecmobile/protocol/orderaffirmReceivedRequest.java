
package com.gotraveling.insthub.ecmobile.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotraveling.external.activeandroid.Model;
import com.gotraveling.external.activeandroid.annotation.Column;
import com.gotraveling.external.activeandroid.annotation.Table;

@Table(name = "orderaffirmReceivedRequest")
public class orderaffirmReceivedRequest  extends Model
{

     @Column(name = "session")
     public SESSION   session;

     @Column(name = "order_id")
     public int order_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          this.order_id = jsonObject.optInt("order_id");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != session)
          {
            localItemObject.put("session", session.toJson());
          }
          localItemObject.put("order_id", order_id);
          return localItemObject;
     }

}
