package cn.my.yaya.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.runoob.com/mongodb/mongodb-java.html
 */
public class MongodbJdbc {
    public static void main(String[] args) {
        //链接到mongodb服务
        MongoClient mongoClient = new MongoClient("localhost",27017);
        //链接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
        System.out.println("Connect to database successfully");
        try {
            mongoDatabase.createCollection("test");
            System.out.println("创建集合成功");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.out.println("集合创建失败");
        }
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        System.out.println("集合选择成功");

        //插入文档
        /**
         * 1. 创建文档 org.bson.Document 参数为key-value的格式
         * 2. 创建文档集合List<Document>
         * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>)
         *        插入单个文档可以用 mongoCollection.insertOne(Document)
         * */
        Document document = new Document("title","mongodb").
                append("description","database").
                append("likes",100).
                append("by","Fly");
        List<Document> documents =new ArrayList<Document>();
        documents.add(document);
        //collection.insertOne(document);
        collection.insertMany(documents);
        System.out.println("文档插入成功");

        //检索所有文档
        /**
         * 1. 获取迭代器FindIterable<Document>
         * 2. 获取游标MongoCursor<Document>
         * 3. 通过游标遍历检索出的文档集合
         * */
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){
            System.out.println(mongoCursor.next());
        }

        //更新文档   将文档中likes=100的文档修改为likes=200
        collection.updateMany(Filters.eq("likes",100),new Document("$set",new Document("likes",200)));
        //检索查看结果
        mongoCursor = findIterable.iterator();
        System.out.println("检索更新后的结果");
        while(mongoCursor.hasNext()){
            System.out.println(mongoCursor.next());
        }

        //删除文档
        //删除符合条件的第一个文档
        collection.deleteOne(Filters.eq("likes",200));
        //删除所有符合条件的文档
        collection.deleteMany (Filters.eq("likes", 200));
        //删除后的结果
        System.out.println("删除后的结果");
        while(mongoCursor.hasNext()){
            System.out.println(mongoCursor.next());
        }
    }
}
