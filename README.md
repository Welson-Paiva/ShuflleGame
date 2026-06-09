[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white

# 🎮 Shuffle Game
![java][JAVA_BADGE]
[![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=flat&logo=android-studio&logoColor=white)](https://developer.android.com/studio)   

Um jogo de puzzle numérico para Android com autenticação, múltiplos níveis de dificuldade e ranking global — desenvolvido com Java e Firebase.

---

## 📱 Sobre o Projeto

O **Shuffle Game** é um clássico jogo de embaralhamento de peças (sliding puzzle) onde o jogador deve organizar os números em ordem crescente dentro de um tabuleiro antes que o tempo acabe. O app conta com sistema de login, perfil com avatar e um ranking global entre todos os jogadores.

---

## ✨ Funcionalidades

- 🔐 **Login e Cadastro** com Firebase Authentication
- 🧑‍🎨 **Escolha de avatar** no momento do cadastro (4 opções)
- 🏠 **Menu principal** com exibição do perfil e melhor tempo do jogador
- 🎯 **3 níveis de dificuldade:**
  - Fácil — tabuleiro 3×3, 90 segundos
  - Médio — tabuleiro 4×4, 120 segundos
  - Difícil — tabuleiro 5×5, 240 segundos
- ⏱️ **Temporizador** com contador regressivo
- 🔊 **Efeitos sonoros** (clique, movimento de peça, vitória, game over)
- 🏆 **Ranking global** com os melhores tempos de todos os jogadores
- 💾 **Salvamento automático** do melhor tempo no Firebase Realtime Database
- 🚪 **Botão de desistência** com confirmação antes de abandonar a partida

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Uso |
|---|---|
| Java | Linguagem principal |
| Android SDK | Desenvolvimento mobile (minSdk 24 / targetSdk 36) |
| Firebase Authentication | Login e cadastro de usuários |
| Firebase Realtime Database | Armazenamento de perfis e recordes |
| SoundPool | Efeitos sonoros |
| CountDownTimer | Temporizador do jogo |
| GridLayout | Tabuleiro dinâmico |
| Material Design | Componentes visuais |

---

## 🏗️ Estrutura do Projeto

```
app/src/main/java/com/example/shufflegame/
│
├── LoginActivity.java       # Tela de login com Firebase Auth
├── CadastroActivity.java    # Tela de cadastro com seleção de avatar
├── MainActivity.java        # Menu principal, perfil e ranking
├── GameActivity.java        # Lógica do jogo (tabuleiro, timer, vitória)
│
└── models/
    └── UserModel.java       # Modelo do usuário para o Firebase
```

---

## 🚀 Como Executar

### Pré-requisitos

- Android Studio instalado
- JDK 11 ou superior
- Conta no [Firebase Console](https://console.firebase.google.com/)

### Configuração

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/Welson-Paiva/ShuflleGame.git
   ```

2. **Abra no Android Studio:**
   - File → Open → selecione a pasta do projeto

3. **Configure o Firebase:**
   - Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
   - Ative **Authentication** (Email/Senha) e **Realtime Database**
   - Baixe o arquivo `google-services.json` e substitua o existente em `app/`

4. **Execute o projeto:**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em **Run ▶** no Android Studio

---

## 🎲 Como Jogar

1. Faça login ou crie uma conta
2. No menu principal, toque em **Jogar** e escolha a dificuldade
3. As peças aparecem embaralhadas — toque em uma peça adjacente ao espaço vazio para movê-la
4. Organize os números em ordem crescente (da esquerda para direita, de cima para baixo) antes que o tempo acabe
5. Ao vencer, seu tempo restante é salvo como recorde caso seja melhor que o anterior

---

## 🏆 Sistema de Ranking

O ranking exibe todos os jogadores ordenados pelo maior tempo restante ao vencer (quanto mais tempo sobrar, melhor a posição). Os três primeiros colocados recebem destaque em **ouro**, **prata** e **bronze**.

---

## 📋 Requisitos do Sistema

- Android 7.0 (API 24) ou superior
- Conexão com a internet (necessária para login e ranking)
