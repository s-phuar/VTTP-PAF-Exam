package vttp.batch5.paf.movies.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.json.JsonObject;

public class Movie {
    

    private String imdb_id;
    private int vote_average;
    private int vote_count;
    private Date release_date;
    private int revenue;
    private int budget;
    private int runtime;

    public String getImdb_id() {return imdb_id;}
    public void setImdb_id(String imdb_id) {this.imdb_id = imdb_id;}
    public int getVote_average() {return vote_average;}
    public void setVote_average(int vote_average) {this.vote_average = vote_average;}
    public int getVote_count() {return vote_count;}
    public void setVote_count(int vote_count) {this.vote_count = vote_count;}
    public Date getRelease_date() {return release_date;}
    public void setRelease_date(Date release_date) {this.release_date = release_date;}
    public int getRevenue() {return revenue;}
    public void setRevenue(int revenue) {this.revenue = revenue;}
    public int getBudget() {return budget;}
    public void setBudget(int budget) {this.budget = budget;}
    public int getRuntime() {return runtime;}
    public void setRuntime(int runtime) {this.runtime = runtime;}

    @Override
    public String toString() {
        return "Movie [imdb_id=" + imdb_id + ", vote_average=" + vote_average + ", vote_count=" + vote_count
                + ", release_date=" + release_date + ", revenue=" + revenue + ", budget=" + budget + ", runtime="
                + runtime + "]";
    }
    
    

    public static Movie jsonToMovie(JsonObject jsonObj){
        Movie mov = new Movie();
        mov.setImdb_id(jsonObj.getString("imdb_id"));
        mov.setVote_average(jsonObj.getInt("vote_average"));
        mov.setVote_count(jsonObj.getInt("vote_count"));
        mov.setRelease_date(strToDate(jsonObj.getString("release_date")));
        mov.setRevenue(jsonObj.getInt("revenue"));
        mov.setBudget(jsonObj.getInt("budget"));
        mov.setRuntime(jsonObj.getInt("runtime"));

        return mov;
    }   

  public static Date strToDate(String date) {
    try{
    Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
    return convertedDate;
    }catch(ParseException e){
        System.out.println("error parsing date");
        return null;
    }
  }


}
