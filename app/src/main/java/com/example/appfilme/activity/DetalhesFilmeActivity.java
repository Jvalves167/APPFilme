package com.example.appfilme.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfilme.R;
import com.example.appfilme.adapter.AdapterPublicacoes;
import com.example.appfilme.helper.ConfiguracaoFirebase;
import com.example.appfilme.model.Publicacao;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetalhesFilmeActivity extends AppCompatActivity {


    private TextView titulo;
    private TextView categoria;
    private ImageView imagem;
    private TextView avaliacao;
    private Publicacao publicacaoSelecionada;
    private FirebaseAuth autenticacao;
    private AdapterPublicacoes adapterPublicacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_filme);
        inicializarComponentes();


        //visibilidade();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        publicacaoSelecionada = (Publicacao) getIntent().getSerializableExtra("publicacaoSelecionada");
        String urlString = publicacaoSelecionada.getFotos().get(0);
        //Picasso.get().load(urlString).into(imagem);


        if(publicacaoSelecionada != null){

            titulo.setText(publicacaoSelecionada.getTitulo());
            avaliacao.setText(publicacaoSelecionada.getDescricao());
            categoria.setText(publicacaoSelecionada.getCategoria());
            Picasso.get().load(urlString).into(imagem);





        }
    }
    public void abrirComentarios (View view){
        if( autenticacao.getCurrentUser()==null){//usuario deslogado
            Toast.makeText(DetalhesFilmeActivity.this, "Faça o login para acessar os comentários!", Toast.LENGTH_SHORT).show();
        }else{//usuario logado
            Intent i = new Intent(DetalhesFilmeActivity.this, ComentariosActivity.class);
            i.putExtra("idPostagem",publicacaoSelecionada.getIdPublicacao());
            startActivity(i);
        }

    }
    private void inicializarComponentes(){

        titulo = findViewById(R.id.textTituloDetalhe);
        categoria = findViewById(R.id.textCategoriaDetalhe);
        avaliacao = findViewById(R.id.textAvaliacaoDetalhe);
        imagem = findViewById(R.id.imageViewPubli);


    }


}