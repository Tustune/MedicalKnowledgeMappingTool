package EntityLibraryHelper;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBHelper{
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	public MongoDBHelper() {
		try {
			// 连接到 mongodb 服务
			mongoClient = new MongoClient("localhost", 27017);

			// 连接到数据库
			mongoDatabase = mongoClient.getDatabase("medicalknowledge");
			// System.out.println("Connect to database successfully");
		} catch (Exception e) {

		}
	}

	public void InsertDocuments(String collectionName, List<Document> documents) {
		// TODO 插入多行
		try {
			// 获取当前请求collection返回
			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			// System.out.println("Collection test chosen successfully");

			// 将documents插入collection
			collection.insertMany(documents);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Document> FindDocuments(String collectionName) {
		// TODO 找collectionName 并返回所有内容
		List<Document> documents = new ArrayList<Document>();
		try {

			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			// System.out.println("集合 test 选择成功");

			// 检索所有文档
			/**
			 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3. 通过游标遍历检索出的文档集合
			 */
			FindIterable<Document> findIterable = collection.find();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while (mongoCursor.hasNext()) {
				documents.add(mongoCursor.next());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return documents;
	}

	public List<Document> FindDocumentsBy(String collectionName, BasicDBObject condition) {
		// TODO 条件查找并返回
		List<Document> documents = new ArrayList<Document>();
		try {

			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			// System.out.println("集合 test 选择成功");

			// 检索所有文档
			/**
			 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3. 通过游标遍历检索出的文档集合
			 */
			FindIterable<Document> findIterable = collection.find(condition);
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while (mongoCursor.hasNext()) {
				documents.add(mongoCursor.next());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return documents;
	}

	public void updateDocuments(String collectionName, BasicDBObject query, BasicDBObject result) {
		// TODO 更新数据
		try {

			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

			BasicDBObject up = new BasicDBObject("$set", result);
			collection.updateMany(query, up);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
