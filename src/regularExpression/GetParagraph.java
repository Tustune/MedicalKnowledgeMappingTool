package regularExpression;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * <p>
 * ClassName GetParagraph
 * </p>
 *
 */
public class GetParagraph {
	private String filePath;
	public ArrayList<String> res = new ArrayList<String>();// 段落切分结果

	public GetParagraph() {
	}

	public GetParagraph(String filePath) {
		// TODO Auto-generated constructor stub
		this.filePath = filePath;
	}

	public ArrayList<String> getRes() throws Exception {

		StringBuilder sb = new StringBuilder();// 拼接读取的内容
		String temp = null;// 临时变量，存储sb去除空格的内容

		// BufferedReader reader = new BufferedReader(new FileReader(new
		// File("E:\\研一上学期\\高级软件工程\\知识图谱\\心血管疾病诊疗指南.txt")));
		// BufferedReader reader=new BufferedReader(new InputStreamReader(new
		// FileInputStream("E:\\研一上学期\\高级软件工程\\知识图谱\\心血管疾病诊疗指南3.txt"),"UTF-8"));
		// BufferedReader reader = new BufferedReader(new InputStreamReader(new
		// FileInputStream("/Users/tustunne/Downloads/乔雨/心血管疾病诊疗指南3.txt" +
		// ""), "UTF-8"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.filePath), "UTF-8"));
		int ch = 0;
		while ((ch = reader.read()) != -1) {
			temp = sb.toString().trim().replaceAll("\\s*", "");// 取出前后空格，之后去除中间空格
			if ((char) ch == '\n') {
				// 判断是否是空行
				if (!"".equals(temp)) {
					// 说明到了段落结尾，将其加入链表，并清空sb
					res.add(temp);
				}
				sb.delete(0, sb.length());
			} else {
				// 说明没到段落结尾，将结果暂存
				sb.append((char) ch);
			}
		}
		if (reader.read() == -1) {
			//System.out.println("哈哈，你读到了末尾嘞！");
		}
		// 最后一段如果非空， 将最后一段加入，否则不处理
		if (!"".equals(temp)) {
			res.add(temp);
		}

		// Iterator<String> iterator = res.iterator();
		// while (iterator.hasNext()) {
		// String next = iterator.next();
		// System.out.println("段落开始：");
		// System.out.println(next);
		// }
		// for (int i=0;i<res.size();i++){
		// System.out.println(res.get(1));
		// }
		// System.out.println("23");
		// System.out.println("________________________________");
		// System.out.println("23"+res);
		// System.out.println("________________________________");
		// System.out.println(getRes().get(1));
		// System.out.println("________________________________");
		// System.out.println(res.get(1));
		//System.out.println("段落的个数是：" + res.size());
		return res;
	}

}