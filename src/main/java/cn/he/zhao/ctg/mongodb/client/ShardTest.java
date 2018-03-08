package cn.he.zhao.ctg.mongodb.client;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by HeFeng on 2018/3/8.
 */
public class ShardTest {
    public static void main(String[] args){
        //创建客户端连接
        MongoClient mongoClient = new MongoClient( "132.122.232.235" , 20000 );
        //使用admin数据库才能增加分片
        MongoDatabase database = mongoClient.getDatabase("admin");

        //增加分片集合
        Document doc = new Document().parse("{ shardcollection : \"testdb.table6\",key : {id: 1} }");
        database.runCommand(doc);

        //增加分片集合2
        Document id_doc = new Document("id",1);

        doc = new Document("shardcollection", "testdb.table7")
                .append("key", id_doc);
        database.runCommand(doc);
    }
}
