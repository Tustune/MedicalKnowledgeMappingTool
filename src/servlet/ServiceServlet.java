package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import regularExpression.RegularExpression;
import regularExpression.node;

import entityCreator.EntityCreator;

public class ServiceServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置request和response的文本格式
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		// 获取文章的标题和内容
		String title = req.getParameter("title");
		String content = req.getParameter("content");

		// 设置临时存储文件路径
		String filePath = this.getClass().getClassLoader().getResource("../../article.txt").getPath();
		// 将获取到的文本信息存入article文件中，供正则表达式模块使用
		storeArticle(content, filePath);

		// 正则表达式模块调用文件生成ArrayList
		RegularExpression re = new RegularExpression(filePath);
		List<node> nodeList = new ArrayList<node>();
		try {
			nodeList = re.getNodeList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 文章处理模块调用正则表达式模块生成的ArrayList用于进行自然语言处理

		// 本体构建模块生成json格式的信息
		EntityCreator ec = new EntityCreator(nodeList);
		String json = ec.getEntity();

		try {
			PrintWriter out = resp.getWriter();
			out.print(json);
			System.out.println(json);
			out.flush();
			out.close();
		} catch (Exception e) {

		}

	}

	private void storeArticle(String content, String filePath) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File(filePath);
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			fop.write(content.getBytes());
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
