package com.example.shufflegame;

// ==========================================
// IMPORTAÇÕES
// ==========================================

// Intent
import android.content.Intent;

// Bundle
import android.os.Bundle;

// View
import android.view.View;

// Componentes
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

// ==========================================
// CLASSE MAIN ACTIVITY
// ==========================================
public class MainActivity extends AppCompatActivity {

    // ==========================================
    // COMPONENTES PERFIL
    // ==========================================

    ImageView imgAvatar;

    TextView txtNome;

    TextView txtRecorde;

    // ==========================================
    // BOTÕES
    // ==========================================

    Button btnJogar;

    Button btnFacil;

    Button btnMedio;

    Button btnDificil;

    // ==========================================
    // LAYOUTS
    // ==========================================

    LinearLayout layoutModos;

    LinearLayout layoutRanking;

    // ==========================================
    // FIREBASE
    // ==========================================

    FirebaseAuth auth;

    DatabaseReference databaseReference;

    // ==========================================
    // onCreate
    // ==========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // XML
        setContentView(R.layout.activity_main);

        // ==========================================
        // CONECTA XML
        // ==========================================

        imgAvatar =
                findViewById(R.id.imgAvatar);

        txtNome =
                findViewById(R.id.txtNome);

        txtRecorde =
                findViewById(R.id.txtRecorde);

        btnJogar =
                findViewById(R.id.btnJogar);

        btnFacil =
                findViewById(R.id.btnFacil);

        btnMedio =
                findViewById(R.id.btnMedio);

        btnDificil =
                findViewById(R.id.btnDificil);

        layoutModos =
                findViewById(R.id.layoutModos);

        layoutRanking =
                findViewById(R.id.layoutRanking);

        // ==========================================
        // FIREBASE
        // ==========================================

        auth =
                FirebaseAuth.getInstance();

        databaseReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference("usuarios");

        // ==========================================
        // CARREGA PERFIL
        // ==========================================

        carregarPerfil();

        // ==========================================
        // BOTÃO JOGAR
        // ==========================================

        btnJogar.setOnClickListener(v -> {

            // Mostra modos
            layoutModos.setVisibility(View.VISIBLE);

            // Esconde botão jogar
            btnJogar.setVisibility(View.GONE);
        });

        // ==========================================
        // FÁCIL
        // ==========================================

        btnFacil.setOnClickListener(v -> {

            abrirJogo("facil");
        });

        // ==========================================
        // MÉDIO
        // ==========================================

        btnMedio.setOnClickListener(v -> {

            abrirJogo("medio");
        });

        // ==========================================
        // DIFÍCIL
        // ==========================================

        btnDificil.setOnClickListener(v -> {

            abrirJogo("dificil");
        });

        // ==========================================
        // CARREGA RANKING
        // ==========================================

        carregarRanking();
    }

    // ==========================================
    // CARREGA PERFIL
    // ==========================================
    private void carregarPerfil(){

        FirebaseUser user =
                auth.getCurrentUser();

        if(user == null){

            return;
        }

        String uid =
                user.getUid();

        databaseReference
                .child(uid)

                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                if(snapshot.exists()){

                                    String nome =
                                            snapshot.child("nome")
                                                    .getValue(String.class);

                                    Long tempo =
                                            snapshot.child("melhorTempo")
                                                    .getValue(Long.class);

                                    String avatar =
                                            snapshot.child("avatar")
                                                    .getValue(String.class);

                                    // Nome
                                    txtNome.setText(nome);

                                    // Recorde
                                    txtRecorde.setText(
                                            "Melhor tempo: "
                                                    + tempo
                                                    + "s");

                                    // Avatar
                                    if(avatar.equals("avatar1")){

                                        imgAvatar.setImageResource(
                                                R.drawable.avatar1);
                                    }

                                    else if(avatar.equals("avatar2")){

                                        imgAvatar.setImageResource(
                                                R.drawable.avatar2);
                                    }

                                    else if(avatar.equals("avatar3")){

                                        imgAvatar.setImageResource(
                                                R.drawable.avatar3);
                                    }

                                    else{

                                        imgAvatar.setImageResource(
                                                R.drawable.avatar4);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
    }

    // ==========================================
    // ABRE JOGO
    // ==========================================
    private void abrirJogo(String modo){

        Intent intent =
                new Intent(
                        MainActivity.this,
                        GameActivity.class);

        intent.putExtra(
                "modo",
                modo);

        startActivity(intent);
    }

    // ==========================================
    // RANKING
    // ==========================================
    private void carregarRanking(){

        // Depois vamos implementar
    }
}