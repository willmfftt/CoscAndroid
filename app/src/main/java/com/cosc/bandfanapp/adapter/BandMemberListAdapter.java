package com.cosc.bandfanapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.model.BandMember;

import java.util.List;

/**
 * @author William Moffitt
 * @version 1.0 11/19/15
 */
public class BandMemberListAdapter extends ArrayAdapter<BandMember> {

    private List<BandMember> mBandMembers;

    public BandMemberListAdapter(Context context, List<BandMember> bandMembers) {
        super(context, 0);

        mBandMembers = bandMembers;
    }

    @Override
    public int getCount() {
        return mBandMembers.size();
    }

    @Override
    public BandMember getItem(int position) {
        return mBandMembers.get(position);
    }

    @Override
    public int getPosition(BandMember item) {
        return mBandMembers.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.band_member_list_item
                    , parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.full_name);
            viewHolder.dateStarted = (TextView) convertView.findViewById(R.id.date_start);

            convertView.setTag(viewHolder);
        }

        BandMember bandMember = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        String fullNameStr = bandMember.first_name + " " + bandMember.last_name;
        viewHolder.fullName.setText(fullNameStr);
        viewHolder.dateStarted.setText(bandMember.date_start);

        return convertView;
    }

    private static class ViewHolder {

        public TextView fullName;
        public TextView dateStarted;

    }

}
