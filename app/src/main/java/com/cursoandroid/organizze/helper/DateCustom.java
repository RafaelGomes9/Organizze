package com.cursoandroid.organizze.helper;



import android.util.Log;



import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;


public class DateCustom {

    private static Calendar calendario = Calendar.getInstance();
    private static String meses[] = {"01/", "02/", "03/", "04/", "05/", "06/", "07/", "08/", "09/", "10/", "11/", "12/"};
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyyyy");

    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("M");


    public static String dataFirebase(int in) {

        if (in == 0) {

            calendario.add(Calendar.MONTH, -1);


        } else if (in == 1) {

            calendario.add(Calendar.MONTH, 1);


        } else {


            calendario.add(Calendar.MONTH, 0);


        }

        return simpleDateFormat.format(calendario.getTime());


    }


    public static String mostrarData() {

        int teste = Integer.parseInt(simpleDateFormat3.format(calendario.getTime())) - 1;

        if (teste > 11) {

            teste -= 10;


        }


        String retornarData = meses[teste] + simpleDateFormat2.format(calendario.getTime());

        return retornarData;


    }


    public static String dataAtual() {

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String dataString = simpleDateFormat.format(data);

        return dataString;


    }

    public static String mesAnoDataEscolhida(String data) {


        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;

        return mesAno;


    }



public static Boolean validarData(String data)

{



   try{



        Date date= new SimpleDateFormat("dd/MM/yyyy").parse(data);
        Log.i("INFO","Data Correta!");






    }


catch (Exception e)
{

    Log.i("ERRO","Data Incorreta! "+e);
    return false;








}









    return true;





    }







    }


































