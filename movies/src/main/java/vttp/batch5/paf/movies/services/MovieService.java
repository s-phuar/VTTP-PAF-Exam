package vttp.batch5.paf.movies.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.naming.java.javaURLContextFactory;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.json.data.JsonDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
@Service
public class MovieService {
  @Autowired
  private MongoMovieRepository mongoMovieRepository;

  @Autowired
  private MySQLMovieRepository mySQLMovieRepository;

  public static final String dataPath = "/data/director_movies_report.jrxml";

  // TODO: Task 2
  @Transactional
  public void batchInsertMovies(List<JsonObject> jsonList){
    batchinsertSQL(jsonList);
    batchInsertMongo(jsonList);
  
  }

  public void batchInsertMongo(List<JsonObject> jsonList){
    List<Document> batch = new ArrayList<>();
    int batchSize = 25;

    jsonList.stream()
      .map(jObject -> Document.parse(jObject.toString()))
      .forEach(d ->{
        batch.add(d);
        if (batch.size() >= batchSize) {
          mongoMovieRepository.batchInsertMovies(batch, "imdb");
          batch.clear();  // Clear the batch after insertion
      }
      });

      if (!batch.isEmpty()) {
        mongoMovieRepository.batchInsertMovies(batch, "imdb");
    }

  }


  public void batchinsertSQL(List<JsonObject> jsonList){
    List<Movie> batchSQL = new ArrayList<>();

    int batchSize = 25;
    
    jsonList.stream()
      .map(jObject -> {
        return Movie.jsonToMovie(jObject);
      }).forEach(m ->{
        batchSQL.add(m);
        if (batchSQL.size() >= batchSize) {
          mySQLMovieRepository.batchInsertMovies(batchSQL);
          batchSQL.clear();  // Clear the batch after insertion
      }
      });

      if (!batchSQL.isEmpty()) {
        mySQLMovieRepository.batchInsertMovies(batchSQL);
      }
  }



  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public JsonArray getProlificDirectors(int count) {
    List<Document> docList =  mongoMovieRepository.getProlificDirectors(count);

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();


    for(Document d: docList){
      JsonObject temp = Json.createObjectBuilder()
        .add("director_name", d.getString("_id"))
        .add("movies_count", d.getInteger("movies_count"))
        .add("total_revenue", d.getInteger("total_revenue"))
        .add("total_budget", d.getInteger("total_budget"))
        // .add("profit_loss", d.getInteger("profit_loss"))
        .build();

      arrayBuilder.add(temp);
    }
    JsonArray array = arrayBuilder.build();
    return array;
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() throws JRException {

    //overall report JSON data source
    Map<String, Object> params = new HashMap<>();
    // params.put("DIRECTOR_TABLE_DATASET", directorsDS);



    //load report
    InputStream overallReport = getClass().getResourceAsStream(dataPath);
    JasperReport jasperReport = JasperCompileManager.compileReport(overallReport);

    //populate report with json data sources
    // JasperPrint print = JasperFillManager.fillReport(report, params, reportDS);

    //generate report  as PDF
    JRPdfExporter exporter = new JRPdfExporter();

    // exporter.setExporterInput(new SimpleExporterInput(print));
    // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("director_data.pdf"));



    

  }

}
