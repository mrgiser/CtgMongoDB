package cn.he.zhao.ctg.mongodb.async;

import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

/**
 * Created by HeFeng on 2018/2/28.
 */
public class AsyncTest {

    public static void main(String[] args){
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://132.122.232.234:27017"));

        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("runoob");

        Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        collection.insertOne(doc, new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable t) {
                System.out.println("Inserted!");
            }
        });

        collection.count(
                new SingleResultCallback<Long>() {
                    @Override
                    public void onResult(final Long count, final Throwable t) {
                        System.out.println(count);
                    }
                });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
