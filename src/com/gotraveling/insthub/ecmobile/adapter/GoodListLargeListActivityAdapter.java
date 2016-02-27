package com.gotraveling.insthub.ecmobile.adapter;
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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gotraveling.insthub.BeeFramework.adapter.BeeBaseAdapter;
import com.gotraveling.insthub.ecmobile.component.GoodItemLargeCell;
import com.gotraveling.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.insthub.ecmobile.R;

import java.util.ArrayList;

public class GoodListLargeListActivityAdapter extends BeeBaseAdapter {

    public GoodListLargeListActivityAdapter(Context c, ArrayList dataList) {
        super(c, dataList);
         
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
         
        return dataList.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
         
        return null;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
         
        return 0;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent,
                            BeeCellHolder h) {
        SIMPLEGOODS simplegoods = (SIMPLEGOODS)dataList.get(position);
        ((GoodItemLargeCell)cellView).bindData(simplegoods);
        return null;
    }

    @Override
    public View createCellView() {
         
        return mInflater.inflate(R.layout.b2_product_detail_cell, null);
    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView) {
         
        return null;
    }
}
