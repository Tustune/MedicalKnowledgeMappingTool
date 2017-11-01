package EntityLibraryHelper;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class EntityLibraryHelper {
	private List<String> normal;
	private List<String> twoWord;
	private List<String> threeWord;
	private List<String> fourWord;
	private List<String> result;
	private String lastNode;
	
	public EntityLibraryHelper(List<String> normal, List<String> twoWord, List<String> threeWord, List<String> fourWord, String lastNode) {
		this.normal = normal;
		this.twoWord = twoWord;
		this.threeWord = threeWord;
		this.fourWord = fourWord;
		this.lastNode = lastNode;
		this.result = new ArrayList<String>();
	}

	public List<String> getResult() {
		// TODO Auto-generated method stub
		// 首先获取数据库中对应的collection中的所有内容，目前只有一个document，存储着和这个标签有关的所有信息
		MongoDBHelper dbHelper = new MongoDBHelper();
		BasicDBObject query = new BasicDBObject("name", lastNode);
		if(dbHelper.FindDocumentsBy("MedicalItem",query).size() != 0) {
			List<String> lastNodeList = (List<String>) dbHelper.FindDocumentsBy("MedicalItem",query).get(0).get(lastNode);
			if(lastNodeList != null) {
				// 依次遍历三个List
				for (int i = 0; i < normal.size(); i++) {
					if(lastNodeList.contains(normal.get(i)))
						result.add(normal.get(i));
				}
				for (int i = 0; i < twoWord.size(); i++) {
					if(lastNodeList.contains(twoWord.get(i)))
						result.add(twoWord.get(i));
				}
				for (int i = 0; i < threeWord.size(); i++) {
					if(lastNodeList.contains(threeWord.get(i)))
						result.add(threeWord.get(i));
				}
				for (int i = 0; i < fourWord.size(); i++) {
					if(lastNodeList.contains(fourWord.get(i)))
						result.add(fourWord.get(i));
				}
			}
		}
		return this.result;
	}
}
