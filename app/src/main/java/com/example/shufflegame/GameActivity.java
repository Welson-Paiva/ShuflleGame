package com.example.shufflegame;

// ==========================================
// IMPORTAÇÕES
// ==========================================

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

// ==========================================
// CLASSE GAME ACTIVITY
// ==========================================
public class GameActivity extends AppCompatActivity {

    // ==========================================
    // COMPONENTES XML
    // ==========================================

    GridLayout gridJogo;
    TextView txtTempo;
    LinearLayout layoutVitoria;
    LinearLayout layoutGameOver;
    Button btnJogarNovamente;
    Button btnMenu;
    Button btnTentarNovamente;
    Button btnMenuGameOver;
    Button btnDesistir;

    // ==========================================
    // VARIÁVEIS JOGO
    // ==========================================

    ArrayList<Integer> numeros = new ArrayList<>();
    int colunas = 3;
    int linhas = 3;
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

    private SoundPool soundPool;
    private int somClique;
    private int somMadeira;
    private int somVitoria;
    private int somGameOver;

    // ==========================================
    // onCreate
    // ==========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // ==========================================
        // CONECTA XML
        // ==========================================

        gridJogo         = findViewById(R.id.gridJogo);
        txtTempo         = findViewById(R.id.txtTempo);
        layoutVitoria    = findViewById(R.id.layoutVitoria);
        layoutGameOver   = findViewById(R.id.layoutGameOver);
        btnJogarNovamente= findViewById(R.id.btnJogarNovamente);
        btnMenu          = findViewById(R.id.btnMenu);
        btnTentarNovamente = findViewById(R.id.btnTentarNovamente);
        btnMenuGameOver  = findViewById(R.id.btnMenuGameOver);
        btnDesistir      = findViewById(R.id.btnDesistir);

        // ==========================================
        // FIREBASE
        // ==========================================

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // ==========================================
        // SONS
        // ==========================================

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(20)
                .setAudioAttributes(audioAttributes)
                .build();

        somClique   = soundPool.load(this, R.raw.click, 1);
        somMadeira  = soundPool.load(this, R.raw.wood_move, 1);
        somVitoria  = soundPool.load(this, R.raw.victory, 1);
        somGameOver = soundPool.load(this, R.raw.gameover, 1);

        // ==========================================
        // RECEBE DIFICULDADE
        // ==========================================

        String dificuldade = getIntent().getStringExtra("modo");

