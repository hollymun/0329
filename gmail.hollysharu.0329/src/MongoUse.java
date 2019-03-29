import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoUse {

	public static void main(String[] args) {
		//MongDB에 접속하기 위한 정보 
		//String ip = "localhost";
		String ip = "127.0.0.1";
		int port = 27017; 
		String db_name = "mymongo";
	
		//Mongo DB 접속 
		//MongoClient 클래스를 이용 - 생성자에 new ServerAddress(ip, port) 설정
		//1. 접속한 후 listDatabases()라는 메소드 호출하면 Iterator가 구현돼서 객체가 리턴됨 
		MongoClient client = new MongoClient(new ServerAddress(ip, port));
		ListDatabasesIterable<Document> dbs = client.listDatabases();
		for(Document document : dbs) {
			System.out.printf("%s\n", document); 
		}
		
		//2. 접속한 후 getDatabase(데이터베이스이름)를 호출하면 
		//데이터베이스를 사용할 수 있는 객체가 리턴됨 - 실제 작업 getDatabase가 함 
		MongoDatabase db = client.getDatabase(db_name);
		
		//3. 위의 객체를 가지고 listCollectionNames()를 호출하면 
		//모든 컬렉션의 이름이 리턴됨 
		MongoIterable<String> dbNames = db.listCollectionNames();
		//컬렉션이름을 모두 출력 
		for(String name : dbNames) {
			System.out.printf("%s\n", name);
		}
		
		//4. 사용이 끝나면 MongoClient.close() 해야 함 / 예외처리는 안 함 
		client.close();
	}
}
