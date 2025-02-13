package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.Movie;

@Repository
public class MySQLMovieRepository {
  @Autowired
  private JdbcTemplate template;

  public static final String sql_insertMovie="""
    insert into imdb(imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime)
    values (?, ?, ?, ?, ?, ?, ?)
    on duplicate key update vote_average=values(vote_average),
    vote_count=values(vote_count),
    release_date=values(release_date),
    revenue=values(revenue),
    budget=values(budget),
    runtime=values(runtime)

  """;

  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<Movie> listMovies) {

    List<Object[]> params = listMovies.stream()
    .map(lim ->{
        Object[] rec = new Object[7];
        rec[0] = lim.getImdb_id();
        rec[1] = lim.getVote_average();
        rec[2] = lim.getVote_count();
        rec[3] = lim.getRelease_date();
        rec[4] = lim.getRevenue();
        rec[5] = lim.getBudget();
        rec[6] = lim.getRuntime();
        return rec;
    })
    .toList();

    template.batchUpdate(sql_insertMovie, params);

  }



  // TODO: Task 3


}
