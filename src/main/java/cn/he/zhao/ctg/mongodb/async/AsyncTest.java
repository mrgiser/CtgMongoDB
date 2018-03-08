package cn.he.zhao.ctg.mongodb.async;

import com.mongodb.*;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.async.client.MongoClient;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Created by HeFeng on 2018/2/28.
 * 异步客户端示例
 */
public class AsyncTest {

    public static void main(String[] args){

        MyRunable run = new MyRunable();

        Thread thread=new Thread(run);
        thread.start();

        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyRunable implements Runnable {

    @Override
    public void run() {

        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                                                        .maxWaitQueueSize(5000)
                                                        .maxSize(10).build();
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(asList(
                        new ServerAddress("132.122.232.236", 20000),
                        new ServerAddress("132.122.232.235", 20000),
                        new ServerAddress("132.122.232.234", 20000)))
                .build();


        MongoClientSettings settings = MongoClientSettings.builder()
                                        .connectionPoolSettings(connectionPoolSettings)
                                        .clusterSettings(clusterSettings).build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("testdb");
        MongoCollection<Document> collection = database.getCollection("table6");

        long start = System.currentTimeMillis();
        for (int i=0;i<10000;i++){
            final Document doc = new Document("id", i)
                    .append("type", "database"+i)
                    .append("test","testval"+i)
                    .append("name", "name"+i);

            try{
                collection.insertOne(doc, new SingleResultCallback<Void>() {
                    @Override
                    public void onResult(final Void result, final Throwable t) {
                        if (t != null){
                            System.out.println(t.getMessage());
                            return;
                        }
                        System.out.println("Inserted!"+ doc.get("id"));
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


        System.out.println("use time = " + (System.currentTimeMillis()- start));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--------for done--------");

        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

        SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable t) {
                System.out.println("Operation Finished!");
            }
        };

        DBObject groupFields = new BasicDBObject("_id", null);
        groupFields.put("count", new BasicDBObject("$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        List<BasicDBObject> aggreList = new ArrayList<BasicDBObject>();
        aggreList.add(group);
        collection.aggregate(aggreList).forEach(printBlock, callbackWhenFinished);


//        collection.count(
//                new SingleResultCallback<Long>() {
//                    @Override
//                    public void onResult(final Long count, final Throwable t) {
//                        System.out.println("count = "+ count);
//                    }
//                });
    }
}