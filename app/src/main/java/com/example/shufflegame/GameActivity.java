package com.example.shufflegame;

// ==========================================
// IMPORTAÇÕES
// ==========================================

// Intent
import android.content.Intent;

// Cor
import android.graphics.Color;

// Media Player (sons)
import android.media.MediaPlayer;

// Temporizador
import android.os.CountDownTimer;
import android.os.Bundle;

// Componentes
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Utilidades
import java.util.ArrayList;
import java.util.Collections;

// ==========================================
// CLASSE GAME ACTIVITY
// ==========================================
public class GameActivity extends AppCompatActivity {

    // ==========================================
    // COMPONENTES XML
    // ==========================================

    // Grid do jogo
    GridLayout gridJogo;

    // Texto tempo
    TextView txtTempo;

    // Overlay vitória
    LinearLayout layoutVitoria;

    // Overlay derrota
    LinearLayout layoutGameOver;

    // Botões
    Button btnJogarNovamente;
    Button btnMenu;
    Button btnTentarNovamente;
    Button btnMenuGameOver;

    // ==========================================
    // VARIÁVEIS JOGO
    // ==========================================

    // Lista números
    ArrayList<Integer> numeros =
            new ArrayList<>();

    // Quantidade colunas
    int colunas = 3;

    // Quantidade linhas
    int linhas = 3;

    // Tempo da partida
    int tempoPartida = 60;

    // ==========================================
    // TEMPORIZADOR
    // ==========================================

    CountDownTimer timer;

    // ==========================================
    // FIREBASE
    // ==========================================

    FirebaseAuth auth;

    DatabaseReference databaseReference;

    // ==========================================
    // SONS
    // ==========================================

    MediaPlayer somClique;

    MediaPlayer somMadeira;

    MediaPlayer somVitoria;

    MediaPlayer somGameOver;

    // ==========================================
    // onCreate
    // ==========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicializa Activity
        super.onCreate(savedInstanceState);

        // Define XML
        setContentView(R.layout.activity_game);

        // ==========================================
        // CONECTA XML
        // ==========================================

        gridJogo =
                findViewById(R.id.gridJogo);

        txtTempo =
                findViewById(R.id.txtTempo);

        layoutVitoria =
                findViewById(R.id.layoutVitoria);

        layoutGameOver =
                findViewById(R.id.layoutGameOver);

        btnJogarNovamente =
                findViewById(R.id.btnJogarNovamente);

        btnMenu =
                findViewById(R.id.btnMenu);

        btnTentarNovamente =
                findViewById(R.id.btnTentarNovamente);

        btnMenuGameOver =
                findViewById(R.id.btnMenuGameOver);

        // ==========================================
        // FIREBASE
        // ==========================================

        auth = FirebaseAuth.getInstance();

        databaseReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference("usuarios");

        // ==========================================
        // SONS
        // ==========================================

        somClique =
                MediaPlayer.create(this,
                        R.raw.click);

        somMadeira =
                MediaPlayer.create(this,
                        R.raw.wood_move);

        somVitoria =
                MediaPlayer.create(this,
                        R.raw.victory);

        somGameOver =
                MediaPlayer.create(this,
                        R.raw.gameover);

        // ==========================================
        // RECEBE DIFICULDADE
        // ==========================================

        String dificuldade =
                getIntent().getStringExtra("modo");

        // Evita crash caso venha nulo
        if(dificuldade == null){

            dificuldade = "facil";
        }

        // ==========================================
        // CONFIGURA DIFICULDADE
        // ==========================================

        configurarDificuldade(dificuldade);

        // ==========================================
        // CRIA TABULEIRO
        // ==========================================

        criarTabuleiro();

        // ==========================================
        // INICIA TIMER
        // ==========================================

        iniciarTempo();

        // ==========================================
        // BOTÃO JOGAR NOVAMENTE
        // ==========================================

        btnJogarNovamente.setOnClickListener(v -> {

            // Som clique
            somClique.start();

            // Reinicia Activity
            recreate();
        });

        // ==========================================
        // BOTÃO MENU
        // ==========================================

        btnMenu.setOnClickListener(v -> {

            somClique.start();

            finish();
        });

        // ==========================================
        // BOTÃO GAME OVER
        // ==========================================

        btnTentarNovamente.setOnClickListener(v -> {

            somClique.start();

            recreate();
        });

        // ==========================================
        // BOTÃO MENU GAME OVER
        // ==========================================

