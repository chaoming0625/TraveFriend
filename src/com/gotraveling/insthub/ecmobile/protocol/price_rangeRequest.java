
package com.gotraveling.insthub.ecmobile.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotraveling.external.activeandroid.Model;
import com.gotraveling.external.activeandroid.annotation.Column;
import com.gotraveling.external.activeandroid.annotation.Table;

@Table(name = "price_rangeRequest")
public class price_rangeRequest  extends Model
{

     @Column(name = "category_id")
     public int   category_id;

    @Column(name = "session")
    public SESSION   session;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.category_id = jsonObject.optInt("category_id");
         SESSION session = new SESSION();
         session.fromJson(jsonObject.optJSONObject("session"));
         this.session = session;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("category_id", category_id);
         if(null != session)
         {
             localItemObject.put("session", session.toJson());
         }
          return localItemObject;
     }

}
