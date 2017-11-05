package thulac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ThulacAnalysis {
	private String inputStr;
	private String modelsPath;
	//private String[] outputStrArray;
	private List<String> outputSrtArray;
	private String tempFilePath;
	
	public ThulacAnalysis(String inputStr, String modelsPath){
		this.inputStr = inputStr;
		this.modelsPath = modelsPath;
		this.tempFilePath = this.modelsPath + "/tempInput.txt";
	}
	
	public List<String> calThulacResult() {
		if(inputStr != null && !inputStr.equals("null")) {
			// 首先将String写入临时文件
			writeFile();
			// thulac词法分析器读取文件生成结果array
			thulac tc = new thulac(this.modelsPath);
			try {
				tc.thulacCal(this.tempFilePath);
				this.outputSrtArray = tc.getResult();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.outputSrtArray;
	}

	private void writeFile() {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File(this.tempFilePath);
			fop = new FileOutputStream(file);
			// if file doesn’t exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			fop.write(inputStr.getBytes());
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
