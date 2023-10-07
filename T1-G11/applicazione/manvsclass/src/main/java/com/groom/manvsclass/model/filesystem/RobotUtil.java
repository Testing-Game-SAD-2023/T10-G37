package com.groom.manvsclass.model.filesystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputFilter.Config;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class RobotUtil {

	public static int LineCoverage(String path) {
		Element line = null;
		String linecoverage= null;
		try {
			
			File cov = new File(path);
			
			Document doc = Jsoup.parse(cov, null, "", Parser.xmlParser());
			line = doc.getElementsByTag("coverage").get(3);
			linecoverage = String.valueOf(line).substring(32, 35);
			
			linecoverage = linecoverage.split("%", 0)[0];
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Integer.valueOf(linecoverage) ;
	}

	public static int LineCoverageCSV(String path) {
		try {
			// Creare un oggetto CSVReader
			CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1).build();
	
			// Leggere le righe del file CSV
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				// Verificare se la riga ha il formato desiderato
				if (nextLine[1].equals("LINE")) {
					// Estrai e converti il valore in double, moltiplica per 100 e converte in intero
					double coverageDouble = Double.parseDouble(nextLine[2]) * 100;
					return (int) coverageDouble;
				}
			}
	
			// Chiudere il reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		// Restituire un valore predefinito se il valore non è stato trovato
		return -1; // Cambia il valore predefinito a seconda delle tue esigenze
	}






    public static void generateAndSaveRobots(String fileName, String cname, MultipartFile multipartFile) throws IOException {
        
		//Secondo me deve essere aggiunto a monte un controllo su qual è il robot che si attiva altrimenti viene eseguito sempre tutto
		// RANDOOP - T9			    
		Path directory = Paths.get("/VolumeT9/app/FolderTree/" + cname + "/" + cname + "SourceCode");
		
		try {
			// Verifica se la directory esiste già
			if (!Files.exists(directory)) {
				// Crea la directory
				Files.createDirectories(directory);
				System.out.println("La directory è stata creata con successo.");
			} else {
				System.out.println("La directory esiste già.");
			}
		} catch (Exception e) {
			System.out.println("Errore durante la creazione della directory: " + e.getMessage());
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = directory.resolve(fileName);
			System.out.println(filePath.toString());
			Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
		}

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("java", "-jar", "Task9-G19-0.0.1-SNAPSHOT.jar");
        processBuilder.directory(new File("/VolumeT9/app/"));
    
        Process process = processBuilder.start();
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
			
        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = reader.readLine()) != null)
            System.out.println(line);

        try {
			int exitCode = process.waitFor();

			System.out.println("ERRORE CODE: " + exitCode);
		} catch (InterruptedException e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File resultsDir = new File("/VolumeT9/app/FolderTree/" + cname + "/RobotTest/RandoopTest");

        File results [] = resultsDir.listFiles();
        for(File result : results) {
			int score = LineCoverage(result.getAbsolutePath() + "/coveragetot.xml");

			System.out.println(result.toString().substring(result.toString().length() - 7, result.toString().length() - 5));
			int livello = Integer.parseInt(result.toString().substring(result.toString().length() - 7, result.toString().length() - 5));

			System.out.println("La copertura del livello " + String.valueOf(livello) + " è: " + String.valueOf(score));

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/robots");

			JSONArray arr = new JSONArray();

			JSONObject rob = new JSONObject();
			rob.put("scores", String.valueOf(score));
			rob.put("type", "randoop");
			rob.put("difficulty", String.valueOf(livello));
			rob.put("testClassId", cname);

			arr.put(rob);

			JSONObject obj = new JSONObject();
			obj.put("robots", arr);

			StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

			httpPost.setEntity(jsonEntity);

			HttpResponse response = httpClient.execute(httpPost);

		}
		
        // EVOSUITE - T8
		// TODO: RICHIEDE AGGIUSTAMENTI IN T8
		Path directory2 = Paths.get("/VolumeT8/app/FolderTree/" + cname + "/" + cname + "SourceCode");
		
		try {
			// Verifica se la directory esiste già
			if (!Files.exists(directory2)) {
				// Crea la directory
				Files.createDirectories(directory2);
				System.out.println("La directory è stata creata con successo.");
			} else {
				System.out.println("La directory esiste già.");
			}
		} catch (Exception e) {
			System.out.println("Errore durante la creazione della directory: " + e.getMessage());
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = directory2.resolve(fileName);
			System.out.println(filePath.toString());
			Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
		}

        ProcessBuilder processBuilder2 = new ProcessBuilder();

		//robot_generazione.sh VCardBean VCardBean "/mnt/c/Users/didom/Desktop/copianuovo/T10-G40-main/T8-G21/Progetto_SAD_GRUPPO21_TASK8/Progetto_def/opt_livelli/classe/VCardBean" 5

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
        processBuilder2.command("bash", "/VolumeT8/app/Prototipo2.0/robot_generazione.sh", cname, cname,  "/VolumeT8/app/FolderTree/" + cname + "/" + cname + "SourceCode" ,"3");
        processBuilder2.directory(new File("/VolumeT8/app/"));
    
        Process process2 = processBuilder2.start();
 
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
        String line2;
        while ((line2 = reader2.readLine()) != null)
            System.out.println(line2);
			
        reader2 = new BufferedReader(new InputStreamReader(process2.getErrorStream()));
        while ((line2 = reader2.readLine()) != null)
            System.out.println(line2);

        try {
			int exitCode = process2.waitFor();

			System.out.println("ERRORE CODE: " + exitCode);
		} catch (InterruptedException e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File resultsDir2 = new File("/VolumeT8/app/FolderTree/" + cname + "/RobotTest/EvoSuiteTest");

        File results2 [] = resultsDir2.listFiles();
        for(File result2 : results2) {
			System.out.println(result2.getAbsolutePath());
			//File path = new File(result2.getAbsolutePath() + "/TestReport/statistics.csv");
			//int score=LineCoverageCSV("path:"+ path.getAbsolutePath());
			int score = LineCoverageCSV(result2.getAbsolutePath() + "/TestReport/statistics.csv");

			System.out.println(result2.toString().substring(result2.toString().length() - 7, result2.toString().length() - 5));
			int livello = Integer.parseInt(result2.toString().substring(result2.toString().length() - 7, result2.toString().length() - 5));

			System.out.println("La copertura del livello " + String.valueOf(livello) + " e': " + String.valueOf(score));

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/robots");

			JSONArray arr = new JSONArray();

			JSONObject rob = new JSONObject();
			rob.put("scores", String.valueOf(score));
			rob.put("type", "EvoSuite");
			rob.put("difficulty", String.valueOf(livello));
			rob.put("testClassId", cname);

			arr.put(rob);

			JSONObject obj = new JSONObject();
			obj.put("robots", arr);

			StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

			httpPost.setEntity(jsonEntity);

			HttpResponse response = httpClient.execute(httpPost);

		}
    }
    

}
