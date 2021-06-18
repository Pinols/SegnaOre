package com.example.segnaore;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Combo {
    LocalDate data;
    String arrivo;
    String fine;
    String pausa;
    public Combo(){

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Combo(String data, String arrivo, String fine, String pausa){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
        this.data=LocalDate.parse(data,dtf);
        this.arrivo=arrivo;
        this.fine=fine;
        this.pausa=pausa;
    }
    public LocalDate getData() {
        return data;
    }
    public String getDataString(){
        return this.data.toString();
    };
    public String getArrivo() {
        return arrivo;
    }
    public String getFine() {
        return fine;
    }
    public String getPausa(){return pausa; }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public void setArrivo(String arrivo) {
        this.arrivo = arrivo;
    }
    public void setFine(String fine) {
        this.fine = fine;
    }
    public void setPausa(String pausa){this.pausa=pausa; }

    @Override
    public String toString(){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
        return "Data: "+dtf.format(this.data)+" Arrivo: "+this.arrivo+" Fine: "+this.fine+" Pausa: "+this.pausa+"";
    }
}
