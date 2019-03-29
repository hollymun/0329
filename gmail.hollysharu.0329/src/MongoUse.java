import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

public class MongoUse {

	public static void main(String[] args) {
		//MongDB에 접속하기 위한 정보 
		//String ip = "localhost";
		String ip = ""; //문자열을 가지고 연산(+)하는 경우 
		String db_name = null; //문자열을 대입받는 경우 
		int port = -1;
	
		//텍스트 파일의 내용 읽기 
		try(BufferedReader br = new BufferedReader(
									new InputStreamReader(
										new FileInputStream(
											new File("./db.txt"))))){
			//한 줄의 텍스트 읽기 
			String line = br.readLine();
			//System.out.printf("%s\n", line);
			//텍스트 내용: localhost 27017 mymongo 
			//공백을 기준으로 분할하기: 특정 패턴을 가지고 분할 String [] split(String pattern)
			String [] ar = line.split(" ");
			//분할 된 내용의 인덱스 별 값을 변수에 저장 
			ip = ar[0]; 
			port = Integer.parseInt(ar[1]);
			db_name = ar[2];
			
			

		}
		catch(Exception e) {
			System.out.printf("%s\n", e.getMessage());
			e.printStackTrace();
		}
		
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
		
		//users 컬렉션 가져오기 
		MongoCollection<Document> users = db.getCollection("users");
		//기록하기 위해서 쓰기 권한을 가져오기 
		users.getWriteConcern();
		//기록하기 위해서는 Document 객체에 데이터를 설정 - Map 형식 
		//컬렉션 객체가 insertOne 또는 insertMany를 호출
		Document docu = new Document(); 
		docu.put("id", "gamja");
		docu.put("password", "2019");
		docu.put("name", "감자");
		
		users.insertOne(docu);
		
		//users의 전체 데이터 읽기 
/*		FindIterable<Document> result = users.find();
		for(Document temp : result) {
			System.out.printf("%s\n", temp);
		} 
*/
		//조건에 맞는 데이터만 읽어오기 
		FindIterable<Document> result = users.find(
											Filters.eq("id", "root"));
		for(Document temp : result) {
			System.out.printf("%s\n", temp);
		} 
		
		//4. 사용이 끝나면 MongoClient.close() 해야 함 / 예외처리는 안 함 
		client.close();
	}
}
