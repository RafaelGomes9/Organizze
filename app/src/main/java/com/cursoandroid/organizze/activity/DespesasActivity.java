package com.cursoandroid.organizze.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {


    private TextInputEditText campoData, campoCategoria,campoDescricao;


     private EditText campoValor;

     private FloatingActionButton fabSalvar;

     private Movimentacao movimentacao;

     private DatabaseReference firebaseRef= ConfiguracaoFirebase.getFirebaseDatabase();

     private FirebaseAuth autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();

     private Double despesaTotal;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);


        campoData= findViewById(R.id.editData);
        campoCategoria= findViewById(R.id.editCategoria);
        campoDescricao= findViewById(R.id.editDescricao);
        campoValor= findViewById(R.id.editValor);



            campoData.setText(DateCustom.dataAtual());

        recuperarDespesaTotal();

        fabSalvar= findViewById(R.id.fabSalvar);



        fabSalvar.setOnClickListener(new View.OnClickListener()
       {

            @Override
            public void onClick(View v)
            {



    if (validarCamposDespesa())
    {


        movimentacao = new Movimentacao();

        String data = campoData.getText().toString();
        Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

        movimentacao.setValor(valorRecuperado);
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(data);
        movimentacao.setTipo("d");


        Double despesaAtualizada = despesaTotal + valorRecuperado;
        atualizarDespesa(despesaAtualizada);


        movimentacao.salvar(data);
        finish();

    }









}



        });

    }







    public Boolean validarCamposDespesa()
    {

        String textoValor= campoValor.getText().toString();
        String textoData= campoData.getText().toString();
        String textoCategoria= campoCategoria.getText().toString();
        String textoDescricao= campoDescricao.getText().toString();
        Boolean verificarData= DateCustom.validarData(textoData);
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();



        if(!textoValor.isEmpty() && textoValor.contains("1") | textoValor.contains("2") | textoValor.contains("3") | textoValor.contains("4") | textoValor.contains("5") | textoValor.contains("6") | textoValor.contains("7") | textoValor.contains("8") | textoValor.contains("9") | textoValor.contains("0"))
        {


            if(verificarData)
            {





                if(!textoCategoria.isEmpty())
                {


                    if(!textoDescricao.isEmpty())
                    {

                        if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
                        {





                            return true;








                        }



                        else {


                            Toast.makeText(DespesasActivity.this,"Ocorreu um erro ao salvar a receita. Verifique a conexão com a internet",Toast.LENGTH_SHORT).show();
                            return false;





                        }











                    }

                    else {



                        Toast.makeText(DespesasActivity.this,"Descrição não foi preenchida!",Toast.LENGTH_SHORT).show();
                        return false;





                    }







                }

                else {



                    Toast.makeText(DespesasActivity.this,"Categoria não foi preenchida!",Toast.LENGTH_SHORT).show();
                    return false;





                }







            }

            else {



                Toast.makeText(DespesasActivity.this,"A Data não foi preenchida corretamente!",Toast.LENGTH_SHORT).show();
                return false;





            }






        }

        else {



            Toast.makeText(DespesasActivity.this,"Valor não foi preenchido!",Toast.LENGTH_SHORT).show();
            return false;





        }









    }


public void recuperarDespesaTotal()
{

    String uidUsuario= autenticacao.getCurrentUser().getUid();
    DatabaseReference usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);


    usuarioRef.addValueEventListener(new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {


            Usuario usuario= snapshot.getValue(Usuario.class);
            despesaTotal= usuario.getDespesaTotal();




        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });








}

public void atualizarDespesa(Double despesa)
{


    String uidUsuario= autenticacao.getCurrentUser().getUid();
    DatabaseReference usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);
    usuarioRef.child("despesaTotal").setValue(despesa);









}













}