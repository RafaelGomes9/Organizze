package com.cursoandroid.organizze.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public  class MascaraEditText {

public static final String FORMATO_DATA= "##/##/####";



public static TextWatcher mascara(final EditText editText,final String mascara)
{


    return new TextWatcher() {

        boolean atualizado;
        String antigo= "";



        @Override
        public void beforeTextChanged(final CharSequence s, int start, int count, int after) {



        }

        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count)
        {



            final String str= MascaraEditText.desmascarar(s.toString());
            String mascara= "";

            if(atualizado)
            {

                antigo= str;
                atualizado=false;
                return;








            }


            int i= 0;

            for(final char m:mascara.toCharArray())
            {

                if(m!='#' && str.length()>antigo.length())

                {

                    mascara+=m;
                    continue;


                }

                try {



                    mascara+=str.charAt(i);








                }


catch (final Exception e)
{

    break;







}


  i++;









            }


            atualizado=true;
            editText.setText(mascara);
            editText.setSelection(mascara.length());




        }




        @Override
        public void afterTextChanged(final Editable s) {

        }
    };







}


public static String desmascarar(final String s)
{


    return s.replaceAll("[/]","");








}








}
