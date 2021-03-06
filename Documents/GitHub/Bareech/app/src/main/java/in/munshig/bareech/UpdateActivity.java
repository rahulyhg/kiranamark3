package in.munshig.bareech;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.widget.ToggleButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;

import in.munshig.bareech.SharedPrefs;

public class UpdateActivity extends AppCompatActivity implements Serializable{

    public int PERMISSIONS_MULTIPLE_REQUEST = 123;
    static EditText name;
    static Button unit[]=new Button[3];
    static ToggleButton small[] = new ToggleButton[6];
    static ToggleButton medium[] = new ToggleButton[4];
    static ToggleButton big[] = new ToggleButton[4];
    static Button exists, DNexists;
    static TextView sm,md,lg;
    static int unitvalue=-1;
    static int k=0;
    TextView vieweditems, saveditems;
    SharedPrefs sp;
    //  static String[] listofitems = new String[300];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        final LocalDatabase ldb = new LocalDatabase(getApplicationContext());

        final ItemModel imodel = (ItemModel) getIntent().getSerializableExtra("imodel");

        vieweditems = (TextView) findViewById(R.id.vieweditems);
        saveditems = (TextView) findViewById(R.id.saveditems);

        vieweditems.setVisibility(View.INVISIBLE);
        saveditems.setVisibility(View.INVISIBLE);

//        unit[0] = (Button)findViewById(R.id.toggleButton);
//        unit[1] = (Button)findViewById(R.id.toggleButton2);
//        unit[2] = (Button)findViewById(R.id.toggleButton3);
        small[0] = (ToggleButton)findViewById(R.id.toggleButton4);
        small[1] = (ToggleButton)findViewById(R.id.toggleButton5);
        small[2] = (ToggleButton)findViewById(R.id.toggleButton6);
        small[3] = (ToggleButton)findViewById(R.id.toggleButton7);
        small[4] = (ToggleButton)findViewById(R.id.toggleButton17);
        small[5] = (ToggleButton)findViewById(R.id.toggleButton16);
        medium[0] = (ToggleButton)findViewById(R.id.toggleButton8);
        medium[1] = (ToggleButton)findViewById(R.id.toggleButton9);
        medium[2] = (ToggleButton)findViewById(R.id.toggleButton10);
        medium[3] = (ToggleButton)findViewById(R.id.toggleButton11);
        big[0] = (ToggleButton)findViewById(R.id.toggleButton12);
        big[1] = (ToggleButton)findViewById(R.id.toggleButton13);
        big[2] = (ToggleButton)findViewById(R.id.toggleButton14);
        big[3] = (ToggleButton)findViewById(R.id.toggleButton15);

        exists = (Button)findViewById(R.id.button);
        DNexists = (Button)findViewById(R.id.button2);

        name = (EditText) findViewById(R.id.itemname);
        sm = (TextView)findViewById(R.id.textView);
        md = (TextView)findViewById(R.id.textView2);
        lg = (TextView)findViewById(R.id.textView3);

        name.setText(imodel.itemname);
        int unitkavalue = imodel.unit;
        abkyahoga(imodel.itemname, imodel.unit);

        if(imodel.one == 1)
            small[0].setChecked(true);
        if(imodel.two == 1)
            small[1].setChecked(true);
        if(imodel.five == 1)
            small[2].setChecked(true);
        if(imodel.six == 1)
            small[4].setChecked(true);
        if(imodel.ten == 1)
            small[3].setChecked(true);
        if(imodel.twelve == 1)
            small[5].setChecked(true);
        if(imodel.fifty == 1)
            medium[0].setChecked(true);
        if(imodel.hundred == 1)
            medium[1].setChecked(true);
        if(imodel.twofifty == 1)
            medium[2].setChecked(true);
        if(imodel.fivehundred == 1)
            medium[3].setChecked(true);
        if(imodel.onethousand == 1)
            big[0].setChecked(true);
        if(imodel.twothousand == 1)
            big[1].setChecked(true);
        if(imodel.twothousandfivehundred == 1)
            big[2].setChecked(true);
        if(imodel.fivethousand == 1)
            big[3].setChecked(true);

