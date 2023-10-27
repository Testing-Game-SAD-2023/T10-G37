package RemoteCCC.App;


import RemoteCCC.Config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;



@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ServletComponentScan
@RestController
public class App {


    public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

    	public static String LineCoverageCSV(String path) {
		try {
			// Creare un oggetto CSVReader
			CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1).build();
	
			// Leggere le righe del file CSV
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				// Verificare se la riga ha il formato desiderato
				if (nextLine[1].equals("LINE")) {

					// Estrai e converti il valore in double, moltiplica per 100 e converte in stringa
					double coverageDouble = Double.parseDouble(nextLine[2]) * 100;
                    int coverage= (int) coverageDouble;
                    String cov= String.valueOf(coverage);
					return  cov;
				}
			}
	
			// Chiudere il reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		// Restituire un valore predefinito se il valore non è stato trovato
		return "Errore csvCoverage"; 
	}
    


       /**
     * REST endpoint for handling POST requests with JSON body containing two Java files.
     * Compiles the two files, runs the test file on the compiled first file, and measures test coverage with Jacoco.
     * @param request The JSON request containing the two Java files.
     * @return A JSON response containing the test coverage results.
     * @throws IOException If there is an I/O error reading or writing files.
     * @throws InterruptedException
     */
    @PostMapping(value = "/compile-and-codecoverage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseDTO compileAndTest(@RequestBody RequestDTO request) throws IOException, InterruptedException {
        
        String testingClassName   = request.getTestingClassName();
        String testingClassCode   = request.getTestingClassCode();

        String underTestClassName = request.getUnderTestClassName();
        String underTestClassCode = request.getUnderTestClassCode();
        
        // Salvataggio dei due file su disco: occorre specificare il nome della classe, per la corretta compilazione
        saveCodeToFile(testingClassName, testingClassCode, Config.getTestingClassPath());
        System.out.println("Codice correttamente salvato " + Config.getUnderTestClassPath());
        saveCodeToFile(underTestClassName, underTestClassCode, Config.getUnderTestClassPath());

        //Output di ritorno del comando maven.
        String []output_maven={""};

        ResponseDTO response = new ResponseDTO();

        

        
        if(compileExecuteCovarageWithMaven(output_maven,request)){
            //Salvo il path in cui verrà inserito il file csv
            Path path = Paths.get(Config.getCoverageFolder());
            //Estraggo il valore della copertura per linee tramite la funzione LineCoverageCSV
            String coverage=LineCoverageCSV(Config.getCoverageFolder());
            try {
                Files.delete(path);
                System.out.println("Il file è stato eliminato con successo.");
            } catch (IOException e) {
                System.err.println("Impossibile eliminare il file: " + e.getMessage());
            }
            //Costruisco la risposta con i valori ottenuti
            response.setError(false);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(coverage);

        }else
        {
            //Costruisco la risposta di errore
            response.setError(true);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(null);            
        }
        deleteFile(underTestClassName, testingClassName);
        return response;
    }


    @PostMapping(value = "/highlightline", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseDTO highlight(@RequestBody RequestDTO request) throws IOException, InterruptedException {
        
        String testingClassName   = request.getTestingClassName();
        String testingClassCode   = request.getTestingClassCode();

        String underTestClassName = request.getUnderTestClassName();
        String underTestClassCode = request.getUnderTestClassCode();
        
        // Salvataggio dei due file su disco: occorre specificare il nome della classe, per la corretta compilazione
        saveCodeToFile(testingClassName, testingClassCode, Config.getTestingClassPath());
        saveCodeToFile(underTestClassName, underTestClassCode, Config.getUnderTestClassPath());        
        //Aggiunge la dichiarazione del package ai file java ricevuti.
        //addPackageDeclaration(firstFilePath, secondFilePath);

        //Output di ritorno del comando maven.
        String []output_maven={""};

        ResponseDTO response = new ResponseDTO();        
        if(highlight(output_maven)){
            String retXmlJacoco = readFileToString(Config.getxmlFolder());//zipSiteFolderToJSON(Config.getzipSiteFolderJSON()).toString();
            response.setError(false);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(retXmlJacoco);

        }else
        {
            response.setError(true);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(null);            
        }
        deleteFile(underTestClassName, testingClassName);
        return response;
    }


 
    private static String readFileToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String contents = new String(bytes);
        return contents;
    }

 

    
    private void deleteFile(String underTestClassName, String testingClassName)throws IOException
    {
        File file1 = new File(Config.getUnderTestClassPath()+underTestClassName);
        file1.delete();
        File file2 = new File(Config.getTestingClassPath() + testingClassName);
        file2.delete();
    }

