# GUIDA DEFINITIVA ALL'INSTALLAZIONE E ALL'ESECUZIONE
# Gruppo T10-G37:
 Alberto Aimone 	M63001508
 Valerio Di Domenico 	M63001465
 Genny Fedele 		M63001422

## PASSO 1
NOTA: Questa procedura è specifica per sistemi Windows, per sistemi MacOs e Linux, si rimanda alla documentazione ufficiale per l'installazione di docker desktop( si allegano i link  
MAC: https://docs.docker.com/desktop/install/mac-install/   
Linux: https://docs.docker.com/desktop/install/linux-install/ )  
Installazione Docker Desktop: se già installato correttamente sulla macchina passare al Passo 2   
NOTA: sebbene il Windows Subsystem for Linux wsl2, una funzionalità che consente di eseguire un ambiente Linux all'interno del sistema operativo Windows garantendo la compatibilità tra Docker Desktop e Windows, normalmente venga installato e aggiornato durante l'installazione di Docker Desktop, vi sono casi in cui questo step non venga effettuato correttamente in maniera automatica (all'apertura di Docker è presente un messaggio di errore "WSL Error"), bisogna quindi installare manualmente wsl tramite gli step preliminari 0:
<pre>
0a) avviare il prompt dei comandi
0b) digitare wsl --install e premere invio
0c) digitare wsl --update e premere invio
0d) riavviare la macchina
</pre>
1) recarsi alla pagina web ufficiale: https://www.docker.com/products/docker-desktop/
2) cliccare su Download per il proprio sistema operativo (ad es. Download for Windows) per scaricare il setup eseguibile
3) lanciare l'eseguibile e procedere all'installazione, è possibile completare l'installazione con le impostazione di default, anche senza effettuare la registrazione di un account e senza effettuare il login
4) riavviare la macchina come richiesto al termine dell'installazione
5) all'avvio di Docker Desktop nella sezione Settings -> General controllare che sia spuntata l'opzione "Use the WSL 2 based engine"

## PASSO 2
Installazione dell'applicazione su Docker:

1) avviare Docker Desktop
2) A. avviare lo script "installer.bat" eseguendo il file con doppio click (consentendo esclusioni al firewall se richieste). Saranno effettuate automaticamente le seguenti operazioni:  
   B. Se si utilizza un dispositivo con una distrubuzione Linux o MacOs, avvviare lo script "installazione_mac_linux.sh" anzichè "installer.bat". Dopodichè seguire il resto dei passi.  

<pre>
a) creazione della rete "global-network" comune a tutti i container.
b) creazione del volume "VolumeT9" comune ai Task 1 e 9; creazione del volume "VolumeT8" comune ai Task 1 e 8
c) installazione di ogni singolo container.
</pre>
NOTA: il container relativo al Task 9 ("Progetto-SAD-G19-master") si sospenderà autonomamente dopo l'avvio. Esso viene utilizzato solo per "popolare" il volume "VolumeT9" condiviso con il Task 1. Lo stesso vale per il container relativo al Task 8 ("Prototipo2.0"), usato per il volume "VolumeT8".

## PASSO 3
Configurazione container "manvsclass-mongo_db-1":

1) dalla sezione Containers di Docker Desktop trovare il gruppo di container "manvsclass" (relativo al Task 1) e cliccare la freccia a sinistra del nome del container per rivelare i due container mongo_db-1 e controller-1
2) cliccare su mongo_db-1
3) cliccare su exec per posizionarsi all'interno del terminale del container
4) digitare il comando "mongosh" e premere invio
5) digitare i seguenti comandi:

        use manvsclass
        db.createCollection("ClassUT");
        db.createCollection("interaction");
        db.createCollection("Admin");
        db.createCollection("Operation");
        db.ClassUT.createIndex({ difficulty: 1 })
        db.Interaction.createIndex({ name: "text", type: 1 })
        db.interaction.createIndex({ name: "text" })
        db.Admin.createIndex({username: 1})

NOTA: è possibile effettuare una copia di tutti i comandi e incollarli sull'exec, questi infatti verranno eseguiti correttamente in maniera sequenziale. Per incollare un testo sull'exec non è possibile usare la combinazione CTRL+V ma è necessario fare click del tasto destro del mouse -> Paste
NOTA: si ricordi di premere invio dopo l'ultimo comando

