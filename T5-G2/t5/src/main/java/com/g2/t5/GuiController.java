package com.g2.t5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Model.ClassUT;
import com.g2.Model.Game;
import com.g2.Model.Player;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class GuiController {

    private RestTemplate restTemplate;

    @Autowired
    public GuiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

      public List<String> getLevels(String className) {
        List<String> result = new ArrayList<String>();
        int i;
        for(i = 1; i < 11; i++) {
            try {
                restTemplate.getForEntity("http://t4-g18-app-1:3000/robots?testClassId=" + className + "&type=randoop&difficulty="+String.valueOf(i), Object.class);
            } catch (Exception e) {
               
                break;
            }
            String lvl= "randoop lvl " + i;            
            result.add(lvl);
        }
        System.out.println("indice corrente : " + i );
        for(int j=(1);j<i;j++){
            String lvlEvo= "evosuite lvl " + j;          
            result.add(lvlEvo);
             System.out.println("indice corrente : " + j );

        }
        return result;
    }


    public List<ClassUT> getClasses() {
        ResponseEntity<List<ClassUT>> responseEntity = restTemplate.exchange("http://manvsclass-controller-1:8080/home",
            HttpMethod.GET, null, new ParameterizedTypeReference<List<ClassUT>>() {
        });

        return responseEntity.getBody();
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";

        List<ClassUT> classes = getClasses();

        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, List<String>> robotList = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            String valore = classes.get(i).getName();
            List<String> levels = getLevels(valore);
            System.out.println("Lista"+levels);
            hashMap.put(i, valore); // guardare t8.java per fixare i nomi
            robotList.put(i,levels);
            System.out.println("robot:" + robotList);
        }
        model.addAttribute("hashMap", hashMap);

        model.addAttribute("hashMap2", robotList);
        return "main";
    }

    @GetMapping("/report")
    public String reportPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";
        return "report";
    }

    @PostMapping("/save-data")
    public ResponseEntity<String> saveGame(@RequestParam("playerId") int playerId, @RequestParam("robot") String robot,
            @RequestParam("classe") String classe, @RequestParam("difficulty") String difficulty, HttpServletRequest request) {

                if(!request.getHeader("X-UserID").equals(String.valueOf(playerId))) return ResponseEntity.badRequest().body("Unauthorized");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime oraCorrente = LocalTime.now();
                String oraFormattata = oraCorrente.format(formatter);

                GameDataWriter gameDataWriter = new GameDataWriter();
                Game g = new Game(playerId, "descrizione", "nome", difficulty);
                g.setData_creazione(LocalDate.now());
                g.setOra_creazione(oraFormattata);
                g.setClasse(classe);
                JSONObject ids = gameDataWriter.saveGame(g);

                if(ids == null) return ResponseEntity.badRequest().body("Bad Request");

                return ResponseEntity.ok(ids.toString());
    }

    @GetMapping("/editor")
    public String editorPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";

        return "editor";
    }

}
