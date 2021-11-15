package com.cursoandroid.organizze.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.adapter.AdapterMovimentacao;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();
    private TextView textoSaldo;
    private TextView textoSaudacao;
    private Double despesaTotal=0.0;
    private Double receitaTotal=0.0;
    private Double resumoUsuario=0.0;
    private DatabaseReference firebaseRef= ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerMovimentacao;
    private static String mesAnoFirebase;
    private TextView textoMesAno;
    private Movimentacao movimentacao;



    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao>movimentacoes = new ArrayList<>();


    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);


        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo= connectivityManager.getActiveNetworkInfo();

        verificarConexaoInternet();





        mesAnoFirebase= DateCustom.dataFirebase(2);

        textoSaldo= findViewById(R.id.textSaldo);
        textoSaudacao= findViewById(R.id.textSaudacao);





        textoMesAno= findViewById(R.id.textoMesAno);
        textoMesAno.setText(DateCustom.mostrarData());





        recyclerView= findViewById(R.id.recyclerMovimentos);
        swipe();

          adapterMovimentacao= new AdapterMovimentacao(movimentacoes,this);

           RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
           recyclerView.setLayoutManager(layoutManager);
           recyclerView.setHasFixedSize(true);
           recyclerView.setAdapter(adapterMovimentacao);



        FloatingActionButton fab = findViewById(R.id.fab);

        FloatingActionButton fabDespesa= findViewById(R.id.fabDespesa);
        FloatingActionButton fabReceita= findViewById(R.id.fabReceita);


        LinearLayout despesaLayout= findViewById(R.id.despesasLayout);
        LinearLayout receitaLayout=  findViewById(R.id.receitaLayout);

        Animation mostrarBotao= AnimationUtils.loadAnimation(PrincipalActivity.this,R.anim.mostrar_botao);
        Animation ocultarBotao= AnimationUtils.loadAnimation(PrincipalActivity.this,R.anim.ocultar_botao);

        Animation mostrarLayout= AnimationUtils.loadAnimation(PrincipalActivity.this,R.anim.mostrar_layout);
        Animation ocultarLayout= AnimationUtils.loadAnimation(PrincipalActivity.this,R.anim.ocultar_layout);


        despesaLayout.setVisibility(View.GONE);
        receitaLayout.setVisibility(View.GONE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(despesaLayout.getVisibility()==View.VISIBLE && receitaLayout.getVisibility()==View.VISIBLE)
                {

                    despesaLayout.setVisibility(View.GONE);
                    receitaLayout.setVisibility(View.GONE);
                    receitaLayout.startAnimation(ocultarLayout);
                    despesaLayout.startAnimation(ocultarLayout);
                    fab.startAnimation(ocultarBotao);





                }



                else{




                    despesaLayout.setVisibility(View.VISIBLE);
                    receitaLayout.setVisibility(View.VISIBLE);
                    receitaLayout.startAnimation(mostrarLayout);
                    despesaLayout.startAnimation(mostrarLayout);
                    fab.startAnimation(mostrarBotao);











                }










            }
        });


        fabDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                despesaLayout.setVisibility(View.GONE);
                receitaLayout.setVisibility(View.GONE);
                receitaLayout.startAnimation(ocultarLayout);
                despesaLayout.startAnimation(ocultarLayout);
                fab.startAnimation(ocultarBotao);
                startActivity(new Intent(PrincipalActivity.this,DespesasActivity.class));





            }
        });



fabReceita.setOnClickListener(new View.OnClickListener() {
    @Override
        public void onClick(View v) {




        despesaLayout.setVisibility(View.GONE);
        receitaLayout.setVisibility(View.GONE);
        receitaLayout.startAnimation(ocultarLayout);
        despesaLayout.startAnimation(ocultarLayout);
        fab.startAnimation(ocultarBotao);
        startActivity(new Intent(PrincipalActivity.this,ReceitasActivity.class));




    }
});








    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_principal,menu);



        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId())
        {


                case R.id.menuSair:
                autenticacao.signOut();
                startActivity(new Intent(PrincipalActivity.this,MainActivity.class));
                finish();
                break;







        }





        return super.onOptionsItemSelected(item);
    }













