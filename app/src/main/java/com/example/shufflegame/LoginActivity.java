package com.example.shufflegame;

// ==========================================
// IMPORTAÇÕES
// ==========================================

// Intent para trocar de tela
import android.content.Intent;

// Bundle da Activity
import android.os.Bundle;

// Detecta toques
import android.view.MotionEvent;

// Mostrar senha
import android.text.method.HideReturnsTransformationMethod;

// Esconder senha
import android.text.method.PasswordTransformationMethod;

// Componentes visuais
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Compatibilidade AppCompat
import androidx.appcompat.app.AppCompatActivity;

// Firebase Authentication
import com.google.firebase.auth.FirebaseAuth;

// ==========================================
// CLASSE LOGIN ACTIVITY
// ==========================================
public class LoginActivity extends AppCompatActivity {

    // ==========================================
    // CAMPOS
    // ==========================================

    // Campo email
    EditText edtEmail;

    // Campo senha
    EditText edtSenha;

    // ==========================================
    // BOTÃO LOGIN
    // ==========================================

    Button btnLogin;

    // ==========================================
    // TEXTO CADASTRO
    // ==========================================

    TextView txtCadastro;

    // ==========================================
    // FIREBASE AUTH
    // ==========================================

    FirebaseAuth auth;

    // ==========================================
    // onCreate
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicializa Activity
        super.onCreate(savedInstanceState);

        // Define XML da tela
        setContentView(R.layout.activity_login);

        // ==========================================
        // CONECTA XML AO JAVA
        // ==========================================

        // Campo email
        edtEmail =
                findViewById(R.id.edtEmail);

        // Campo senha
        edtSenha =
                findViewById(R.id.edtSenha);

        // Botão login
        btnLogin =
                findViewById(R.id.btnLogin);

        // Texto cadastro
        txtCadastro =
                findViewById(R.id.txtCadastro);

        // ==========================================
        // INICIALIZA FIREBASE AUTH
        // ==========================================

        auth = FirebaseAuth.getInstance();

        // ==========================================
        // MOSTRAR / ESCONDER SENHA
        // ==========================================

        edtSenha.setOnTouchListener((v, event) -> {

            // Drawable da direita
            final int DRAWABLE_RIGHT = 2;

            // Verifica ação do toque
            if(event.getAction() == MotionEvent.ACTION_UP){

                // Verifica clique no ícone
                if(event.getRawX() >=
                        (edtSenha.getRight()
                                - edtSenha
                                .getCompoundDrawables()[DRAWABLE_RIGHT]
                                .getBounds()
                                .width())){

                    // ==========================================
                    // SENHA ESTÁ ESCONDIDA
                    // ==========================================

                    if(edtSenha
                            .getTransformationMethod()
                            instanceof PasswordTransformationMethod){

                        // Mostra senha
                        edtSenha.setTransformationMethod(
                                HideReturnsTransformationMethod
                                        .getInstance());

                    } else {

                        // ==========================================
                        // ESCONDE SENHA
                        // ==========================================

                        edtSenha.setTransformationMethod(
                                PasswordTransformationMethod
                                        .getInstance());
                    }

                    // Mantém cursor no final
                    edtSenha.setSelection(
                            edtSenha.getText().length());

                    return true;
                }
            }

            return false;
        });

        // ==========================================
        // EVENTO BOTÃO LOGIN
        // ==========================================

        btnLogin.setOnClickListener(v -> {

            // ==========================================
            // PEGA DADOS DIGITADOS
            // ==========================================

            // Email digitado
            String email =
                    edtEmail.getText()
                            .toString()
                            .trim();

            // Senha digitada
            String senha =
                    edtSenha.getText()
                            .toString()
                            .trim();

            // ==========================================
            // VERIFICA CAMPOS VAZIOS
            // ==========================================

            if(email.isEmpty()
                    || senha.isEmpty()){

                // Mensagem erro
                Toast.makeText(
                        this,
                        "Preencha todos os campos",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // ==========================================
            // LOGIN FIREBASE
            // ==========================================

            auth.signInWithEmailAndPassword(
                            email,
                            senha)

                    .addOnCompleteListener(task -> {

                        // ==========================================
                        // LOGIN FUNCIONOU
                        // ==========================================

                        if(task.isSuccessful()){

                            // Mensagem sucesso
                            Toast.makeText(
                                    this,
                                    "Login realizado com sucesso!",
                                    Toast.LENGTH_SHORT).show();

                            // ==========================================
                            // ABRE TELA PRINCIPAL
                            // ==========================================

                            startActivity(
                                    new Intent(
                                            this,
                                            MainActivity.class));

                            // Fecha tela login
                            finish();

                        } else {

                            // ==========================================
                            // ERRO LOGIN
                            // ==========================================

                            Toast.makeText(
                                    this,
                                    "Email ou senha inválidos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // ==========================================
        // EVENTO ABRIR CADASTRO
        // ==========================================

        txtCadastro.setOnClickListener(v -> {

            // Vai para tela cadastro
            startActivity(
                    new Intent(
                            this,
                            CadastroActivity.class));
        });
    }
}