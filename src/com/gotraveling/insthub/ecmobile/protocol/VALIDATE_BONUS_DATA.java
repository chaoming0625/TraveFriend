
package com.gotraveling.insthub.ecmobile.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotraveling.external.activeandroid.Model;
import com.gotraveling.external.activeandroid.annotation.Column;
import com.gotraveling.external.activeandroid.annotation.Table;

@Table(name = "VALIDATE_BONUS_DATA")
public class VALIDATE_BONUS_DATA  extends Model
{

     @Column(name = "bouns")
     public String   bouns;

     @Column(name = "bonus_formated")
     public String   bonus_formated;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.bouns = jsonObject.optString("bouns");

          this.bonus_formated = jsonObject.optString("bonus_formated");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("bouns", bouns);
          localItemObject.put("bonus_formated", bonus_formated);
          return localItemObject;
     }

}
