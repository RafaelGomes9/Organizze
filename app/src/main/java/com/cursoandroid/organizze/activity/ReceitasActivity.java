package com.cursoandroid.organizze.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.helper.MascaraEditText;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
public class ReceitasActivity extends AppCompatActivity {


    private TextInputEditText campoData, campoCategoria,campoDescricao;


    private EditText campoValor;

    private FloatingActionButton fabSalvar;

    private Movimentacao movimentacao;

    private DatabaseReference firebaseRef= ConfiguracaoFirebase.getFirebaseDatabase();

    private FirebaseAuth autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();

    private Double receitaTotal;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);



        campoData= findViewById(R.id.editData);
        campoCategoria= findViewById(R.id.editCategoria);
        campoDescricao= findViewById(R.id.editDescricao);
        campoValor= findViewById(R.id.editValor);






            campoData.setText(DateCustom.dataAtual());



         /*   if(DateCustom.validarData(campoData.getText().toString()))
            {


                Log.i("INFO","Ok");


            }*/







        recuperarReceitaTotal();


        fabSalvar= findViewById(R.id.fabSalvar);


        fabSalvar.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {



                if(validarCamposReceita())
                {



                    movimentacao= new Movimentacao();

                    String data= campoData.getText().toString();
                    Double valorRecuperado= Double.parseDouble(campoValor.getText().toString());

                    movimentacao.setValor(valorRecuperado);
                    movimentacao.setCategoria(campoCategoria.getText().toString());
                    movimentacao.setDescricao(campoDescricao.getText().toString());
                    movimentacao.setData(data);
                    movimentacao.setTipo("r");


                    Double receitaAtualizada= receitaTotal+valorRecuperado;
                    atualizarReceita(receitaAtualizada);






                    movimentacao.salvar(data);
                    finish();

                }


            }
        });








    }




    public Boolean validarCamposReceita()
    {

        String textoValor= campoValor.getText().toString();
        String textoData= campoData.getText().toString();
        String textoCategoria= campoCategoria.getText().toString();
        String textoDescricao= campoDescricao.getText().toString();
        Boolean verificarData= DateCustom.validarData(textoData);
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();



        String retornoData[] = textoData.split("/");
      //  String dia = retornoData[0];
      //  String mes = retornoData[1];





        if(!textoValor.isEmpty() && textoValor.contains("1") | textoValor.contains("2") | textoValor.contains("3") | textoValor.contains("4") | textoValor.contains("5") | textoValor.contains("6") | textoValor.contains("7") | textoValor.contains("8") | textoValor.contains("9") | textoValor.contains("0"))
        {


            if(verificarData)
            {



                if(!textoCategoria.isEmpty())
                {


                    if (!textoDescricao.isEmpty())
                    {


                        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
                        {


                            return true;


                        }

                        else
                            {


                            Toast.makeText(ReceitasActivity.this, "Ocorreu um erro ao salvar a receita. Verifique a conex??o com a internet", Toast.LENGTH_SHORT).show();
                            return false;


                        }


                    }





                    else {



                        Toast.makeText(ReceitasActivity.this,"Descri????o n??o foi preenchida!",Toast.LENGTH_SHORT).show();
                        return false;





                    }







                }

                else {



                    Toast.makeText(ReceitasActivity.this,"Categoria n??o foi preenchida!",Toast.LENGTH_SHORT).show();
                    return false;





                }







            }

            else {



                Toast.makeText(ReceitasActivity.this,"A Data n??o foi preenchida corretamente!",Toast.LENGTH_SHORT).show();
                return false;





            }






        }

        else {



            Toast.makeText(ReceitasActivity.this,"Valor n??o foi preenchido!",Toast.LENGTH_SHORT).show();
            return false;





        }









    }















    public void recuperarReceitaTotal()
    {

        String uidUsuario= autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);


        usuarioRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Usuario usuario= snapshot.getValue(Usuario.class);
                receitaTotal= usuario.getReceitaTotal();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }




    public void atualizarReceita(Double receita)
    {


        String uidUsuario= autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);
        usuarioRef.child("receitaTotal").setValue(receita);









    }








}