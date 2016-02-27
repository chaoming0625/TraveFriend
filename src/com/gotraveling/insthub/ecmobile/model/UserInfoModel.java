package com.gotraveling.insthub.ecmobile.model;
//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

import com.gotraveling.external.androidquery.callback.AjaxStatus;
import com.gotraveling.insthub.BeeFramework.model.BaseModel;
import com.gotraveling.insthub.BeeFramework.model.BeeCallback;
import com.gotraveling.insthub.BeeFramework.view.MyProgressDialog;
import com.gotraveling.insthub.ecmobile.fragment.E0_ProfileFragment;
import com.gotraveling.insthub.ecmobile.protocol.*;
import com.insthub.ecmobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

public class UserInfoModel extends BaseModel {
    public USER user;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public static final int RANK_LEVEL_NORMAL = 0;
    public static final int RANK_LEVEL_VIP = 1;


    public UserInfoModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public void getUserInfo() {

        userinfoRequest request = new userinfoRequest();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                UserInfoModel.this.callback(url, jo, status);

                try {

                    userinfoResponse response = new userinfoResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                            user = response.data;
                            user.save();
                            editor.putString("email",user.email);
                            editor.commit();
                            UserInfoModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };

        request.session = SESSION.getInstance();

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.USER_INFO).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

}
