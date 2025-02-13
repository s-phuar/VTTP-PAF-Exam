package vttp.batch5.paf.movies.repositories;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;

@Repository
public class MongoMovieRepository {
    @Autowired
    private MongoTemplate template;


 // TODO: Task 2.3
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
//  var collection = db.getCollection(collectionName);
//  collection.insertMany(documents);
 //
 public void batchInsertMovies(List<Document> documents, String collectionName) {
    MongoCollection<Document>collection = template.getCollection(collectionName);

    collection.insertMany(documents);
 }

//count number of documents in collection
//db.comment.countDocuments();
public long countCollection(String collectionName){
    MongoCollection<Document> collectionTable = template.getCollection(collectionName);
    return collectionTable.countDocuments();
}


 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
 public void logError() {

 }

 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
        //  db.imdb.aggregate([
        //     {$group:{_id:"$director", movies_count:{$sum:1}, total_revenue:{$sum:"$revenue"}, total_budget:{$sum:"$budget"}}},
        //     {$addFields : {profit_loss : {$subtract: [ "$total_revenue", "$total_budget"]}}},
        //     {$sort:{movies_count:-1}},
        //     {$limit:5}
        //     ])
 //

 public List<Document> getProlificDirectors(int limit){

    GroupOperation byDirector = Aggregation.group("director")
        .count().as("movies_count")
        .sum("revenue").as("total_revenue")
        .sum("budget").as("total_budget");

    AddFieldsOperation addFieldsOperation = Aggregation.addFields().addField("profit_loss")
        .withValueOf((Subtract.valueOf("total_revenue").subtract("total_budget"))).build();


    SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "movies_count");
    LimitOperation limitOp = Aggregation.limit(limit);

    Aggregation pipeline =Aggregation.newAggregation(byDirector,addFieldsOperation,sortOperation,limitOp);

    return template.aggregate(pipeline, "imdb", Document.class).getMappedResults();

 }




}
