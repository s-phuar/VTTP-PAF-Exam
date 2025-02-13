package vttp.batch5.paf.movies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import net.sf.jasperreports.engine.data.JsonData;
import net.sf.jasperreports.json.data.JsonDataSource;
import vttp.batch5.paf.movies.services.MovieService;

@Controller
public class MainController {

  @Autowired
  private MovieService movieService;


  // TODO: Task 3
   @GetMapping("/api/summary")
   @ResponseBody
   public ResponseEntity<JsonArray> getProlificDirectors(@RequestParam("count") String count){

    JsonArray array = movieService.getProlificDirectors(Integer.parseInt(count));

    return ResponseEntity.ok().body(array);
   }


  
  // TODO: Task 4
   @GetMapping("/api/summary/pdf")
   @ResponseBody
   public ResponseEntity<String> generatePdf(@RequestParam("count") String count){




    return null;
   }


}
