package com.yafieimam.nuhanaapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class SinauActivity extends BaseDrawerActivity {
    GridView androidGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLayoutInflater().inflate(R.layout.activity_sinau, frameLayout);
//        setContentView(R.layout.activity_sinau);

        DatabaseHandler db = new DatabaseHandler(this);
        int count = db.getAksaraJawaCount();
        String[] namaAksara = new String[count];
        int[] gambarAksara = new int[count];

        int i = 0;
        List<AksaraJawa> allAksaraJawa = db.getAllAksaraJawa();
        for (AksaraJawa aksara_jawa : allAksaraJawa) {
            namaAksara[i] = aksara_jawa.getAksaraJawa();
            gambarAksara[i] = getResources().getIdentifier("aksara_" + namaAksara[i].toLowerCase(), "drawable", getPackageName());
            i++;
        }

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(SinauActivity.this, namaAksara, gambarAksara);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
//                Toast.makeText(SinauActivity.this, "GridView Item: " + namaAksara[+i], Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(SinauActivity.this);
                dialog.setContentView(R.layout.description_layout);
                dialog.setTitle("AksaraJawa " + namaAksara[+i]);

                TextView textAksara = (TextView) dialog.findViewById(R.id.nama_aksara_jawa);
                textAksara.setText(namaAksara[+i]);
                ImageView imageAksara = (ImageView) dialog.findViewById(R.id.aksara_jawa);
                imageAksara.setImageResource(gambarAksara[+i]);
                TextView textContoh = (TextView) dialog.findViewById(R.id.contoh_aksara);
                textContoh.setText(namaAksara[+i] + "" + namaAksara[i].toLowerCase());
                Button backButton = (Button) dialog.findViewById(R.id.btn_ok);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            SinauActivity.this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