public void recuperarResumo()
{

    String uidUsuario= autenticacao.getCurrentUser().getUid();
    usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);

    Log.i("Evento","evento foi adicionado!");


  valueEventListenerUsuario= usuarioRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Usuario usuario= snapshot.getValue(Usuario.class);
            despesaTotal= usuario.getDespesaTotal();
            receitaTotal= usuario.getReceitaTotal();
            resumoUsuario= receitaTotal-despesaTotal;

            DecimalFormat decimalFormat= new DecimalFormat("0.##");
            String resultadoFormatado= decimalFormat.format(resumoUsuario);

            textoSaudacao.setText("Olá, "+usuario.getNome());
            textoSaldo.setText("R$ "+resultadoFormatado);





        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });











}


    public void buscarTransacao()
    {

        String uidUsuario= autenticacao.getCurrentUser().getUid();
        movimentacaoRef= firebaseRef.child("movimentacao").child(uidUsuario).child(mesAnoFirebase);


        valueEventListenerMovimentacao=movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                movimentacoes.clear();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {

                    Movimentacao movimentacao= snapshot1.getValue(Movimentacao.class);
                    movimentacao.setKey(snapshot1.getKey());
                    movimentacoes.add(movimentacao);







                }


                adapterMovimentacao.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













    }





















    public void voltar(View view)

{


        networkInfo= connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
    {
        mesAnoFirebase = DateCustom.dataFirebase(0);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
        buscarTransacao();
        textoMesAno.setText(DateCustom.mostrarData());

    }

    else{


            mesAnoFirebase = DateCustom.dataFirebase(0);
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
            Toast.makeText(PrincipalActivity.this,"Não foi possível recuperar suas movimentações. Verifique sua conexão com a internet",Toast.LENGTH_SHORT).show();
            textoMesAno.setText(DateCustom.mostrarData());






        }






}




    public void avancar(View view)

    {


        networkInfo= connectivityManager.getActiveNetworkInfo();


        if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
        {
            mesAnoFirebase = DateCustom.dataFirebase(1);
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
            buscarTransacao();
            textoMesAno.setText(DateCustom.mostrarData());

        }

        else{


            mesAnoFirebase = DateCustom.dataFirebase(1);
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
            Toast.makeText(PrincipalActivity.this,"Não foi possível recuperar suas movimentações. Verifique sua conexão com a internet",Toast.LENGTH_SHORT).show();
            textoMesAno.setText(DateCustom.mostrarData());






        }






        }
















    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        buscarTransacao();
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento","evento foi removido!");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
       deslogarUsuario();




    }



    public void deslogarUsuario()
    {


        autenticacao.signOut();








    }



    public void swipe()
{

    ItemTouchHelper.Callback itemTouch= new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

            int dragFlags= ItemTouchHelper.ACTION_STATE_IDLE;
            int swipeFlags= ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {



            excluirMovimentacao(viewHolder);








        }
    };


    new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);



}



public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder) {


    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle("Excluir movimentação da Conta");
    alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação da sua conta ");
    alertDialog.setCancelable(false);


    alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            networkInfo= connectivityManager.getActiveNetworkInfo();

            if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
            {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String uidUsuario = autenticacao.getCurrentUser().getUid();
                movimentacaoRef = firebaseRef.child("movimentacao").child(uidUsuario).child(mesAnoFirebase);
                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();
                Toast.makeText(PrincipalActivity.this, "Movimentação excluida", Toast.LENGTH_SHORT).show();
            }


            else {


                Toast.makeText(PrincipalActivity.this, "Ocorreu um erro ao excluir a movimentação. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();




            }





        }
    });




    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            Toast.makeText(PrincipalActivity.this,"Cancelado",Toast.LENGTH_SHORT).show();
            adapterMovimentacao.notifyDataSetChanged();









        }
    });

alertDialog.create();
alertDialog.show();


}



public void atualizarSaldo()
{

    String uidUsuario= autenticacao.getCurrentUser().getUid();
    usuarioRef= firebaseRef.child("usuarios").child(uidUsuario);


    if(movimentacao.getTipo().equals("r"))
{

receitaTotal= receitaTotal-movimentacao.getValor();
usuarioRef.child("receitaTotal").setValue(receitaTotal);






}

if(movimentacao.getTipo().equals("d"))

{

    despesaTotal= despesaTotal-movimentacao.getValor();
    usuarioRef.child("despesaTotal").setValue(despesaTotal);







}


}







public void verificarConexaoInternet()
{

    if(!(networkInfo!=null && networkInfo.isConnectedOrConnecting()))
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Erro");
        alertDialog.setMessage("Não foi possível recuperar suas movimentações. Verifique sua conexão com a internet");
        alertDialog.setCancelable(false);


        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialog.create();
        alertDialog.show();


    }


}

}