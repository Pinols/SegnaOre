package com.example.segnaore;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import java.io.FileWriter;
import java.lang.*;
import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import static android.graphics.Color.GREEN;

public class FileManager {
    Context ctx;
    File file;
    public FileManager(Context ctx){
        this.ctx=ctx;
        this.file=new File(ctx.getFilesDir().getPath(),"ore.txt");
    }
    public void setFile(File file){
        this.file=file;
    }
    public String readFromFile(){
        StringBuffer contents=new StringBuffer("");
        try{
            FileInputStream FIS;
            FIS=ctx.openFileInput("ore.txt");
            int n;
            while((n=FIS.read())!=-1){
                contents.append(Character.toString((char)n));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return contents.toString();
    }

    public void writeToFile(String arr,String fin,String pausa,String data){
        FileWriter FW;
        String s=new String("");
        String z=new String(readFromFile());
        System.out.println(" SCRIVENDO SU FILE ORA :::: "+data);
        try{
            FW= new FileWriter(this.file);
            FW.write(z);
            s=data+"-"+arr+"-"+fin+"-"+pausa+"#";
            FW.append(s);
            FW.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Combo> creaCombos(){
        ArrayList<Combo> combos=new ArrayList<>();
        try{
            String[] dummy;
            String read=readFromFile();             //test
            String[] readarray=read.split("#");
            for(String ss:readarray){
                dummy=ss.split("-");
                Combo combo=new Combo(dummy[0],dummy[1],dummy[2],dummy[3]);
                combos.add(combo);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return combos;
    }

    public boolean raccogliDati(View view, String data){
        Spinner s1=(Spinner)view.findViewById(R.id.spinnerarr);
        int i=s1.getSelectedItemPosition();
        String arr=(String)s1.getItemAtPosition(i);
        arr=arr.replace(":","");
        Spinner s2=(Spinner)view.findViewById(R.id.spinnerfin);
        int j= s2.getSelectedItemPosition();
        String fin=(String)s2.getItemAtPosition(j);
        fin=fin.replace(":","");

        if(arr.equals(fin)){
            return false;
        }

        Spinner s3=(Spinner)view.findViewById(R.id.spinnerPausa);
        int k=s3.getSelectedItemPosition();
        String pausa=(String)s3.getItemAtPosition(k);
        writeToFile(arr,fin,pausa,data);
        return true;
    }

    public Duration getTime(String arrivo2, String fine2, String pausa){
        int arrivo=Integer.parseInt(arrivo2);
        int fine=Integer.parseInt(fine2);
        int hrsa=arrivo/100;
        int mina=arrivo%100;
        int millia=((hrsa*60)+mina)*60000;
        int hrsf=fine/100;

        //tryng to add end hrs after midnight, the day after
        //was it that easy?
        if(hrsf<hrsa)hrsf=hrsf+24;


        int minf=fine%100;
        int millif=((hrsf*60)+minf)*60000;
        long dummy=(long)millif-(long)millia;
        /*adding - pausa*/
        int p=Integer.parseInt(pausa);
        int hrsp=p/100;
        int minp=p%100;
        int millip=((hrsp*60)+minp)*60000;
        long result=dummy-(long)millip;
        Duration d=Duration.ofMillis(result);
        return d;
    }

    public ArrayList<Button> generaBottoni(View view, Context ctx, int mese,int anno){
        ArrayList <Button> bottoni = new ArrayList<Button>();
        ArrayList<Combo> combos=creaCombos();
        combos=filtra(mese,anno,combos);
        for(int i=0;i<combos.size();i++) {
            /*if (combos.get(i) != null) {
                check = combos.get(i).getData().getMonthValue();
            }*/
            DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
            Button b = new Button(ctx);
            String s = "" + dtf.format(combos.get(i).getData()) + " Ore: " + getTime(combos.get(i).getArrivo(), combos.get(i).getFine(), combos.get(i).getPausa()).toString().replaceFirst("PT","") + " Pausa: " + combos.get(i).getPausa() + "min";
            b.setText(s);
            System.out.println(s);
            b.setId(i + 100);

            b.setBackgroundResource(R.drawable.cbutton);

            bottoni.add(b);
        }
            return bottoni;
        }

    public void removeEntry(Combo b){
        ArrayList<Combo> combos=creaCombos();
        int i=0;
        for(Combo c: combos){
            if(c.getArrivo().equals(b.getArrivo())&&c.getFine().equals(b.getFine())&&c.getDataString().equals(b.getDataString())&&c.getPausa().equals(b.getPausa()))break;
            i++;
        }
        combos.remove(i);
        emptyFile();
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
        for(Combo c :combos){
            writeToFile(c.getArrivo(),c.getFine(),c.getPausa(),dtf.format(c.getData()));
        }
    }
    public void emptyFile(){
        try{
            file=new File(ctx.getFilesDir().getPath(),"ore.txt");
            FileWriter FW= new FileWriter(file);
            FW.write("");
            FW.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Button> generate(Context ctx, View view,int mese,int anno,FileManager fm){
        ArrayList<Button> test=fm.generaBottoni(view,ctx,mese,anno);
        return test;
    }

    public ArrayList<Combo> filtra(int mese,int anno, ArrayList<Combo> combs){
        Iterator<Combo> iterator=combs.iterator();
        do{
            Combo c=new Combo();
            if(iterator.hasNext()!=false)c=iterator.next();
            if(c.getData().getMonthValue()!=mese || c.getData().getYear()!=anno){   //or è importante
                iterator.remove();
            }
        }while(iterator.hasNext());
        return combs;
    }

    public void lanciaRiepilogo(Context ctx,View view){
        try{
            Intent myint= new Intent(view.getContext(),Riepilogo.class);
            int mese=0;
            Spinner s=(Spinner)view.findViewById(R.id.spinMese);    /*mese*/
            int j=s.getSelectedItemPosition();
            String z= s.getItemAtPosition(j).toString();
            if(z.equals("Gennaio"))mese=1;
            else if(z.equals("Febbraio"))mese=2;
            else if(z.equals("Marzo"))mese=3;
            else if(z.equals("Aprile"))mese=4;
            else if(z.equals("Maggio"))mese=5;
            else if(z.equals("Giugno"))mese=6;
            else if(z.equals("Luglio"))mese=7;
            else if(z.equals("Agosto"))mese=8;
            else if(z.equals("Settembre"))mese=9;
            else if(z.equals("Ottobre"))mese=10;
            else if(z.equals("Novembre"))mese=11;
            else if(z.equals("Dicembre"))mese=12;
            int anno=0;                                                 /*anno*/
            Spinner s2=(Spinner)view.findViewById(R.id.spinAnno);
            int i=s2.getSelectedItemPosition();
            anno=Integer.parseInt(s2.getItemAtPosition(i).toString());
            if(anno!=0)myint.putExtra("anno",anno);
            if(mese!=0)myint.putExtra("mese",mese);
            view.getContext().startActivity(myint);
        }catch(Exception e){
            System.out.println("Eccezione lanciando riepilogo");
            e.printStackTrace();
        }
    }

    public String getTotMese(ArrayList<Combo> combos){
        Duration d=Duration.ZERO;
        for(Combo c:combos){
            d=d.plus(getTime(c.getArrivo(),c.getFine(),c.getPausa()));
        }
        String s=d.toString().replaceAll("PT","");
        int i=s.indexOf("H");
        if(i!=-1){
            String s1=s.substring(0,i+1);
            String s2=s.substring(i+1);
            if(!s2.equals(""))s1=s1.concat(" e ");
            String result=s1.concat(s2);
            return result;
        }
        return s;
    }

    public String aggiornaTv(){
        LocalDate ld=LocalDate.now();
        int month=ld.getMonthValue();
        int year=ld.getYear();
        ArrayList<Combo> combos=creaCombos();
        if(combos.size()!=0){
            combos=filtra(month,year,combos);
            String tot=getTotMese(combos);
            String f="Questo mese hai lavorato ";
            f=f.concat(tot);
            return f;
        }
        else return "Nessuna data questo mese.";
    }
}