        btnMenuGameOver.setOnClickListener(v -> {

            somClique.start();

            finish();
        });
    }

    // ==========================================
    // CONFIGURA DIFICULDADE
    // ==========================================
    private void configurarDificuldade(String modo){

        // Fácil
        if(modo.equals("facil")){

            linhas = 3;

            colunas = 3;

            tempoPartida = 60;
        }

        // Médio
        else if(modo.equals("medio")){

            linhas = 4;

            colunas = 4;

            tempoPartida = 90;
        }

        // Difícil
        else{

            linhas = 5;

            colunas = 5;

            tempoPartida = 120;
        }
    }

    // ==========================================
    // CRIA TABULEIRO
    // ==========================================
    private void criarTabuleiro(){

        // Define colunas
        gridJogo.setColumnCount(colunas);

        // Limpa grid
        gridJogo.removeAllViews();

        // Limpa lista
        numeros.clear();

        // Total peças
        int total =
                linhas * colunas;

        // Adiciona números
        for(int i = 1; i < total; i++){

            numeros.add(i);
        }

        // Espaço vazio
        numeros.add(0);

        // Embaralha
        Collections.shuffle(numeros);

        // ==========================================
        // CRIA PEÇAS
        // ==========================================

        for(int i = 0; i < numeros.size(); i++){

            // Número atual
            int numero =
                    numeros.get(i);

            // Cria botão
            Button botao =
                    new Button(this);

            // Tamanho
            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 180;

            params.height = 180;

            params.setMargins(
                    8,
                    8,
                    8,
                    8);

            botao.setLayoutParams(params);

            // Fundo madeira
            botao.setBackgroundResource(
                    R.drawable.madeira_clara);

            // Cor texto
            botao.setTextColor(Color.WHITE);

            // Tamanho texto
            botao.setTextSize(24);

            // ==========================================
            // PEÇA VAZIA
            // ==========================================

            if(numero == 0){

                botao.setText("");

                botao.setVisibility(Button.INVISIBLE);

            } else {

                botao.setText(
                        String.valueOf(numero));
            }

            // ==========================================
            // EVENTO CLIQUE
            // ==========================================

            int finalI = i;

            botao.setOnClickListener(v -> {

                // Som madeira
                somMadeira.start();

                // Move peça
                moverPeca(finalI);
            });

            // Adiciona no grid
            gridJogo.addView(botao);
        }
    }

    // ==========================================
    // MOVE PEÇA
    // ==========================================
    private void moverPeca(int posicao){

        // Procura vazio
        int vazio =
                numeros.indexOf(0);

        // ==========================================
        // VERIFICA MOVIMENTO
        // ==========================================

        boolean podeMover = false;

        // Direita
        if(posicao == vazio - 1
                && vazio % colunas != 0){

            podeMover = true;
        }

        // Esquerda
        if(posicao == vazio + 1
                && posicao % colunas != 0){

            podeMover = true;
        }

        // Cima
        if(posicao == vazio - colunas){

            podeMover = true;
        }

        // Baixo
        if(posicao == vazio + colunas){

            podeMover = true;
        }

        // ==========================================
        // MOVE
        // ==========================================

        if(podeMover){

            Collections.swap(
                    numeros,
                    posicao,
                    vazio);

            atualizarGrid();

            verificarVitoria();
        }
    }

    // ==========================================
    // ATUALIZA GRID
    // ==========================================
    private void atualizarGrid(){

        for(int i = 0; i < numeros.size(); i++){

            // Botão
            Button botao =
                    (Button) gridJogo.getChildAt(i);

            // Número
            int numero =
                    numeros.get(i);

            // Peça vazia
            if(numero == 0){

                botao.setText("");

                botao.setVisibility(Button.INVISIBLE);

            } else {

                botao.setVisibility(Button.VISIBLE);

                botao.setText(
                        String.valueOf(numero));
            }
        }
    }

    // ==========================================
    // VERIFICA VITÓRIA
    // ==========================================
    private void verificarVitoria(){

        for(int i = 0; i < numeros.size() - 1; i++){

            if(numeros.get(i) != i + 1){

                return;
            }
        }

        // ==========================================
        // VENCEU
        // ==========================================

        timer.cancel();

        somVitoria.start();

        layoutVitoria.setVisibility(
                LinearLayout.VISIBLE);

        salvarRecorde();
    }

    // ==========================================
    // TIMER
    // ==========================================
    private void iniciarTempo(){

        timer =
                new CountDownTimer(
                        tempoPartida * 1000L,
                        1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        // Atualiza texto
                        txtTempo.setText(
                                String.valueOf(
                                        millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {

                        // Game over
                        somGameOver.start();

                        layoutGameOver.setVisibility(
                                LinearLayout.VISIBLE);
                    }
                };

        timer.start();
    }

    // ==========================================
    // SALVA RECORDE
    // ==========================================
    private void salvarRecorde(){

        // UID usuário
        String uid =
                auth.getCurrentUser().getUid();

        // Tempo restante
        int tempoRestante =
                Integer.parseInt(
                        txtTempo.getText().toString());

        // Salva Firebase
        databaseReference
                .child(uid)
                .child("melhorTempo")
                .setValue(tempoRestante);

        Toast.makeText(
                this,
                "Novo recorde salvo!",
                Toast.LENGTH_SHORT).show();
    }
}