6) assicurarsi che siano nello stato Running tutti i container eccetto "progetto-sad-g19-master" e "prototipo2.0" che saranno correttamente nello stato Exited per quanto detto in precedenza. Nel caso vi sia qualche container non nello stato di Running è necessario selezionare il container tramite il quadratino alla sinistra del nome, dopodichè cliccare sul pulsante Start selected items rappresentato in alto a destra da un pulsante di Play.

## PASSO 4
L'intera applicazione è adesso pienamente configurata e raggiungibile sulla porta :80
Caricamento Classi da parte dell'amministratore:

NOTA: se già si è registrato precedentemente l'admin effettuare il login alla pagina http://localhost/login_Admin e saltare allo step 4

1) aprire il proprio browser e recarsi su http://localhost/registraAdmin
2) registrare i dati dell'amministratore
3) al termine della registrazioni si verrà automaticamente reindirizzati su http://localhost/home_adm
4) cliccare su + Add Class in alto
5) inserire i dati sulla classe tenendo presente che è tassativo inserire nel Class name l'effettivo nome della classe (ad es. se il file Calcolatrice.java contiene la classe Calcolatrice, in Class name andrà indicato Calcolatrice)
6) effettuare l'upload della file .java tramite il pulsante Scegli il file
NOTA: sono presenti alcuni file già pronti all'upload nella cartella ClassiUT\Tests (ad es. Calcolatrice.java)
7) cliccare upload

NOTA: a questo punto verranno generati in automatico i livelli di Evosuite e Randoop. A seconda della macchina che si utilizza e della complessità della classe caricata potrebbe essere necessario qualche secondo o minuto per permettere ad Evosuite e Randoop di completare l'esecuzione, munirsi di pazienza. È comunque possibile monitorare l'avanzamento tramite il controller del Task 1: per fare ciò è necessario recarsi su Docker nella sezione Containers, espandere il container del Task 1 "manvsclass" come fatto al Passo 3.1 e cliccare su "controller-1", qui è possibile visualizzare il log del container che riporterà l'output di avanzamento dell'upload della classe con la creazione della suite di test di Randoop ed Evosuite.

8) al termine dell'upload automaticamente si verrà reindirizzati sulla home (http://localhost/home_adm) e si visualizzerà correttamente la nuova classe caricata

## PASSO 5
Avvio della partita da parte dello studente
NOTA: se già si è registrato uno studente saltare allo step 4

1) recarsi su http://localhost/login
2) cliccare su "Non sei ancora registrato? Registrati."
3) si verrà reindirizzati sulla pagina http://localhost/register in cui sarà possibile inserire i dati di registrazione; al termine dell'inserimento cliccare su "Invia"
4) recarsi su http://localhost/login
5) inserire Username e Password inseriti in fase di registrazione e cliccare su "Accedi"
6) selezionare a sinistra una classe di test e a destra robot e livello da sfidare, dopodichè cliccare su Submit in basso a destra
7) verrà visualizzato il report di riepilogo, cliccare su Submit in basso a destra
8) si arriverà alla pagina di editor in cui è possibile usare i tool dell'editor e scrivere il codice nel riquadro in alto a sinistra o caricare un file di test tramite il pulsante Open File
NOTA: sono presenti alcuni file già pronti nella cartella ClassiUT\Tests (ad es. TestCalcolatrice.java)
      tenere a mente che i file di test compatibili sono in formato Junit4
9) è possibile cliccare il pulsante con il logo di evosuite "Run UserTest" per avviare la misura di coverage con Evosuite, oppure cliccare il pulsante J "Highlight Line" per evidenziare le righe di codice coperte con l'utilizzo del tool JaCoCo, infine è possibile cliccare sul pulsante Play di "Start Game" per terminare la partita
10) al termine dell'elaborazione apparirà un alert con l'esito della partita e nella console in basso a destra saranno visualizzate tutte le misure di coverage relative al test scritto dall'utente; la console in basso a sinistra riporterà il log della compilazione

