package com.example.segnaore;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class MainView extends Activity {
    PopupWindow popw;
    View popview;
    PopupWindow rieppop;
    View riepv;
    boolean check=false;
    String date;
    long l;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewone);
        Button prev = (Button) findViewById(R.id.B002);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        final FileManager fm=new FileManager(getBaseContext());
        Button add= (Button) findViewById(R.id.B003);
        CalendarView cv=(CalendarView) findViewById(R.id.calendar);
        LocalDate ld=LocalDate.now();
        int month=ld.getMonthValue();
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
        date= ld.format(dtf);
        OnDateChangeListener listener= new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                month=month+1;
                String d,m,y;                                       // Formatting value of date to standard dd/mm/yyyy
                if(day<10)d="0"+String.valueOf(day);
                else d=String.valueOf(day);
                if(month<10)m="0"+String.valueOf(month);
                else m=String.valueOf(month);
                y=String.valueOf(year);
                date=""+d+"/"+m+"/"+y;
            }
        };
        cv.setOnDateChangeListener(listener);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LayoutInflater inflater=(LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);             //creating popup
                popview=inflater.inflate(R.layout.addpop,null);
                check=true;
                popw=new PopupWindow(popview,850,1125,true);
                popw.showAtLocation(view, Gravity.CENTER,0,0);
                if(check){
                    TextView tvday=popview.findViewById(R.id.tvday);
                    tvday.setText("Giorno selezionato: "+date);
                    Button addore=(Button)popview.findViewById(R.id.B004);
                    /*cosmetico*/
                    Spinner s1=(Spinner)popview.findViewById(R.id.spinnerarr);
                    Spinner s2=(Spinner)popview.findViewById(R.id.spinnerfin);
                    Spinner s3=(Spinner)popview.findViewById(R.id.spinnerPausa);
                    s1.setSelection(32);
                    s2.setSelection(68);
                    s3.setSelection(1);
                    //
                    addore.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            boolean flag=fm.raccogliDati(popview,date);

                            if(flag==true){
                                Toast.makeText(getApplicationContext(),"Data aggiunta",Toast.LENGTH_LONG).show();
                                TextView tv = findViewById(R.id.tvtest);
                                tv.setText(fm.aggiornaTv());
                                popw.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Data NON aggiunta, arrivo uguale ad uscita.",Toast.LENGTH_LONG).show();
                                popw.dismiss();
                            }
                        }
                    });
                }
            }
        });

        TextView tv = findViewById(R.id.tvtest);
        tv.setText(fm.aggiornaTv());
        Button riepilogo=findViewById(R.id.bRiepilogo);
        riepilogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LayoutInflater inflater=(LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);             //creating popup
                riepv=inflater.inflate(R.layout.popriepilogo,null);
                check=true;
                rieppop=new PopupWindow(riepv,675,765,true);
                rieppop.showAtLocation(view, Gravity.CENTER,0,0);
                if(check){
                    Button confirm=(Button)riepv.findViewById(R.id.buttonRiep);
                    confirm.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            rieppop.dismiss();
                            fm.lanciaRiepilogo(getApplicationContext(),riepv);
                        }
                    });
                }
            }
        });
    }

    protected void onResume () {
        super.onResume();
        FileManager fm = new FileManager(this);
        TextView tv = findViewById(R.id.tvtest);
        tv.setText(fm.aggiornaTv());
    }
}