package thulac;

import java.util.ArrayList;
import java.util.List;

import EntityLibraryHelper.EntityLibraryHelper;
import regularExpression.node;

public class ThulacCreator {
	private List<node> nodeList;
	private String modelsPath;

	public ThulacCreator(List<node> nodeList, String modelsPath) {
		this.nodeList = nodeList;
		this.modelsPath = modelsPath;
	}

	public List<node> recalNodeList() {
		for (int i = 0; i < this.nodeList.size(); i++) {
			if (this.nodeList.get(i).getContent() == null || this.nodeList.get(i).getContent().equals("null"))
				continue;
			if (this.nodeList.get(i).getRank() != 5)
				continue;
			String nodeListContent = this.nodeList.get(i).getContent().replaceAll("\r", "");
			nodeListContent.replaceAll("null", "");
			ThulacAnalysis ta = new ThulacAnalysis(nodeListContent, this.modelsPath);
			List<String> thuResult = ta.calThulacResult();
			// 向前遍历查找上一结点，用于实体库数据库查找的表头
			String lastNode = "";
			for (int j = i - 1; j >= 0; j--) {
				if (this.nodeList.get(j).getRank() < this.nodeList.get(i).getRank()) {
					lastNode = this.nodeList.get(j).getName();
				}
			}

			List<String> twoWord = new ArrayList<String>();
			List<String> threeWord = new ArrayList<String>();
			List<String> fourWord = new ArrayList<String>();
			// 对test进行结果分析，生成二词一组和三词一组的形式去数据库中查找是否存在
			if (this.nodeList.size() > 3) {
				for (int j = 1; j < thuResult.size(); j++) {
					twoWord.add(thuResult.get(j) + thuResult.get(j - 1));
				}
				for (int j = 2; j < thuResult.size(); j++) {
					threeWord.add(thuResult.get(j) + thuResult.get(j - 1) + thuResult.get(j - 2));
				}
				for (int j = 3; j < thuResult.size(); j++) {
					fourWord.add(thuResult.get(j) + thuResult.get(j - 1) + thuResult.get(j - 2) + thuResult.get(j - 3));
				}
			}
			
			// 单词，双词，和三词进行数据库匹配，查看当前内容是否在实体库中存在，如果存在就存储下来并且添加到nodeList中去
			EntityLibraryHelper elh = new EntityLibraryHelper(thuResult, twoWord, threeWord, fourWord, lastNode);
			List<String> result = elh.getResult();
			
			// 将result添加到nodelist中
			if(result != null || result.size() != 0) {
				for (int j = 0; j < result.size(); j++) {
					this.nodeList.add(i, new node(this.nodeList.get(i).getRank()+1,result.get(i),""));
				}
			}


			// if (test != null) {
			// for (int j = 0; j < test.size(); j++) {
			// System.out.print(test.get(j) + " ");
			// }
			// System.out.println();
			// }
			
		}
		return this.nodeList;
	}
}