            DNexists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sm.setVisibility(View.INVISIBLE);
                    md.setVisibility(View.INVISIBLE);
                    lg.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < 6; i++) {
//                    if(i!=3)
//                    unit[i].setChecked(false);
                        small[i].setVisibility(View.INVISIBLE);
                        if(i<4)
                        medium[i].setVisibility(View.INVISIBLE);
                        if(i<4) {
                            big[i].setText(String.valueOf(Integer.parseInt(big[i].getText().toString().substring(0,1))*1000));
                            big[i].setVisibility(View.INVISIBLE);
                        }
                    }
                    startActivity(new Intent(UpdateActivity.this, ViewActivity.class));
                }
            });


            exists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String namevalue = name.getText().toString().trim();
                    String itemcode = imodel.barcode.substring(7);
                    if (!namevalue.equals("")) {

                        for (int i = 0; i < 6; i++) {
                            if (small[i].isChecked()) {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "ONE", 1);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWO", 1);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVE", 1);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TEN", 1);
                                if (i == 4)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "SIX", 1);
                                if (i == 5)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWELVE", 1);
                            } else {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "ONE", 0);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWO", 0);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVE", 0);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TEN", 0);
                                if (i == 4)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "SIX", 0);
                                if (i == 5)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWELVE", 0);
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            if (medium[i].isChecked()) {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIFTY", 1);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "HUNDRED", 1);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOFIFTY", 1);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVEHUNDRED", 1);
                            }
                            else
                            {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIFTY", 0);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "HUNDRED", 0);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOFIFTY", 0);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVEHUNDRED", 0);
                            }
                            if (big[i].isChecked()) {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "THOUSAND", 1);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOTHOUSAND", 1);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOTHOUSANDFIVEHUNDRED", 1);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVETHOUSAND", 1);
                            }
                            else
                            {
                                if (i == 0)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "THOUSAND", 0);
                                if (i == 1)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOTHOUSAND", 0);
                                if (i == 2)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "TWOTHOUSANDFIVEHUNDRED", 0);
                                if (i == 3)
                                    ldb.updatetablefor0and1(itemcode, namevalue, "FIVETHOUSAND", 0);
                            }
                        }

                        sm.setVisibility(View.INVISIBLE);
                        md.setVisibility(View.INVISIBLE);
                        lg.setVisibility(View.INVISIBLE);
                        for (int j = 0; j < 6; j++) {
//                        if (j != 3)
//                            unit[j].setChecked(false);
                            if (small[j].isChecked())
                                small[j].setChecked(false);
                            if(j<4) {
                                if (medium[j].isChecked())
                                    medium[j].setChecked(false);
                                if (big[j].isChecked())
                                    big[j].setChecked(false);

                                medium[j].setVisibility(View.INVISIBLE);
                                big[j].setText(String.valueOf(Integer.parseInt(big[j].getText().toString().substring(0,1))*1000));
                                big[j].setVisibility(View.INVISIBLE);
                            }
                            small[j].setVisibility(View.INVISIBLE);
                        }
                        Log.i("returnref", ldb.iwanttoseethereftable());
                        Log.i("return", ldb.iwanttoseethetable());
                        unitvalue = -1;
                        startActivity(new Intent(UpdateActivity.this, ViewActivity.class));
                    }
                }

            });


    }

    public void abkyahoga(String namevalue, int someunitvalue)
    {
        if (!namevalue.equals("")) {
            sm.setVisibility(View.VISIBLE);
            md.setVisibility(View.VISIBLE);
            lg.setVisibility(View.VISIBLE);
            if(someunitvalue == 0) {
                sm.setText("Gram - Small");
                md.setText("Gram - Medium");
                lg.setText("Gram - Large");
                for (int j = 0; j < 6; j++) {
                    small[j].setVisibility(View.VISIBLE);
                    if(j<4)
                        medium[j].setVisibility(View.VISIBLE);
                    if(j<4) {
                        big[j].setVisibility(View.VISIBLE);
                        int forbignum = Integer.parseInt(big[j].getText().toString())/1000;
                        big[j].setText(String.valueOf(forbignum)+"kg");
                        big[j].setTextOff(String.valueOf(forbignum)+"kg");
                        big[j].setTextOn(String.valueOf(forbignum)+"kg");
                    }
                }
            }
            else if(someunitvalue == 1) {
                sm.setText("miliLitre - Small");
                md.setText("miliLitre - Medium");
                lg.setText("miliLitre - Large");
                for (int j = 0; j < 6; j++) {
                    small[j].setVisibility(View.VISIBLE);
                    if(j<4)
                        medium[j].setVisibility(View.VISIBLE);
                    if(j<4) {
                        big[j].setVisibility(View.VISIBLE);
                        int forbignum = Integer.parseInt(big[j].getText().toString())/1000;
                        big[j].setText(String.valueOf(forbignum)+"L");
                        big[j].setTextOff(String.valueOf(forbignum)+"L");
                        big[j].setTextOn(String.valueOf(forbignum)+"L");
                    }
                }
            }
            else if(someunitvalue == 2) {
                sm.setText("unit - Small");
                md.setText("unit - Medium");
                lg.setText("unit - Large");
                for (int j = 0; j < 6; j++) {
                    small[j].setVisibility(View.VISIBLE);
                    if(j<4)
                        medium[j].setVisibility(View.VISIBLE);
                    if(j<4) {
                        big[j].setVisibility(View.VISIBLE);
//                                int forbignum = Integer.parseInt(big[j].getText().toString())/1000;
//                                big[j].setTextOff(String.valueOf(forbignum)+"");
//                                big[j].setTextOn(String.valueOf(forbignum)+"kg");
                    }
                }
            }

        }
    }


//    public void abkyahoga(String namevalue, int someunitvalue)
//    {
//        if (!namevalue.equals("")) {
//            sm.setVisibility(View.VISIBLE);
//            md.setVisibility(View.VISIBLE);
//            lg.setVisibility(View.VISIBLE);
//            if(someunitvalue == 0) {
//                sm.setText("Gram - Small");
//                md.setText("Gram - Medium");
//                lg.setText("Gram - Large");
//            }
//            else if(someunitvalue == 1) {
//                sm.setText("miliLitre - Small");
//                md.setText("miliLitre - Medium");
//                lg.setText("miliLitre - Large");
//            }
//            else if(someunitvalue == 2) {
//                sm.setText("unit - Small");
//                md.setText("unit - Medium");
//                lg.setText("unit - Large");
//            }
//            for (int j = 0; j < 6; j++) {
//                small[j].setVisibility(View.VISIBLE);
//                if(j<4)
//                medium[j].setVisibility(View.VISIBLE);
//                if(j<4)
//                big[j].setVisibility(View.VISIBLE);
//
//            }
//        }
//    }
}