private static boolean compileExecuteCovarageWithMaven(String []ret, RequestDTO request) throws IOException, InterruptedException {
    
        String nome_test=request.getTestingClassName();
        String nome_classe=request.getUnderTestClassName();
        String nome_t = nome_test.replace(".java", "");
        String nome_c = nome_classe.replace(".java", "");        
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "codice.sh", nome_c, nome_t);
        processBuilder.directory(new File(Config.getpathCompiler())); // Imposta la directory di lavoro corretta
        
        // Reindirizza l'output di errore standard (stderr) del processo
        processBuilder.redirectErrorStream(true);        
        Process process = processBuilder.start();        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            ret[0] += line += "\n";
            // Stampa l'output in modo da poter vedere l'errore
            System.err.println(line);
        }
        
        int exitCode = process.waitFor();
        
        if (exitCode == 0) {
            System.out.println("Maven clean compile executed successfully.");
            return true;
        } else {
            System.out.println("Error executing Maven clean compile.");
            return false;
        }
        

    }


    private static boolean highlight(String []ret) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("mvn", "clean", "compile", "test");
        processBuilder.directory(new File(Config.getpathCompiler()));
    
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            ret[0] += line += "\n";
        int exitCode = process.waitFor();
       
        // Legge il contenuto del buffer del terminale
        // InputStream inputStream = process.getInputStream();
        // byte[] buffer = new byte[inputStream.available()];
        // inputStream.read(buffer);
        // ret[0] = new String(buffer, StandardCharsets.UTF_8);
        if (exitCode == 0) {
            System.out.println("Maven clean compile executed successfully.");
            return true;
        } else {
            System.out.println("Error executing Maven clean compile.");
            return false;
        }

    }


   
    /**
     * Metodo per salvare un file ".java"
     * @param nameclass Nome della classe da salvare.
     * @param code      Codice java da salvare.
     * @param path      Stringa che descrive il path dove slavare il file.
     * @return Un oggetto Path che localizza il file salvato.
     * @throws IOException Se ci sono errori I/O di lettura o scrittura su file.
     */
    private Path saveCodeToFile(String nameclass, String code, String path) throws IOException {
        String packageDeclaration = Config.getpackageDeclaretion();
        code = packageDeclaration + code;
        File tempFile = new File(path + nameclass);
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(code);
            System.out.println("File creato correttamente ");
        }
        catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file: " + e.getMessage());
        }
        
        return tempFile.toPath();
    }
   


    //Data Transfer Object
    private static class RequestDTO {

        private String testingClassName;
        private String testingClassCode;
        
        private String underTestClassName;
        private String underTestClassCode;


        public String getTestingClassName(){
            return testingClassName;
        }
        
        public String getTestingClassCode() {
            return testingClassCode;
        }


        public String getUnderTestClassName(){
            return underTestClassName;
        }

        public String getUnderTestClassCode() {
            return underTestClassCode;
        }     
        
    }

    private static class ResponseDTO{

        private Boolean error;
        private String outCompile;
        private String coverage;


        public Boolean getError(){
            return error;
        }

        public String getOutCompile(){
            return outCompile;
        }
        public String getCoverage(){
            return coverage;
        }

        public void setError(boolean error)
        {
            this.error = error;
        }
        
        public void setoutCompile(String outCompile)
        {
            this.outCompile = outCompile;
        }

        public void setCoverage(String coverage)
        {
            this.coverage = coverage;
        }


    }


}
