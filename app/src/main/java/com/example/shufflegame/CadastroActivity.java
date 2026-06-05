package com.example.shufflegame;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.shufflegame.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CadastroActivity extends AppCompatActivity {

    // Campos
    EditText edtNome;
    EditText edtEmailCadastro;
    EditText edtSenhaCadastro;

    // Botão
    Button btnCadastrar;

    // Cards
    CardView cardAvatar1;
    CardView cardAvatar2;
    CardView cardAvatar3;
    CardView cardAvatar4;

    // Bordas
    View bordaAvatar1;
    View bordaAvatar2;
    View bordaAvatar3;
    View bordaAvatar4;

    // Avatar selecionado
    String avatarSelecionado = "avatar1";

    // Firebase
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);

        // Campos
        edtNome = findViewById(R.id.edtNome);
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);

        // Botão
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Cards
        cardAvatar1 = findViewById(R.id.cardAvatar1);
        cardAvatar2 = findViewById(R.id.cardAvatar2);
        cardAvatar3 = findViewById(R.id.cardAvatar3);
        cardAvatar4 = findViewById(R.id.cardAvatar4);

        // Bordas
        bordaAvatar1 = findViewById(R.id.bordaAvatar1);
        bordaAvatar2 = findViewById(R.id.bordaAvatar2);
        bordaAvatar3 = findViewById(R.id.bordaAvatar3);
        bordaAvatar4 = findViewById(R.id.bordaAvatar4);

        // Firebase
        auth = FirebaseAuth.getInstance();

        databaseReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference("usuarios");

        // Mostrar senha
        edtSenhaCadastro.setOnTouchListener((v, event) -> {

            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP){

                if(event.getRawX() >=
                        (edtSenhaCadastro.getRight()
                                - edtSenhaCadastro
                                .getCompoundDrawables()[DRAWABLE_RIGHT]
                                .getBounds()
                                .width())){

                    if(edtSenhaCadastro
                            .getTransformationMethod()
                            instanceof PasswordTransformationMethod){

                        edtSenhaCadastro
                                .setTransformationMethod(
                                        HideReturnsTransformationMethod
                                                .getInstance());

                    } else {

                        edtSenhaCadastro
                                .setTransformationMethod(
                                        PasswordTransformationMethod
                                                .getInstance());
                    }

                    edtSenhaCadastro.setSelection(
                            edtSenhaCadastro.getText().length());

                    return true;
                }
            }

            return false;
        });

        // Avatar 1
        cardAvatar1.setOnClickListener(v -> {

            avatarSelecionado = "avatar1";

            atualizarBordas();
        });

        // Avatar 2
        cardAvatar2.setOnClickListener(v -> {

            avatarSelecionado = "avatar2";

            atualizarBordas();
        });

        // Avatar 3
        cardAvatar3.setOnClickListener(v -> {

            avatarSelecionado = "avatar3";

            atualizarBordas();
        });

        // Avatar 4
        cardAvatar4.setOnClickListener(v -> {

            avatarSelecionado = "avatar4";

            atualizarBordas();
        });

        // Atualiza inicial
        atualizarBordas();

        // Botão cadastrar
        btnCadastrar.setOnClickListener(v -> {

            String nome =
                    edtNome.getText()
                            .toString()
                            .trim();

            String email =
                    edtEmailCadastro.getText()
                            .toString()
                            .trim();

            String senha =
                    edtSenhaCadastro.getText()
                            .toString()
                            .trim();

            // Verifica campos
            if(nome.isEmpty()
                    || email.isEmpty()
                    || senha.isEmpty()){

                Toast.makeText(
                        CadastroActivity.this,
                        "Preencha todos os campos",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Senha mínima
            if(senha.length() < 6){

                Toast.makeText(
                        CadastroActivity.this,
                        "Senha precisa ter no mínimo 6 caracteres",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Verifica nome duplicado
            databaseReference
                    .orderByChild("nome")
                    .equalTo(nome)
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    if(snapshot.exists()){

                                        Toast.makeText(
                                                CadastroActivity.this,
                                                "Nome já está em uso",
                                                Toast.LENGTH_SHORT).show();

                                    } else {

                                        auth.createUserWithEmailAndPassword(
                                                        email,
                                                        senha)

                                                .addOnCompleteListener(task -> {

                                                    if(task.isSuccessful()){

                                                        String uid =
                                                                auth.getCurrentUser()
                                                                        .getUid();

                                                        UserModel usuario =
                                                                new UserModel(
                                                                        nome,
                                                                        email,
                                                                        avatarSelecionado,
                                                                        0
                                                                );

                                                        databaseReference
                                                                .child(uid)
                                                                .setValue(usuario);

                                                        Toast.makeText(
                                                                CadastroActivity.this,
                                                                "Conta criada com sucesso!",
                                                                Toast.LENGTH_SHORT).show();

                                                        finish();

                                                    } else {

                                                        Toast.makeText(
                                                                CadastroActivity.this,
                                                                "Erro ao cadastrar",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                    Toast.makeText(
                                            CadastroActivity.this,
                                            "Erro no banco de dados",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
        });
    }

    // Atualiza bordas
    private void atualizarBordas(){

        // Remove todas
        bordaAvatar1.setVisibility(View.GONE);
        bordaAvatar2.setVisibility(View.GONE);
        bordaAvatar3.setVisibility(View.GONE);
        bordaAvatar4.setVisibility(View.GONE);

        // Mostra selecionado
        switch (avatarSelecionado){

            case "avatar1":
                bordaAvatar1.setVisibility(View.VISIBLE);
                break;

            case "avatar2":
                bordaAvatar2.setVisibility(View.VISIBLE);
                break;

            case "avatar3":
                bordaAvatar3.setVisibility(View.VISIBLE);
                break;

            case "avatar4":
                bordaAvatar4.setVisibility(View.VISIBLE);
                break;
        }
    }
}