package com.tabeladetetive.app;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.tabeladetetive.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import bluetooth.ServerBluetooth;

public class PagerViewFragment extends ActionBarActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    List<Crime> suspectCrimeList;
    List<Crime> gunCrimeList;
    List<Crime> placeCrimeList;
    List<Crime> myCardsCrimeList = new ArrayList<Crime>();
    public Map<String, String> crimes = new HashMap<String, String>(3);
    public boolean found = false;
    public Menu menu;
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_DURATION_BT = 2;
    ServerBluetooth serverBluetooth;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_pager);

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Aviso")
                .setMessage("A funcionalidade bluetooth ainda está em desenvolvimento, portanto pode não funcionar como esperado!")
                .show();


        crimes.put(Crime.SUSPECT, "");
        crimes.put(Crime.GUN, "");
        crimes.put(Crime.PLACE, "");
        AdBuddiz.setPublisherKey("ea3db525-938c-4da6-80d7-3c4e11f95566");
        AdBuddiz.cacheAds(this);
        suspectCrimeList = new ArrayList<Crime>();
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Sargento - Bigode", Color.YELLOW));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Florista - Dona Branca", Color.WHITE));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Chefe de cozinha - Tony Gourmet", Color.rgb(170, 100, 0)));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Mordomo - James", Color.BLUE));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Médica - Dona Violeta", Color.rgb(255, 150, 150)));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Dançarina - Srta. Rosa", Color.RED));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Coveiro - Sérgio Soturno", Color.BLACK));
        suspectCrimeList.add(new Crime(this, Crime.SUSPECT, "Advogado - Sr Marinho", Color.GREEN));

        gunCrimeList = new ArrayList<Crime>();
        gunCrimeList.add(new Crime(this, Crime.GUN, "Espingarda", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Pá", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Pé-de-cabra", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Tesoura", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Arma quimica", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Veneno", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Soco inglês", Color.DKGRAY));
        gunCrimeList.add(new Crime(this, Crime.GUN, "Faca", Color.DKGRAY));

        placeCrimeList = new ArrayList<Crime>();
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Prefeitura", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Restaurante", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Floricultura", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Boate", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Hospital", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Mansão", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Cemitério", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Praça", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Hotel", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Banco", Color.LTGRAY));
        placeCrimeList.add(new Crime(this, Crime.PLACE, "Estação de trem", Color.LTGRAY));
        checkCrimes();
        for(Crime crime: suspectCrimeList)
            if(crime.checked==2)
                myCardsCrimeList.add(crime);
        for(Crime crime: gunCrimeList)
            if(crime.checked==2)
                myCardsCrimeList.add(crime);
        for(Crime crime: placeCrimeList)
            if(crime.checked==2)
                myCardsCrimeList.add(crime);
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 2, getResources().getDisplayMetrics());
        //todo uncomment after set color to individual pages
        //mViewPager.setPageMargin(-margin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };
        ActionBar.Tab tab = actionBar.newTab().setText("Suspeitos");
        tab.setTabListener(tabListener);
        //tab.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Armas");
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Locais");
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Minhas cartas");
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
                if(position==3)
                    Toast.makeText(getApplicationContext(),"Toque e segure em seus itens para iniciar a transferencia",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startListen();

        AudioManager audio = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        Log.i("Music volume", audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.i("after request bluetooth enable", "bluetooth");
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Do something
                //Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                //discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                //startActivityForResult(discoverableIntent, REQUEST_DURATION_BT);
                startListen();
            }
        }
        else if(requestCode == REQUEST_DURATION_BT){
            Log.i("after request DURATION BLUETOOTH", "bluetooth");
        }
    }

    @Override
    protected void onPause() {
        if(serverBluetooth!=null) {
            if(serverBluetooth.started) {
                serverBluetooth.stopServer();
                TextView tv = (TextView) findViewById(R.id.msg_serverbluetooth);
                tv.setText("Ative o bluetooth para poder enviar os dados.");
                tv.setTextColor(Color.rgb(100, 100, 0));
                tv.setBackgroundColor(Color.rgb(230, 230, 150));
            }
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.i("onstart", "onstart");
        startListen();
        super.onStart();
    }

    public void startListen()
    {
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            if (serverBluetooth != null && serverBluetooth.started)
                serverBluetooth.stopServer();
            serverBluetooth = new ServerBluetooth(new ServerBluetooth.ReceiveBluetoothBytes() {
                @Override
                public void receiveBytes(byte[] buffer, int bytes) {
                    String data = new String(buffer, 0, bytes);
                    final String type = data.substring(0, data.indexOf("-"));
                    final int position = Integer.parseInt(data.substring(data.indexOf("-") + 1));
                    //String who = "";
                    Crime crime = null;
                    //Log.i("teste", "data received: "+crime+" "+position);
                    if (type.equals(Crime.GUN)) {
                        //gunCrimeList.get(position).setChecked(1);
                        //who = gunCrimeList.get(position).name;
                        crime = gunCrimeList.get(position);
                    }
                    else if (type.equals(Crime.PLACE)) {
                        //placeCrimeList.get(position).setChecked(1);
                        //who = placeCrimeList.get(position).name;
                        crime = placeCrimeList.get(position);
                    }
                    else if (type.equals(Crime.SUSPECT)) {
                        //suspectCrimeList.get(position).setChecked(1);
                        //who = suspectCrimeList.get(position).name;
                        crime = suspectCrimeList.get(position);
                    }
                    final String who2 = crime.name;
                    //Log.i("startListen", myCardsCrimeList.indexOf(crime)+"");
                    //Log.i("startListen checked", crime.checked+"");
                    if(crime.checked != 2)
                        crime.setChecked(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPagerAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Dados recebidos: " + who2, Toast.LENGTH_LONG).show();
                            //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.snow_bound);
                            //mediaPlayer.start();
                        }
                    });

                }
            });
            if (!serverBluetooth.started)
                serverBluetooth.startServer();
            TextView tv = (TextView) findViewById(R.id.msg_serverbluetooth);
            tv.setText("Pronto para receber cartas");
            tv.setTextColor(Color.rgb(0,100,0));
            tv.setBackgroundColor(Color.rgb(150,230,150));
        }
    }
    public String findCategoryKiller(List<Crime> listCategory)
    {
        int totalSuspects = listCategory.size();
        int currentSuspects = totalSuspects;
        String killer="";
        for(Crime suspect: listCategory)
            if(suspect.checked == 1 || suspect.checked == 2)
                currentSuspects--;
            else
                killer = suspect.name;
        if(currentSuspects==1)
            return killer;
        else
            return "";
    }
    public void checkCrimes()
    {
        crimes.put(Crime.GUN, findCategoryKiller(gunCrimeList));
        crimes.put(Crime.PLACE,findCategoryKiller(placeCrimeList));
        crimes.put(Crime.SUSPECT,findCategoryKiller(suspectCrimeList));
        if (!crimes.get(Crime.GUN).equals("") && !crimes.get(Crime.SUSPECT).equals("") && !crimes.get(Crime.PLACE).equals("")) {
            found = true;
        }
        if(found)
        {
            getSupportActionBar().setTitle("Criminoso encontrado");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(130,30,30)));
            //new AlertDialog.Builder(this)
            //        .setIcon(android.R.drawable.ic_dialog_alert)
            //        .setTitle("Suspeito encontrado")
            //        .setMessage("Quem: " + crimes.get(Crime.SUSPECT) + "\nOnde: " + crimes.get(Crime.PLACE)+ "\nCom: " + crimes.get(Crime.GUN))
            //        .show();
        }
        else{
            getSupportActionBar().setTitle("Tabela jogo detetive");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        }
    }
    private void clearCrimeLists()
    {
        AdBuddiz.showAd(PagerViewFragment.this);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Limpar suspeitas")
                .setMessage("Quer realmente limpar?")
                .setPositiveButton("Limpar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Crime suspect : suspectCrimeList) {
                            suspect.setChecked(0);
                            suspect.setMy(false);
                        }
                        for (Crime gun : gunCrimeList) {
                            gun.setChecked(0);
                            gun.setMy(false);
                        }
                        for (Crime place : placeCrimeList) {
                            place.setChecked(0);
                            place.setMy(false);
                        }
                        while(!myCardsCrimeList.isEmpty())
                            myCardsCrimeList.remove(0);
                        getSupportActionBar().setTitle("Tabela jogo detetive");
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                        mPagerAdapter.notifyDataSetChanged();
                        found = false;
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clear:
                clearCrimeLists();
                startListen();
                return true;
            case R.id.action_bluetooth:
                enableBluetooth();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void enableBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BlueTooth não é suportado", Toast.LENGTH_LONG).show();
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else
                Toast.makeText(this, "BlueTooth ja está habilitado", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Fechando tela")
                .setMessage("Tem certeza?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }


    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class PagerAdapter extends FragmentStatePagerAdapter {
        PageFragment fragments[] = new PageFragment[4];
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int i) {
            List<Crime> lists[] = new List[]{suspectCrimeList,gunCrimeList,placeCrimeList,myCardsCrimeList};
            fragments[i] = new PageFragment(lists[i]);
            return fragments[i];
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            for(int i = 0; i < fragments.length; i++)
                if(fragments[i]!=null)
                    fragments[i].crimeAdapter.notifyDataSetChanged();
        }
    }
}