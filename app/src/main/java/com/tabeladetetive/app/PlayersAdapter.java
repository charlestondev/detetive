package com.tabeladetetive.app;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tabeladetetive.R;

import java.util.List;

import bluetooth.ClientBluetooth;

/**
 * Created by charleston on 22/06/14.
 */
public class PlayersAdapter extends BaseAdapter {
    private Activity mContext;
    private List<BluetoothDevice> playersList;
    ClientBluetooth clientBluetooth;
    public PlayersAdapter(Activity context, List<BluetoothDevice> list) {
        mContext = context;
        playersList = list;
        //mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return playersList.size();
    }
    @Override
    public Object getItem(int pos) {
        return playersList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final PlayerViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_player, null);
            viewHolder = new PlayerViewHolder(v);
            v.setTag(viewHolder);

        } else {
            viewHolder = (PlayerViewHolder) v.getTag();
            v.setOnLongClickListener(null);
        }

        viewHolder.mTVName.setText(playersList.get(position).getName());
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clientBluetooth = new ClientBluetooth(playersList.get(position));
                clientBluetooth.connectToServer();
                String type = ((PlayersActivity)mContext).type;
                int p = ((PlayersActivity)mContext).position;
                clientBluetooth.sendBytes((type+"-"+p).getBytes());
                //MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.snow_bound);
                //mediaPlayer.start();
                Toast.makeText(mContext,"Dados enviados!",Toast.LENGTH_LONG).show();
                mContext.finish();
                return true;
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return v;
    }
}
class PlayerViewHolder {
    public TextView mTVName;
    public TextView mTVMac;
    public PlayerViewHolder(View base) {
        mTVName = (TextView) base.findViewById(R.id.name);
        mTVMac = (TextView) base.findViewById(R.id.mac);
    }
}