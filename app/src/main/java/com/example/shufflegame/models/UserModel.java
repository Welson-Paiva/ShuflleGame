package com.example.shufflegame.models;
// Modelo do usuário
public class UserModel { //O Firebase precisa disso para converter JSON em objeto

    // Nome do jogador
    public String nome;

    // Email do jogador
    public String email;

    // Avatar escolhido
    public String avatar;

    // Melhor tempo
    public int melhorTempo;

    // Construtor vazio obrigatório Firebase
    public UserModel() {
    }

    // Construtor completo
    public UserModel(String nome,
                     String email,
                     String avatar,
                     int melhorTempo) {

        this.nome = nome;
        this.email = email;
        this.avatar = avatar;
        this.melhorTempo = melhorTempo;
    }
}
