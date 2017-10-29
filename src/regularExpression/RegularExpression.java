package regularExpression;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {
	private String filePath;

	public RegularExpression(String filePath) {
		// TODO Auto-generated constructor stub
		this.filePath = filePath;
	}

	public String getChapterResult(String managers) {
		String ls = null;
		Pattern pattern = Pattern.compile("(\\s*第)(.{1,9})[章](\\s*)(.*)");
		Matcher matcher = pattern.matcher(managers);
		if (matcher.find()) {
			// while (matcher.find())
			ls = matcher.group();
			// return ls;

		} else {
			// System.out.println("NO MATCH");
		}

		return ls;
	}

	public String getSectionResult(String managers) {
		String ls = null;
		Pattern pattern = Pattern.compile("(\\s*第)(.{1,9})[节](\\s*)(.*)");
		Matcher matcher = pattern.matcher(managers);
		if (matcher.find()) {
			// while (matcher.find())
			ls = matcher.group();
			// return ls;

		} else {
			// System.out.println("NO MATCH");
		}

		return ls;
	}

	public String getSubjectResult(String managers) {
		String ls = null;
		Pattern pattern = Pattern.compile("【(.*)】");
		Matcher matcher = pattern.matcher(managers);
		if (matcher.find()) {
			// while (matcher.find())
			ls = matcher.group();
			// return ls;

		} else {
			// System.out.println("NO MATCH");
		}

		return ls;
	}

	// 1.
	public String getSubSubjectResult(String managers) {
		String ls = null;
		// Pattern pattern = Pattern.compile("\\s*\\d+\\..*[^.,。](?=\\n)");
		Pattern pattern = Pattern.compile("[1-9]{1}.{1}(临床表现|特殊检查|室间隔缺损|瓣膜型单纯肺动脉口狭窄|"
				+ "部分性肺静脉崎形引流|原发性肺动脉高压|外科治疗|外科手术治疗|药物治疗|内科治疗|风湿热|先天性二尖瓣狭窄|罕见病因|左房黏液瘤|严重的主动脉瓣关闭不全|相对性二尖瓣狭窄|缩窄性心包炎)");
		// Pattern pattern = Pattern.compile("([1-9])([0-9])*(\\.)(?!%)(.*)");
		// Pattern pattern = Pattern.compile("([1-9])([0-9])*(\\.)(.*)");
		// Pattern pattern =
		// Pattern.compile("(\\n)([1-9])([0-9])*(\\.)(?!([0-9][0-9])*)(\\n)");
		// Pattern pattern = Pattern.compile("[1-9][0-9](\\.).*(?=\\n)");
		// Pattern pattern = Pattern.compile("([1-9][0-9])*(\\.){1}(.*)(?=\\n)");
		// Pattern pattern = Pattern.compile("([1-9])([0-9])*(\\.)(.*)(\\n)");

		Matcher matcher = pattern.matcher(managers);
		if (matcher.find()) {
			// while (matcher.find())
			ls = matcher.group();
			// return ls;

		} else {
			// System.out.println("NO MATCH");
		}

		return ls;
	}

	// (1)
	public String getLeastSubjectResult(String managers) {
		String ls = null;

		Pattern pattern = Pattern.compile("(\\s*)\\([1-9][0-9]*\\).*(?=:|：)");

		Matcher matcher = pattern.matcher(managers);
		if (matcher.find()) {

			ls = matcher.group();

		} else {
		}

		return ls;
	}

	// 去掉标题级别，仅存储标题内容
	public String getTitleName(String title, String splitText) {
		String titleName = null;
		String[] s1 = title.split(splitText);
		titleName = s1[s1.length - 1];
		return titleName;
	}

	public String getThirdTitleName(String title, String splitText1, String splitText2) {
		String titleName = null;
		// String[] s1 = title.split(splitText1);
		titleName = title.substring(title.indexOf(splitText1) + 1, title.indexOf(splitText2));
		// titleName = s1[s1.length - 1];
		return titleName;
	}

	public String getFourthTitleName(String title, String splitText) {
		String titleName = null;
		// String[] s1 = title.split(splitText);
		titleName = title.substring(title.indexOf(splitText) + 1);
		return titleName;
	}

	public String getFifthTitleName(String title, String splitText1, String splitText2) {
		String titleName = null;
		titleName = title.substring(title.indexOf(")") + 1, title.indexOf(":"));
		return titleName;
	}

	public ArrayList<node> getNodeList() throws Exception {

		// 获取输入文本
		GetParagraph getParagraph = new GetParagraph(this.filePath);
		ArrayList<String> inputString = getParagraph.getRes();

		// 最终返回带级别的文本列表
		ArrayList<node> nodeList = new ArrayList<node>();

		ArrayList<String> wholeList = new ArrayList<String>();
		for (int i = 0; i < inputString.size(); i++) {
			String t = inputString.get(i);
			// System.out.println("判断文本："+t);
			String ch1 = getChapterResult(t);
			String ch2 = getSectionResult(t);
			String ch3 = getSubjectResult(t);
			String ch4 = getSubSubjectResult(t);
			String ch5 = getLeastSubjectResult(t);
			// 判断是否为各级标题,true为是

			boolean tempFlag1 = false;
			boolean tempFlag2 = false;
			boolean tempFlag3 = false;
			boolean tempFlag4 = false;
			boolean tempFlag5 = false;
			// System.out.println("1:"+ch1);
			// System.out.println("2:"+ch2);
			// System.out.println("3:"+ch3);
			// System.out.println("4:"+ch4);
			// System.out.println("5:"+ch5);
			// 先将所有标题存储到一个列表wholeList里

			// 每次判断都先定义一个node存储起来
			node thisNode = new node();
			String thisNodeName = null;

			// 匹配为章名
			if (ch1 != null) {
				tempFlag1 = true;
				wholeList.add(ch1);
				thisNode.setRank(1);
				thisNodeName = getTitleName(ch1, "章");
				thisNode.setName(thisNodeName);
			} else if (ch2 != null) {
				tempFlag2 = true;
				wholeList.add(ch2);
				thisNode.setRank(2);
				thisNodeName = getTitleName(ch2, "节");
				thisNode.setName(thisNodeName);
			} else if (ch3 != null) {
				tempFlag3 = true;
				wholeList.add(ch3);
				thisNode.setRank(3);
				// thisNodeName=getTitleName(ch3,"】");
				thisNodeName = getThirdTitleName(ch3, "【", "】");
				thisNode.setName(thisNodeName);
			} else if (ch4 != null) {
				tempFlag4 = true;
				wholeList.add(ch4);
				thisNode.setRank(4);
				thisNodeName = getFourthTitleName(ch4, ".");
				// thisNodeName=getFourthFifthTitleName(ch4,".","】");
				thisNode.setName(thisNodeName);
			} else if (ch5 != null) {
				tempFlag5 = true;
				wholeList.add(ch5);
				thisNode.setRank(5);
				thisNodeName = getFourthTitleName(ch5, ")");
				thisNode.setName(thisNodeName);
			}
			// 否则就是普通文本，级别设置为6
			else {
				if (i >= 1)
					thisNode.setRank(6);
				String lastNodeContent = nodeList.get(i - 1).getContent();
				if (lastNodeContent == null) {
					nodeList.get(i - 1).setContent(t);
				} else {
					String lastNewNodeContent = lastNodeContent + t;
					nodeList.get(i - 1).setContent(lastNewNodeContent);
				}
			}
			nodeList.add(thisNode);

		}
		// for (int i=0;i<wholeList.size();i++){
		// System.out.println(wholeList.get(i));
		// }

		// for (int i=0;i<nodeList.size();i++){
		// //for (int j=0;i<n)
		// System.out.println(i+"rank:"+nodeList.get(i).getRank());
		// System.out.println(i+"name:"+nodeList.get(i).getName());
		// System.out.println(i+"content:"+nodeList.get(i).getContent());
		// System.out.println("_____________");
		// }
		// System.out.println("分界线1______________________________________________________________________________________________________________________");

		for (int i = 0; i < nodeList.size(); i++) {
			// for (int j=0;i<n)
			if (nodeList.get(i).getRank() == 6) {
				String newContent = nodeList.get(i - 1).getContent() + nodeList.get(i).getContent();
				nodeList.get(i - 1).setContent(newContent);
				nodeList.remove(i);
				i = i - 1;
			}
		}
//		System.out.println(
//				"分界线2_______________________________________________________________________________________________________________________");
//		for (int i = 0; i < nodeList.size(); i++) {
//			// for (int j=0;i<n)
//			System.out.println(i + "rank:" + nodeList.get(i).getRank());
//			System.out.println(i + "name:" + nodeList.get(i).getName());
//			System.out.println(i + "content:" + nodeList.get(i).getContent());
//			System.out.println("_____________");
//		}
		return nodeList;

	}
}
