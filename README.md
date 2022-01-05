# Intelligenza Artificiale e Rappresentazione della Conoscenza, Progetto a.a. 2021/22:

**Professori**: _Luigi Palopoli_, _Simona Nisticò_

**Candidati** del gruppo _"crAIzy"_:
* Danny Di Cello 
* Raffaele Miriello 
* Giuseppe Seminara 
* Francesco Palumbo 

## 1. Introduzione
<img align="right" width="150" height="150" src="https://i.postimg.cc/PxdRpCsk/init-config.jpg" alt="FIG 1">

Fission è un gioco da tavolo per il quale occorre una scacchiera 8x8, 12 pedine bianche e 12 pedine nere, all’interno del gioco si possono avere solo pedine singole.
* All’inizio di ciascuna partita le pedine vengono posizionate come indicato nella figura a lato.
* La partita comincia con una mossa del giocatore bianco.
* Lo scopo del gioco è quello di eliminare tutte le pedine dell’avversario.

## 2. Mosse Valide
<img align="right" width="150" height="150" src="https://i.postimg.cc/k5VJTJKn/f2.jpg" alt="FIG 2">

I giocatori, a turno, muovono una delle loro pedine, le possibili direzioni di mossa comprendono tutte le direzioni orizzontali, verticali ed oblique come mostrato nella figura a lato. Indipendentemente da quale sia la direzione della mossa la pedina avanzerà fino a quando non incontrerà un bordo oppure un’altra pedina, a seconda di quale sia la motivazione di arresto si verificheranno una delle seguenti condizioni:
* Nel caso in cui la pedina si sia arrestata perché ha incontrato un muro, non verrà effettuata alcuna modifica della configurazione eccetto per quelle avvenute a seguito del movimento.
* Nel caso in cui la pedina si sia arrestata perché ha incontrato
una pedina allora verranno eliminate dalla scacchiera la pedina mossa, quella contro cui si è
“scontrata” e tutte le pedine adiacenti ad essa (nella figura sottostante il riquadro chiaro indica l’area di adiacenza per la mossa considerata) viene evidenziato cosa si intenda per pedine adiacenti); questo a prescindere dal loro colore. Le pedine eliminate lo saranno definitivamente e quindi non rientreranno più nel gioco.
<p align="center">
  <img height="250" src="https://i.postimg.cc/SNTbH0yj/f3.jpg" />
</p>

<img align="right" width="150" height="150" src="https://i.postimg.cc/sxG5ydky/f5.jpg" alt="FIG 4">
Durante l’evoluzione del gioco, potrebbero non essere disponibili tutte le direzioni della mossa in quanto per una pedina è possibile muoversi in una determinata direzione solo se è presente almeno uno spazio libero tra la pedina e la cella in cui essa si arresterà. Nella figura a lato è riportato un esempio in cui vengono mostrate quale siano le mosse valide per la pedina bianca posizionata centralmente; nell’esempio riportato le direzioni nord, est e sud-ovest non sono disponibili perché sono presenti le pedine adiacenti e quindi non è presente almeno uno spazio libero.

## 3. Configurazioni Finali
Il gioco si conclude quando uno dei due giocatori non ha più pedine sulla scacchiera, in questo caso la vittoria verrà assegnata al giocatore a cui appartengono le pedine rimanenti.
Il gioco prevede il pareggio ed in particolare sono tre le condizioni in cui esso si verifica:
1. Entrambi i giocatori rimangono con una sola pedina.
2. Il giocatore che deve effettuare la mossa non ha mosse disponibili.
3. La scacchiera rimane vuota.

Per ulteriori informazioni sul gioco consultare: http://www.di.fc.ul.pt/~jpn/gv/fission.htm

## 4. Protocollo di comunicazione
_Messaggi dal Server al Client_:

| **Message** | **Description** |
|---|---|
| ***WELCOME \<colour\>*** | Il server comunica al client il colore che gli è stato assegnato |
| ***MESSAGE \<message\>*** | Il server comunica al client qualcosa |
| ***OPPONENT_MOVE \<move\>*** | Il server comunica al client l'ultima mossa dell'avversario |
| ***YOUR_TURN*** | Il server comunica al client che è il suo turno |
| ***VALID_MOVE*** | Il server conferma al client che la sua ultima mossa è valida |
| ***ILLEGAL_MOVE*** | Il server informa il client che la sua ultima mossa non è valida (l'avversario vince) |
| ***TIMEOUT*** | Il server informa il client che il tempo per comunicare la mossa è scaduto |
| ***VICTORY*** | Il server informa il client che ha vinto la partita |
| ***TIE*** | Il server informa il client che la partita è finita in parità |
| ***DEFEAT*** | Il server informa il client che ha perso la partita |

_Messaggi dal Client al Server_:

| **Message** | **Description** |
|---|---|
| ***MOVE \<move\>*** | Il client comunica la mossa |

Dove:
- ***\<colour\>: Black/White***  
```        È una stringa che rappresenta il colore associato al giocatore; è assegnata dal server e comunicata al client insieme al messaggio WELCOME. Il bianco muove per primo.```
- ***\<move\> := \<start\>,\<dir\>***  
```        La mossa è codificata tramite due stringhe separate da virgola che indicano rispettivamente: la cella di partenza e la direzione della mossa.```
- ***\<start\>***  
```        Ogni cella è univocamente determinata da una lettera e un numero. Si è deciso di etichettare le righe con le lettere da A a G e le colonne con i numeri da 1 a 8 (vedi figura seguente), perciò, un giocatore che voglia muovere una pedina posta in F5 invierà al server la stringa “F5”.```

<p align="center">
  <img src="https://i.postimg.cc/fRnpvkc5/tb.jpg" />
</p>

- ***\<dir\>***  
```      La direzione della mossa è fornita specificando la coordinata geografica verso cui si desidera muovere la propria pedina:```  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**N**: nord <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**NE**: nord-est <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**E**: est <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**SE**: sud-est <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**SW**: sud-ovest <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**W**: ovest <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**NW**: nord-ovest <br>

**Segue un esempio di comunicazione tra il server e il giocatore bianco**
<p align="center">
  <img src="https://i.postimg.cc/RVZsvKMW/tb2.jpg" />
</p>

***ALTRI DETTAGLI***
* Il torneo è all’italiana e prevede quindi un girone di andata ed un girone di ritorno; durante ciascuna partita per la vittoria vengono assegnati 3 punti, per la sconfitta vengono assegnati 0 punti, in caso di pareggio viene assegnato 1 punto a ciascuno dei due giocatori.
* Il tempo di mossa è fissato in un secondo (tempo rilevato al server che intercorre dal momento in cui il server invia il messaggio YOUR_TURN al momento in cui lo stesso server riceve la
nuova mossa dal giocatore coinvolto). Qualora il server non ricevesse la mossa dal giocatore entro il tempo previsto, tale giocatore sarebbe dichiarato sconfitto e la partita conclusa.
* All’inizio del gioco viene garantito un tempo pari a 30 secondi prima che il server invii il primo messaggio YOUR_TURN al giocatore che apre la partita (tempo di warm-up).
* La durata della partita è fissata in 50 mosse per ogni giocatore. Raggiunto tale limite verrà dichiarato il pareggio.
* In caso di crash del giocatore la vittoria viene assegnata all’avversario, indipendentemente da quali siano le motivazioni che lo hanno causato.
* Ogni giocatore artificiale deve poter ricevere i seguenti parametri all'avvio: indirizzo IP del server e il numero di porta del server. La calendarizzazione delle sessioni di test, di deploy e consegna del progetto nonché i formati di consegna saranno fissate in una comunicazione separata.
