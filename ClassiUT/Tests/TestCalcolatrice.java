          import org.junit.Before;
          import org.junit.After;
          import org.junit.BeforeClass;
          import org.junit.AfterClass;
          import org.junit.Test;
          import static org.junit.Assert.assertEquals;
          
          public class NomeClasseTest {
	      private static Calcolatrice calcolatrice;
              @BeforeClass
              public static void setUpClass() {
                  // Eseguito una volta prima dell'inizio dei test nella classe
                  // Inizializza risorse condivise o esegui altre operazioni di setup
              }
          
              @AfterClass
              public static void tearDownClass() {
                  // Eseguito una volta alla fine di tutti i test nella classe
                  // Effettua la pulizia delle risorse condivise o esegui altre operazioni di teardown
              }
          
              @Before
              public void setUp() {
                  // Eseguito prima di ogni metodo di test
                  // Preparazione dei dati di input specifici per il test
       		  calcolatrice = new Calcolatrice(); // Creazione di un'istanza di Calcolatrice

              }
          
              @After
              public void tearDown() {
                  // Eseguito dopo ogni metodo di test
                  // Pulizia delle risorse o ripristino dello stato iniziale
              }
          
              @Test
              public void testMetodo() {
       		 int risultato = calcolatrice.divide(6, 3); // Chiamata al metodo su un'istanza
       		 // Verifica l'output o il comportamento atteso
       		 assertEquals(2, risultato);

              }          
              // Aggiungi altri metodi di test se necessario
          }
        