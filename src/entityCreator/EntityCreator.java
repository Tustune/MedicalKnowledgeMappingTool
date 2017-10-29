package entityCreator;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import regularExpression.node;

public class EntityCreator {
	private List<node> nodeList;
	private String resultJSON;

	public EntityCreator(List<node> nodeList) {
		this.nodeList = nodeList;
		System.out.println(nodeList.size());
		for (int i = 0; i < nodeList.size(); i++) {
			System.out.print(nodeList.get(i).getName() + "  :");
			System.out.println(nodeList.get(i).getRank());
		}
	}

	private void createEntity() {
		// 生成存放点和边的集合
		List<Document> nodes = new ArrayList<Document>();
		List<Document> edges = new ArrayList<Document>();
		// 先将根结点存入
		nodes.add(new Document("name", nodeList.get(0).getName()));

		if (nodeList.size() != 0) {
			// 开始遍历nodelist
			for (int i = 1; i < nodeList.size(); i++) {
				// 遍历一个节点存储一个节点d
				String name = nodeList.get(i).getName();
				nodes.add(new Document("name", name));

				// 如果当前节点比前一个节点的级别高，则说明是当前节点是上一个节点的下一级标签的内容，直接创建边
				if (nodeList.get(i).getRank() > nodeList.get(i - 1).getRank())
					edges.add(new Document("source", i - 1).append("target", i));
				// 如果当前节点比前一个节点的级别相等或小，则说明当前节点是上一个节点的同级或上级节点
				else {
					// 向前遍历直到找到比当前节点级别小的节点创建边
					for (int j = i - 1; j >= 0; j--) {
						if (nodeList.get(j).getRank() < nodeList.get(i).getRank()) {
							edges.add(new Document("source", j).append("target", i));
							break;
						}		
					}
				}
			}
		}
		System.out.println(nodes.size());
		System.out.println(edges.size());

		Document result = new Document("nodes", nodes).append("edges", edges);
		this.resultJSON = result.toJson();

	}

	public String getEntity() {
		createEntity();
		return this.resultJSON;
	}
}
