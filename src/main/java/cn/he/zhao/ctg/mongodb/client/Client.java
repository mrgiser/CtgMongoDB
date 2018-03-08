package cn.he.zhao.ctg.mongodb.client;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

/**
 * Created by HeFeng on 2018/2/28.
 * mongodb 同步客户端示例
 */
public class Client {

    public static void main(String[] args){

        //创建客户端连接
        MongoClient mongoClient = new MongoClient( "132.122.232.235" , 20000 );
        MongoDatabase database = mongoClient.getDatabase("testdb");
        MongoCollection<Document> collection = database.getCollection("table3");

        //插入文档
        long start = System.currentTimeMillis();
        Random r = new Random();
        for (int i=0;i<10000;i++){
            Document doc = new Document("id", i)
                    .append("type", "database"+i)
                    .append("test","testval"+i)
                    .append("name", "name"+i);

            System.out.println("insertOne = " + i);
            collection.insertOne(doc);
        }

        System.out.println("use time = " + (System.currentTimeMillis()- start));

        System.out.println("count = " + collection.count());

        DBObject groupFields = new BasicDBObject("_id", null);
        groupFields.put("count", new BasicDBObject("$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        List<BasicDBObject> aggreList = new ArrayList<BasicDBObject>();
        aggreList.add(group);
        AggregateIterable<Document> output = collection.aggregate(aggreList);

        for (Document dbObject : output)
        {
            System.out.println("Aggregates count = "+ dbObject);
        }

//        Document myDoc = collection.find().first();
//        System.out.println(myDoc.toJson());
//
//
//        //遍历集合
//        MongoCursor<Document> cursor = collection.find().iterator();
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toJson());
//            }
//        } finally {
//            cursor.close();
//        }
//
//        //遍历集合
//        for (Document cur : collection.find()) {
//            System.out.println(cur.toJson());
//        }
//
//        //查找文档
//        Block<Document> printBlock = new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                System.out.println(document.toJson());
//            }
//        };
//        collection.find(eq("name", "MongoDB")).forEach(printBlock);
    }
}
