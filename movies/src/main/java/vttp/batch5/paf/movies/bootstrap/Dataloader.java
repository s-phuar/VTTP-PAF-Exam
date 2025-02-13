package vttp.batch5.paf.movies.bootstrap;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import vttp.batch5.paf.movies.services.MovieService;

@Component
public class Dataloader implements CommandLineRunner{
  @Autowired
  private MovieService movieService;

  @Autowired
  private MongoMovieRepository mongoMovieRepository;

  public static final String dataPath = "/data/movies_post_2010.zip";

  //TODO: Task 2

  @Override
  public void run(String... args) throws Exception {

    // check if data has been loaded into mySQL and Mongo
      if(mongoMovieRepository.countCollection("imdb") > 0){
        System.out.println("data already inside");
        return;
      }


    //if data is not loaded, load it
    ZipFile zipFile = new ZipFile(dataPath);
    ZipEntry entry = zipFile.entries().nextElement();
    InputStream input = zipFile.getInputStream(entry);
    BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));

    String line = "x";

    List<JsonObject> jsonList = new LinkedList<>();
    while(null != line){
      line = br.readLine();
      //read each line as jObj
      if (null == line){
        break;
      }
      JsonReader jsonReader = Json.createReader(new StringReader(line));
      JsonObject jObj = jsonReader.readObject();

      //keep if movies released 2018 and beyond
      if(Movie.strToDate(jObj.getString("release_date")).after(Movie.strToDate("2018-01-01"))){
        // Set<String> keys = jObj.keySet();

        //check for invalid values in fields
        // for(String key: keys){
        //   Object value = jObj.get(key);


        //   // if(value instanceof JsonNumber && (((JsonNumber)value).intValue()<0) ){
        //   //   jObj.remove(key);
        //   //   // jObj.put(key, 0); //incompatibility error
        //   // }

        // }
        jsonList.add(jObj);

      }
    }

    //all jobj added to list by this point
    //insert in batches of 25
    movieService.batchInsertMovies(jsonList);



  }







}