        if (dificuldade == null) {
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
            tocarSom(somClique);
            recreate();
        });

        // ==========================================
        // BOTÃO MENU
        // ==========================================

        btnMenu.setOnClickListener(v -> {
            tocarSom(somClique);
            finish();
        });

        // ==========================================
        // BOTÃO TENTAR NOVAMENTE (GAME OVER)
        // ==========================================

        btnTentarNovamente.setOnClickListener(v -> {
            tocarSom(somClique);
            recreate();
        });

        // ==========================================
        // BOTÃO MENU GAME OVER
        // ==========================================

        btnMenuGameOver.setOnClickListener(v -> {
            tocarSom(somClique);
            finish();
        });

        // ==========================================
        // BOTÃO DESISTIR
        // ==========================================

        btnDesistir.setOnClickListener(v -> {
            tocarSom(somClique);
            confirmarDesistencia();
        });
    }

    // ==========================================
    // TOCA SOM
    // ==========================================
    // CORREÇÃO: método movido para fora do onCreate (estava declarado dentro dele)
    private void tocarSom(int som) {
        soundPool.play(som, 1f, 1f, 1, 0, 1f);
    }

    // ==========================================
    // CONFIGURA DIFICULDADE
    // ==========================================
    private void configurarDificuldade(String modo) {

        if (modo.equals("facil")) {
            linhas = 3;
            colunas = 3;
            tempoPartida = 90;

        } else if (modo.equals("medio")) {
            linhas = 4;
            colunas = 4;
            tempoPartida = 120;

        } else {
            linhas = 5;
            colunas = 5;
            tempoPartida = 240;
        }
    }

    // ==========================================
    // CRIA TABULEIRO
    // ==========================================
    private void criarTabuleiro() {

        gridJogo.setColumnCount(colunas);
        gridJogo.removeAllViews();
        numeros.clear();

        int total = linhas * colunas;

        for (int i = 1; i < total; i++) {
            numeros.add(i);
        }

        numeros.add(0);
        Collections.shuffle(numeros);

        // ==========================================
        // CRIA PEÇAS
        // ==========================================

        for (int i = 0; i < numeros.size(); i++) {

            int numero = numeros.get(i);

            Button botao = new Button(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 180;
            params.height = 180;
            params.setMargins(8, 8, 8, 8);
            botao.setLayoutParams(params);

            botao.setBackgroundResource(R.drawable.madeira_clara);
            botao.setTextColor(Color.WHITE);
            botao.setTextSize(24);

            if (numero == 0) {
                botao.setText("");
                botao.setVisibility(Button.INVISIBLE);
            } else {
                botao.setText(String.valueOf(numero));
            }

            // ==========================================
            // EVENTO CLIQUE
            // CORREÇÃO: listener único, sem aninhamento e sem chamada dupla de moverPeca
            // ==========================================

            int finalI = i;

            botao.setOnClickListener(v -> {
                moverPeca(finalI);
            });

            gridJogo.addView(botao);
        }
    }

    // ==========================================
    // MOVE PEÇA
    // ==========================================
    private void moverPeca(int posicao) {

        int vazio = numeros.indexOf(0);

        boolean podeMover = false;

        // Direita
        if (posicao == vazio - 1 && vazio % colunas != 0) {
            podeMover = true;
        }

        // Esquerda
        if (posicao == vazio + 1 && posicao % colunas != 0) {
            podeMover = true;
        }

        // Cima
        if (posicao == vazio - colunas) {
            podeMover = true;
        }

        // Baixo
        if (posicao == vazio + colunas) {
            podeMover = true;
        }

        if (podeMover) {
            tocarSom(somMadeira);
            Collections.swap(numeros, posicao, vazio);
            atualizarGrid();
            verificarVitoria();
        }
    }

    // ==========================================
    // ATUALIZA GRID
    // ==========================================
    private void atualizarGrid() {

        for (int i = 0; i < numeros.size(); i++) {

            Button botao = (Button) gridJogo.getChildAt(i);
            int numero   = numeros.get(i);

            if (numero == 0) {
                botao.setText("");
                botao.setVisibility(Button.INVISIBLE);
            } else {
                botao.setVisibility(Button.VISIBLE);
                botao.setText(String.valueOf(numero));
            }
        }
    }

    // ==========================================
    // VERIFICA VITÓRIA
    // ==========================================
    private void verificarVitoria() {

        for (int i = 0; i < numeros.size() - 1; i++) {
            if (numeros.get(i) != i + 1) {
                return;
            }
        }

        timer.cancel();
        tocarSom(somVitoria);
        layoutVitoria.setVisibility(LinearLayout.VISIBLE);
        salvarRecorde();
    }

    // ==========================================
    // TIMER
    // ==========================================
    private void iniciarTempo() {

        timer = new CountDownTimer(tempoPartida * 1000L, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txtTempo.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tocarSom(somGameOver);
                layoutGameOver.setVisibility(LinearLayout.VISIBLE);
            }
        };

        timer.start();
    }

    // ==========================================
    // SALVA RECORDE
    // CORREÇÃO: agora verifica se o novo tempo é melhor antes de salvar
    // ==========================================
    private void salvarRecorde() {

        if (auth.getCurrentUser() == null) {
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        int tempoRestante = Integer.parseInt(txtTempo.getText().toString());

        // Lê o recorde atual antes de sobrescrever
        databaseReference.child(uid).child("melhorTempo")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Se não existe recorde anterior, salva direto
                        if (!snapshot.exists()) {
                            gravarTempo(uid, tempoRestante);
                            return;
                        }

                        int melhorAnterior = snapshot.getValue(Integer.class);

                        // Tempo maior = restou mais tempo = melhor desempenho
                        if (tempoRestante > melhorAnterior) {
                            gravarTempo(uid, tempoRestante);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Falha silenciosa; pode-se exibir um Toast se necessário
                    }
                });
    }

    // ==========================================
    // GRAVA TEMPO NO FIREBASE
    // ==========================================
    private void gravarTempo(String uid, int tempo) {

        databaseReference.child(uid).child("melhorTempo").setValue(tempo);

        Toast.makeText(this, "Novo recorde salvo!", Toast.LENGTH_SHORT).show();
    }

    // ==========================================
    // CONFIRMA DESISTÊNCIA
    // ==========================================
    private void confirmarDesistencia() {

        // Pausa o timer enquanto o diálogo está aberto
        timer.cancel();

        new AlertDialog.Builder(this)
                .setTitle("Desistir?")
                .setMessage("Tem certeza que quer abandonar a partida?")

                // Confirma: vai pro menu
                .setPositiveButton("Desistir", (dialog, which) -> {
                    tocarSom(somGameOver);
                    finish();
                })

                // Cancela: retoma o timer do tempo restante
                .setNegativeButton("Continuar", (dialog, which) -> {

                    int tempoRestante =
                            Integer.parseInt(txtTempo.getText().toString());

                    timer = new CountDownTimer(tempoRestante * 1000L, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            txtTempo.setText(
                                    String.valueOf(millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            tocarSom(somGameOver);
                            layoutGameOver.setVisibility(LinearLayout.VISIBLE);
                        }
                    };

                    timer.start();
                })

                // Toca clique e retoma se fechar sem escolher
                .setOnCancelListener(dialog -> {

                    int tempoRestante =
                            Integer.parseInt(txtTempo.getText().toString());

                    timer = new CountDownTimer(tempoRestante * 1000L, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            txtTempo.setText(
                                    String.valueOf(millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            tocarSom(somGameOver);
                            layoutGameOver.setVisibility(LinearLayout.VISIBLE);
                        }
                    };

                    timer.start();
                })
                .show();
    }
}