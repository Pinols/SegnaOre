package com.example.segnaore;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Riepilogo extends Activity {
    ArrayList<Button> bottoni;
    ArrayList<Combo> combos;
    LinearLayout ll;
    boolean chk;
    TextView tv;
    View.OnClickListener listener;
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mese = getIntent().getIntExtra("mese", 0);
        int anno = getIntent().getIntExtra("anno",0);
        setContentView(R.layout.riepilogo);
        listener = new View.OnClickListener() {
            public void onClick(final View v1) {
                LayoutInflater inflater = (LayoutInflater) v1.getContext().getSystemService(v1.getContext().LAYOUT_INFLATER_SERVICE);
                View popv = inflater.inflate(R.layout.popoptions, null);
                final PopupWindow popopt = new PopupWindow(popv, 560, 475, true);
                popopt.showAtLocation(v1.getRootView(), Gravity.CENTER, 0, 100);
                Button yes = (Button) popv.findViewById(R.id.btnoptsi);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileManager fm = new FileManager(getApplicationContext());
                        int i = 0;
                        for (Button b : bottoni) {
                            if (b.equals((Button) v1)) break;
                            else i++;
                        }
                        if (combos != null && combos.get(i) != null) {
                            fm.removeEntry(combos.get(i));
                            combos.remove(i);
                            bottoni.remove(i);
                        }
                        ViewGroup layout = (ViewGroup) v1.getParent();       //sezione aggioranmento post eliminazione entry
                        if (null != layout) {
                            layout.removeView(v1);
                            String s = fm.getTotMese(combos);     //costruttore base
                            if (s.equals("0S")) {
                                tv.setText("Nessuna ora di lavoro nel mese scelto.");
                            } else {
                                tv.setText("Hai lavorato " + s + " questo mese");
                            }
                        }
                        popopt.dismiss();
                        Toast.makeText(getApplicationContext(), "Data rimossa", Toast.LENGTH_LONG).show();
                    }
                });
                Button no = (Button) popv.findViewById(R.id.btnoptno);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popopt.dismiss();
                    }
                });
            }
        };
        ll =findViewById(R.id.layriep);
        FileManager fm = new FileManager(getApplicationContext());
        tv = findViewById(R.id.tvriep);
        combos = fm.creaCombos();
        if(combos.size()>0){
            combos = fm.filtra(mese,anno, combos);
            String s = fm.getTotMese(combos);     //costruttore base
            if (s.equals("0S")) {
                tv.setText("Nessuna ora di lavoro nel mese scelto.");
            } else {
                tv.setText("Hai lavorato " + s + " questo mese");
            }
            //ll.addView(tv);
            bottoni = fm.generate(this, ll, mese,anno, fm);
            for (int i = 0; i < bottoni.size(); i++) {
                if (bottoni.get(i) != null) {
                    bottoni.get(i).setOnClickListener(listener);
                    ll.addView(bottoni.get(i));
                }
            }
        }
        else{
            tv.setText("Nessuna ora di lavoro nel mese scelto.");
        }
        setVisible(false);
        setVisible(true);
    }
}