NOTA: a seconda della macchina che si utilizza e della complessità della classe under test e della classe test caricata potrebbe essere necessario qualche secondo o minuto per permettere di completare l'esecuzione, munirsi di pazienza. È comunque possibile monitorare l'avanzamento tramite Docker: per fare ciò è necessario recarsi nella sezione Containers, espandere il container del Task 7 "remoteccc" e cliccare su "app-1", qui è possibile visualizzare il log del container che riporterà l'output di avanzamento dell'esecuzione. Allo stesso modo è possibile visualizzare i log dei container legati a questa funzionalità (T5, T6)

## FINE

### NOTE FINALI:
Si è notato che sulla macchine sulle quali viene installato l'applicazione, con sistema operativo Windows 10 e Windows 11, all'avvio di Docker, si abbia bisogno di una buona quantità di RAM a disposizione, si è notato che una quantità di RAM inferiore a 8GB potrebbe dare problemi saltuari; possono capitare casi nel quale sul browser all'indirizzo http://localhost non sia presente alcuna pagina, in questi casi è necessario agire nei seguenti modi:
1) aprire la finestra di Docker.
2) se è presente la scritta "Docker Engine Stopped" cliccare con il tasto destro l'icona di Docker (su Windows in basso a destra nell'area di notifica delle icone sulla barra delle applicazioni) e cliccare "Restart". Se anche ciò non funziona terminare l'applicazione manualmente e riavviare Docker.
3) può capitare che vengano terminati alcuni container che rimangono inattivi per un certo periodo di tempo, portandoli allo stato 'Exited', va quindi controllato il Passo 3.6

Per consigli su metologie e software di sviluppo, su come effettuare le modifiche ai vari sottosistemi si rimanda alla Documentazione completa T10-G37


## STRUTTURE CARTELLE E FILE DI RILIEVO:
NOTA: sono stati evidenziati solo gli elementi di particolare interesse
<pre>
\---VolumeT8\app\FolderTree
    \---NomeClasse			//Nome della cartella associata alla classe aggiunta
    	+---NomeClasseSourceCode
    	|   \---NomeClasse.java
    	|
     	\---RobotTest
            \---EvoSuiteTest
          	+---01Level
                |   +---TestReport
    	   	|   |  	\---statistics.csv
                |   \---TestSourceCode
          	|	\---evosuite-tests				//All'interno della cartella evosuite-tests saranno presenti anche i file .class
          	|	    +---NomeClasse_ESTest_scaffolding.java	
          	|	    \---NomeClasse_ESTest.java
          	\---xxLevel
                    +---TestReport
    	   	    |	\---statistics.csv
                    \---TestSourceCode
          		\---evosuite-tests
          		    +---NomeClasse_ESTest_scaffolding.java
          		    \---NomeClasse_ESTest.java

-----------------------------------------------------------------------------------------------------------------------------------------------------------

\---VolumeT9\app\FolderTree		 riassuntivo di quanto scritto qui
    \---NomeClasse			//Nome della cartella associata alla classe aggiunta
    	+---NomeClasseSourceCode
    	|   \---NomeClasse.java
    	|
     	\---RobotTest
            \---RandoopTest
          	+---01Level
    	   	|   +---coveragetot.xml
          	|   +---RegressionTest_it0_livello1.java
		|   \---RegressionTestX_itY_livelloZ.java
          	\---xxLevel
    	   	    +---coveragetot.xml
		    \---RegressionTestX_itY_livelloZ.java

-----------------------------------------------------------------------------------------------------------------------------------------------------------

\---remoteccc-app-1\ClientProject
     +---src
     |	+---main
     |	|   \---java
     |	|	\---ClientProject
     |	|	    \NomeClasse.java	//Nome del file associato alla classe aggiunta
     |	\---test
     |	    \---java
     |		\---ClientProject
     |		    \NomeClasseTest.java
     +---evosuite-report
     |	\---statistics.csv		//Report dei punteggi utente
     +---target			
     |	+---classes
     |	|   \---ClientProject
     |	|	\---NomeClasse.class
     |	\---test-classes
     |	    \---ClientProject
     |		\---NomeClasseTest.class
     \---site
	 \---jacoco
	     \---jacoco.xml
</pre>
