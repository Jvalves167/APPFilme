package com.example.appfilme.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfilme.R;
import com.example.appfilme.adapter.AdapterPublicacoes;
import com.example.appfilme.helper.ConfiguracaoFirebase;
import com.example.appfilme.helper.RecyclerItemClickListener;
import com.example.appfilme.model.Publicacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinhasPublicacoesActivity extends AppCompatActivity {

    private RecyclerView recyclerPublicacoes;
    private List<Publicacao> publicacoes = new ArrayList<>();
    private AdapterPublicacoes adapterPublicacoes;
    private DatabaseReference publicacoesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_publicacoes);



        //Configurações iniciais (pega como reerência as publicações do usurario)
        publicacoesRef = ConfiguracaoFirebase.getFirebase().child("minhas_publicacoes")
                        .child(ConfiguracaoFirebase.getIdUsuario());

        inicializarComponentes();
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarPublicacaoActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Configurar RecyclerView (listagem de anuncios)
        recyclerPublicacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerPublicacoes.setHasFixedSize(true);

        adapterPublicacoes = new AdapterPublicacoes(publicacoes, this);

        recyclerPublicacoes.setAdapter(adapterPublicacoes);

        recuperarPublicacoes();
        //evento de click para excluir anuncio
        recyclerPublicacoes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPublicacoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Publicacao publicacaoSelecionada = publicacoes.get(position);
                               Intent i = new Intent(MinhasPublicacoesActivity.this, DetalhesFilmeActivity.class);
                               i.putExtra("publicacaoSelecionada", publicacaoSelecionada);
                               startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                               exibirConfirmacao(position);

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }
    private void recuperarPublicacoes(){
        LoadingAlert loadingAlert = new LoadingAlert(MinhasPublicacoesActivity.this);
        loadingAlert.startAlertDialog();
        publicacoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publicacoes.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    publicacoes.add(ds.getValue(Publicacao.class));
                }
                Collections.reverse(publicacoes);
                adapterPublicacoes.notifyDataSetChanged();
                loadingAlert.closeAlertDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void inicializarComponentes(){
        recyclerPublicacoes = findViewById(R.id.recyclerAnuncios);
    }
    public void exibirConfirmacao(int position){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Excluindo...");
        msgBox.setMessage("Tem certeza que deseja excluir essa publicação?");

        msgBox.setPositiveButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MinhasPublicacoesActivity.this, "Exclusão cancelada!", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Publicacao publicacaoSelecionado = publicacoes.get(position);
                publicacaoSelecionado.remover();
                adapterPublicacoes.notifyDataSetChanged();
                Toast.makeText(MinhasPublicacoesActivity.this, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }


}