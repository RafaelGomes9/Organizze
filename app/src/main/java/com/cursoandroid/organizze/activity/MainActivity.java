package com.cursoandroid.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity
{


private ViewPager viewPager;
private ViewPagerAdapter viewPagerAdapter;
private LinearLayout layoutDots;
private TextView[]dots;
private int[]layouts;

private FirebaseAuth autenticacao;

public static Activity main;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);




       main=this;

       viewPager= findViewById(R.id.viewPager);
       layoutDots=findViewById(R.id.layoutDots);

       layouts= new int[]{R.layout.intro_1,R.layout.intro_2,R.layout.intro_3,R.layout.intro_4,R.layout.intro_cadastro};



       viewPagerAdapter= new ViewPagerAdapter();
       viewPager.setAdapter(viewPagerAdapter);
       viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        adicionarDots(0);

        getSupportActionBar().hide();






       // autenticacao.signOut();



    }








    private void adicionarDots(int paginaAtual)
    {
        dots = new TextView[layouts.length];

        int corAtivada = getResources().getColor(R.color.dot_ativado);
        int corDesativada = getResources().getColor(R.color.dot_desativado);

        layoutDots.removeAllViews();

        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(corDesativada);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[paginaAtual].setTextColor(corAtivada);
    }












   ViewPager.OnPageChangeListener viewPagerPageChangeListener= new ViewPager.OnPageChangeListener()
   {
       @Override
       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

       }

       @Override
       public void onPageSelected(int position) {


           adicionarDots(position);

       }

       @Override
       public void onPageScrollStateChanged(int state) {


       }
   };
















        public class ViewPagerAdapter extends PagerAdapter

{

    private LayoutInflater layoutInflater;

    public ViewPagerAdapter() {
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);

        return view;
    }






    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}









public void btEntrar(View view)
{

startActivity(new Intent(this, LoginActivity.class));





}




public void btCadastrar(View view)
{

startActivity(new Intent(this, CadastroActivity.class));




}



public void verificarUsuarioLogado()
{

    autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();

//autenticacao.signOut();




    if(autenticacao.getCurrentUser() !=null)
    {

        abrirTelaPrincipal();







    }

else {



    Log.i("INFO","Deslogado!!");




    }








}



    public void abrirTelaPrincipal()
    {

        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
        Log.i("INFO","Logado!!");








    }


    @Override
    protected void onStart()
    {
        super.onStart();
        verificarUsuarioLogado();

    }





}













