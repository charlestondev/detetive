package com.tabeladetetive.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tabeladetetive.R;

import java.util.List;

/**
 * Created by charleston on 25/05/14.
 */
public class CrimeAdapter extends BaseAdapter {
    private Activity mContext;
    private List<Crime> crimeList;
    private LayoutInflater mLayoutInflater;
    public CrimeAdapter(Activity context, List<Crime> list) {
        mContext = context;
        crimeList = list;
        Log.i("context", mContext.toString());
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return crimeList.size();
    }
    @Override
    public Object getItem(int pos) {
        return crimeList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final CrimeViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_crime, null);
            viewHolder = new CrimeViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CrimeViewHolder) v.getTag();
            viewHolder.checked.setOnCheckedChangeListener(null);
            viewHolder.estados_suspeitos.setOnItemSelectedListener(null);
            v.setOnLongClickListener(null);
        }
        final int position_temp = position;
        //if i call setChecked after set a checkListener, the checkListener will be triggered
        final View temp_view = v;
        //viewHolder.checked.setChecked(crimeList.get(position).checked);
        viewHolder.mTVItem.setText(crimeList.get(position).name);
        viewHolder.color.setBackgroundColor(crimeList.get(position).color);
        if(crimeList.get(position).checked==0){
            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.suspect));
            viewHolder.estados_suspeitos.setSelection(0);
        }
        else if(crimeList.get(position).checked==1) {
            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.not_suspect));
            viewHolder.estados_suspeitos.setSelection(1);
        }
        else if(crimeList.get(position).checked==2){
            viewHolder.circle.setBackgroundDrawable(null);
            viewHolder.estados_suspeitos.setSelection(2);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        Intent intent = new Intent(mContext, PlayersActivity.class);
                        String type = crimeList.get(position_temp).type;
                        int listPosition = 0;
                        if(type.equals(Crime.SUSPECT))
                            listPosition = ((PagerViewFragment)mContext).suspectCrimeList.indexOf(crimeList.get(position_temp));
                        else if(type.equals(Crime.PLACE))
                            listPosition = ((PagerViewFragment)mContext).placeCrimeList.indexOf(crimeList.get(position_temp));
                        else if(type.equals(Crime.GUN))
                            listPosition = ((PagerViewFragment)mContext).gunCrimeList.indexOf(crimeList.get(position_temp));
                        intent.putExtra("type", type);
                        intent.putExtra("position", listPosition);
                        mContext.startActivity(intent);
                    }
                    else
                        Toast.makeText(mContext,"Ative o bluetooth!", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else{
            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.maybe_not_suspect));
            viewHolder.estados_suspeitos.setSelection(3);
        }
        /*viewHolder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && !((PagerViewFragment)mContext).crimes.get(crimeList.get(position_temp).type).equals("")) {
                    compoundButton.setChecked(false);
                }
                else {
                    crimeList.get(position_temp).setChecked(b?1:0);
                }
                if(!b)
                    ((PagerViewFragment)mContext).found = false;
                ((PagerViewFragment)mContext).checkCrimes();

            }
        });*/

        final AdapterView.OnItemSelectedListener listener =   new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                int current_state = crimeList.get(position_temp).checked;
                if(current_state!=pos){
                    boolean crimeFounded= !((PagerViewFragment)mContext).crimes.get(crimeList.get(position_temp).type).equals("");
                    if (pos == 0 || pos== 3) {
                        if(pos == 0) {
                            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.suspect));
                        }else
                        {
                            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.maybe_not_suspect));
                        }
                        crimeList.get(position_temp).setChecked(pos);
                        ((PagerViewFragment)mContext).found = false;
                        ((PagerViewFragment)mContext).crimes.put(crimeList.get(position_temp).type, "");

                    }
                    else if(pos == 1){
                        if(current_state == 2 || !crimeFounded){
                            viewHolder.circle.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.not_suspect));
                            crimeList.get(position_temp).setChecked(pos);
                        }
                        else
                            adapterView.setSelection(current_state);
                    }
                    else if(pos == 2){
                        if(current_state == 1 || !crimeFounded) {
                            viewHolder.circle.setBackgroundDrawable(null);
                            crimeList.get(position_temp).setChecked(pos);
                            ((PagerViewFragment)mContext).myCardsCrimeList.add(crimeList.get(position_temp));
                            ((PagerViewFragment)mContext).mPagerAdapter.notifyDataSetChanged();
                            temp_view.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {

                                    String type = crimeList.get(position_temp).type;
                                    int listPosition = 0;
                                    if(type.equals(Crime.SUSPECT))
                                        listPosition = ((PagerViewFragment)mContext).suspectCrimeList.indexOf(crimeList.get(position_temp));
                                    else if(type.equals(Crime.PLACE))
                                        listPosition = ((PagerViewFragment)mContext).placeCrimeList.indexOf(crimeList.get(position_temp));
                                    else if(type.equals(Crime.GUN))
                                        listPosition = ((PagerViewFragment)mContext).gunCrimeList.indexOf(crimeList.get(position_temp));

                                    Intent intent = new Intent(mContext, PlayersActivity.class);
                                    intent.putExtra("type", type);
                                    intent.putExtra("position", listPosition);

                                    mContext.startActivity(intent);
                                    return true;
                                }
                            });
                        }
                        else
                            adapterView.setSelection(current_state);
                    }
                    if(current_state==2){
                        ((PagerViewFragment)mContext).myCardsCrimeList.remove(crimeList.get(position_temp));
                        ((PagerViewFragment)mContext).mPagerAdapter.notifyDataSetChanged();
                    }
                    Log.i("change: ", pos+" "+current_state);
                    ((PagerViewFragment)mContext).checkCrimes();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        viewHolder.estados_suspeitos.setOnItemSelectedListener(listener);

        return v;
    }
}
class CrimeViewHolder {
    public TextView mTVItem;
    public CheckBox checked;
    public TextView color;
    public TextView circle;
    public Spinner estados_suspeitos;
    public CrimeViewHolder(View base) {
        mTVItem = (TextView) base.findViewById(R.id.name);
        checked = (CheckBox) base.findViewById(R.id.checked);
        color = (TextView) base.findViewById(R.id.color);
        circle = (TextView) base.findViewById(R.id.circle);
        estados_suspeitos = (Spinner) base.findViewById(R.id.estados);
    }
}
