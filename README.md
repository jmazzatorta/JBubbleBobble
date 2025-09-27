# Java Platform Game Engine (Bubble Bobble Clone)

Questo progetto è un gioco platform 2D completo, scritto in Java, che si ispira al classico arcade "*Bubble Bobble*".
È stato sviluppato seguendo una rigorosa architettura Model-View-Controller (MVC) per garantire una netta separazione tra la logica di gioco, la gestione dell'input e la resa grafica.

## 📜 Descrizione

Il gioco ricrea le meccaniche fondamentali del titolo originale: i giocatori controllano dei draghetti che sparano bolle per intrappolare i nemici. Una volta intrappolati, i nemici possono essere sconfitti toccando la bolla, trasformandoli in oggetti collezionabili che aumentano il punteggio. Il gioco supporta sia la modalità a giocatore singolo che quella per due giocatori.

L'obiettivo principale di questo progetto è dimostrare una solida implementazione dei pattern di progettazione software in un contesto di sviluppo di videogiochi, con un focus particolare sulla manutenibilità e sull'estensibilità del codice.


## 🏛️ Architettura del Software: Model-View-Controller (MVC)

Il cuore del progetto è la sua architettura MVC, che disaccoppia i componenti principali del gioco:

### Model: Contiene tutta la logica di gioco, lo stato delle entità (giocatori, nemici, bolle) e le regole. È rappresentato dal package model e non ha alcuna conoscenza diretta di come questi dati verranno visualizzati.

  GameModel è la classe centrale che orchestra tutti i "manager".

  I vari Manager (es. PlayersManager, EnemiesManager, ItemsManager) gestiscono specifici aspetti del gioco, come la creazione di entità, l'aggiornamento del loro stato e le collisioni.

### View: Si occupa esclusivamente della rappresentazione grafica dello stato del Model. È contenuta nel package view.

  La View osserva il Model tramite interfacce (EntityListener, GameStateListener) e si aggiorna di conseguenza quando lo stato del gioco cambia, senza mai modificarlo direttamente.

  Classi come PlayersView e EnemiesView sono responsabili del rendering specifico dei diversi tipi di entità, caricando gli sprite e gestendo le animazioni.

### Controller: Interpreta l'input dell'utente (tastiera) e comunica al Model le azioni da intraprendere. È implementato nel package controller.

  L'InputsManager riceve gli eventi dalla tastiera e li delega a gestori specifici per ogni stato del gioco (es. GameInputs per quando si gioca, StatsInputs per i menu).

Questo design rende il codice più pulito, più facile da debuggare e permette di modificare o sostituire la View (ad esempio, passando da Java Swing a un'altra libreria grafica) senza dover toccare la logica di gioco.

## ✨ Caratteristiche Principali (Features)

Gameplay Classico: Movimento su piattaforme, salto e sparo di bolle.

Modalità a 1 o 2 Giocatori: Gioca da solo o in cooperativa con un amico.

Sistema di Livelli: Progredisci attraverso livelli caricati da file di mappa esterni (/maps/levelX.txt).

Diversi Tipi di Nemici: Affronta nemici con comportamenti unici (Zenchan, Monsta, Maita), gestiti da un servizio di Intelligenza Artificiale (EnemyAIService) che determina i loro movimenti e attacchi.

Power-up e Oggetti: Raccogli una vasta gamma di oggetti per aumentare il punteggio e caramelle per potenziare le bolle.

Bolle Speciali: Crea bolle con effetti elementali come fuoco e fulmini.

Salvataggio Punteggi: Il gioco salva automaticamente il punteggio più alto e i profili utente creati (users.dat, highestscore.txt).

Gestione Stati di Gioco: Un automa a stati finiti (StateManager) gestisce le diverse fasi del gioco (Menu, Partita, Game Over, Inserimento Nome).

## 🛠️ Tecnologie Utilizzate

    Linguaggio: Java 21

    Libreria Grafica: Java Swing per la creazione della finestra e la gestione del rendering.

    Gestione Audio: API Java standard per la riproduzione di effetti sonori e musica.

## 🚀 Come Eseguire il Progetto (Getting Started)

    Per compilare ed eseguire il progetto, è necessario avere un JDK (Java Development Kit) installato.

    Non sono richieste dipendenze esterne oltre al JDK standard.

## 📁 Struttura del Progetto

Una panoramica dei package più importanti:

    src/main/java/

        constants: Contiene le costanti globali del gioco (dimensioni dello schermo, FPS, etc.).

        controller: Gestisce l'input dell'utente.

        model: Contiene tutta la logica e lo stato del gioco.

            ai: Intelligenza artificiale dei nemici.

            entities: Definizioni per giocatori, nemici, etc.

            items: Logica per gli oggetti collezionabili.

            managers: Classi che gestiscono le diverse parti del modello.

            states: Gestore degli stati di gioco.

        view: Si occupa di tutto ciò che è visuale.

            objectsview: Classi dedicate al rendering delle entità.

            panels: JPanel per i diversi schermi (gioco, menu, etc.).

            utils: Utility per caricamento immagini, font, etc.

    src/main/resources/

        audio/: File audio (musica ed effetti).

        avatars/, enemies/, player/, etc.: Sprite per le entità.

        maps/: File di testo che definiscono la struttura dei livelli.
