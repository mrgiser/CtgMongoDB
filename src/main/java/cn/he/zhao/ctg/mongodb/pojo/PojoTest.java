package cn.he.zhao.ctg.mongodb.pojo;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
/**
 * Created by HeFeng on 2018/2/28.
 */
public class PojoTest {

    public static void main(String[] args){

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient( "132.122.232.234" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("test");
        database = database.withCodecRegistry(pojoCodecRegistry);

        MongoCollection<Person> collection = database.getCollection("people", Person.class);

        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));

        collection.insertOne(ada);

        List<Person> people = asList(
                new Person("Charles Babbage", 45, new Address("5 Devonshire Street", "London", "W11")),
                new Person("Alan Turing", 28, new Address("Bletchley Hall", "Bletchley Park", "MK12")),
                new Person("Timothy Berners-Lee", 61, new Address("Colehill", "Wimborne", null))
        );

        collection.insertMany(people);

        Block<Person> printBlock = new Block<Person>() {
            @Override
            public void apply(final Person person) {
                System.out.println(person);
            }
        };

        collection.find().forEach(printBlock);

        Person somebody = collection.find(eq("address.city", "Wimborne")).first();
        System.out.println(somebody);

        collection.find(gt("age", 30)).forEach(printBlock);

        collection.updateOne(eq("name", "Ada Byron"), combine(set("age", 23), set("name", "Ada Lovelace")));

        UpdateResult updateResult = collection.updateMany(not(eq("zip", null)), set("zip", null));
        System.out.println(updateResult.getModifiedCount());

        collection.deleteOne(eq("address.city", "Wimborne"));

        DeleteResult deleteResult = collection.deleteMany(eq("address.city", "London"));

        System.out.println(deleteResult.getDeletedCount());
    }
}
