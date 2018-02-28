package cn.he.zhao.ctg.mongodb.client;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by HeFeng on 2018/2/28.
 * mongodb 客户端示例
 */
public class Client {

    public static void main(String[] args){

        //创建客户端连接
        MongoClient mongoClient = new MongoClient( "132.122.232.234" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("runoob");

        //插入文档
//        Document doc = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("info", new Document("x", 203).append("y", 102));
//
//        collection.insertOne(doc);

        System.out.println(collection.count());

        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        //遍历集合1
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        //遍历集合1
        for (Document cur : collection.find()) {
            System.out.println(cur.toJson());
        }

        myDoc = collection.find(eq("name", "MongoDB")).first();
        System.out.println(myDoc.toJson());
    }
}
