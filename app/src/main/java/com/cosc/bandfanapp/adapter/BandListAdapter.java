package com.cosc.bandfanapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.network.model.band.Band;

import java.util.List;

/**
 * @author William Moffitt
 * @version 1.0 11/18/15
 */
public class BandListAdapter extends ArrayAdapter<Band> {

    private List<Band> mBands;

    public BandListAdapter(Context context, List<Band> bands) {
        super(context, 0);

        mBands = bands;
    }

    @Override
    public int getCount() {
        return mBands.size();
    }

    @Override
    public Band getItem(int position) {
        return mBands.get(position);
    }

    @Override
    public int getPosition(Band item) {
        return mBands.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.band_list_item, parent
                    , false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.dateStarted = (TextView) convertView.findViewById(R.id.date_start);

            convertView.setTag(viewHolder);
        }

        Band band = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(band.name);
        viewHolder.dateStarted.setText(band.date_start);

        return convertView;
    }

    private static class ViewHolder {

        public TextView name;
        public TextView dateStarted;

    }

}
