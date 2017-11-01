package thulac;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.*;

import manage.Filter;
import manage.NegWord;
import manage.Postprocesser;
import manage.Preprocesser;
import manage.Punctuation;
import manage.TimeWord;
import manage.VerbWord;

import base.POCGraph;
import base.SegmentedSentence;
import base.TaggedSentence;

import character.CBTaggingDecoder;

public class thulac {
	String modelsPath;
	List<String> result;

	public thulac(String modelsPath) {
		// TODO Auto-generated constructor stub
		this.modelsPath = modelsPath;
		result = new ArrayList<String>();
	}

	/**
	 * @param args
	 * @throws IOException
	 */

	public void thulacCal(String inputFile) throws IOException {
		//
		String[] args = {"-seg_only"};

		String user_specified_dict_name = null;
		String model_path_char = null;

		Character separator = '_';

		boolean useT2S = false;
		boolean seg_only = false;
		boolean useFilter = false;
		int maxLength = 50000;
		String input_file = "";
		String output_file = "";

		int c = 0;
		while (c < args.length) {
			String arg = args[c];
			if (arg.equals("-t2s")) {
				useT2S = true;
			} else if (arg.equals("-user")) {
				user_specified_dict_name = args[++c];
			} else if (arg.equals("-deli")) {
				separator = args[++c].charAt(0);
			} else if (arg.equals("-seg_only")) {
				seg_only = true;
			} else if (arg.equals("-filter")) {
				useFilter = true;
			} else if (arg.equals("-model_dir")) {
				model_path_char = args[++c];
			} else if (arg.equals("-input")) {
				input_file = args[++c];
			} else if (arg.equals("-output")) {
				output_file = args[++c];
			} else {
				// showhelp();
				return;
			}
			c++;
		}

		model_path_char = modelsPath + "/";
		input_file = inputFile;
		String prefix;
		prefix = model_path_char;
//		if (model_path_char != null) {
//			prefix = model_path_char;
//			if (prefix.charAt(prefix.length() - 1) != '/') {
//				prefix += "/";
//			}
//		} else {
//			prefix = "models/";
//		}

		String oiraw;
		String raw = new String();
		POCGraph poc_cands = new POCGraph();
		TaggedSentence tagged = new TaggedSentence();
		SegmentedSentence segged = new SegmentedSentence();

		CBTaggingDecoder cws_tagging_decoder = new CBTaggingDecoder();
		CBTaggingDecoder tagging_decoder = new CBTaggingDecoder();
		if (seg_only) {
			cws_tagging_decoder.threshold = 0;
			cws_tagging_decoder.separator = separator;
			cws_tagging_decoder.init((prefix + "cws_model.bin"), (prefix + "cws_dat.bin"), (prefix + "cws_label.txt"));
			cws_tagging_decoder.setLabelTrans();
		} else {
			tagging_decoder.threshold = 10000;
			tagging_decoder.separator = separator;
			tagging_decoder.init((prefix + "model_c_model.bin"), (prefix + "model_c_dat.bin"),
					(prefix + "model_c_label.txt"));
			tagging_decoder.setLabelTrans();
		}

		Preprocesser preprocesser = new Preprocesser();
		preprocesser.setT2SMap((prefix + "t2s.dat"));
		Postprocesser nsDict = new Postprocesser((prefix + "ns.dat"), "ns", false);
		Postprocesser idiomDict = new Postprocesser((prefix + "idiom.dat"), "i", false);
		Postprocesser userDict = null;
		if (user_specified_dict_name != null) {
			userDict = new Postprocesser(user_specified_dict_name, "uw", true);
		}
		Punctuation punctuation = new Punctuation((prefix + "singlepun.dat"));
		TimeWord timeword = new TimeWord();
		NegWord negword = new NegWord((prefix + "neg.dat"));
		Filter filter = null;
		if (useFilter) {
			filter = new Filter((prefix + "xu.dat"), (prefix + "time.dat"));
		}

		BufferedReader reader = null;
		try {
			if (input_file != "") {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input_file)), "UTF8"));
			} else
				reader = new BufferedReader(new InputStreamReader(System.in));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream out = null;
		if (output_file != "") {
			out = new FileOutputStream(output_file);
		}

		long startTime = System.currentTimeMillis();// 获取当前时间
		Vector<String> vec = null;
		while (true) {
			vec = getRaw(reader, maxLength);
			if (vec.size() == 0)
				break;
			// if(oiraw==null) break;
			for (int i = 0; i < vec.size(); i++) {
				oiraw = vec.get(i);
				if (useT2S) {
					String traw = new String();
					traw = preprocesser.clean(oiraw, poc_cands);
					raw = preprocesser.T2S(traw);
				} else {
					raw = preprocesser.clean(oiraw, poc_cands);
				}
				if (raw.length() > 0) {
					if (seg_only) {
						cws_tagging_decoder.segment(raw, poc_cands, tagged);
						cws_tagging_decoder.get_seg_result(segged);
						nsDict.adjust(segged);
						idiomDict.adjust(segged);
						punctuation.adjust(segged);
						timeword.adjust(segged);
						negword.adjust(segged);
						if (userDict != null) {
							userDict.adjust(segged);
						}
						if (useFilter) {
							filter.adjust(segged);
						}
						
						for (int j = 0; j < segged.size(); j++) {
							this.result.add(segged.get(j));
						}

//						if (out != null) {
//							for (int j = 0; j < segged.size(); j++) {
//								byte[] buff = new byte[] {};
//								buff = segged.get(j).getBytes();
//								try {
//									out.write(buff, 0, buff.length);
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								if (j != segged.size() - 1)
//									out.write(' ');
//							}
//							if (i == vec.size() - 1)
//								out.write('\n');
//							else
//								out.write(' ');
//						} else {
//							System.out.print(segged.get(i));
//							for (int j = 1; j < segged.size(); j++)
//								System.out.print(" " + segged.get(j));
//							if (i == vec.size() - 1)
//								System.out.print("\n");
//							else
//								System.out.print(" ");
//						}
					} 
//						else {
//						tagging_decoder.segment(raw, poc_cands, tagged);
//						nsDict.adjust(tagged);
//						idiomDict.adjust(tagged);
//						punctuation.adjust(tagged);
//						timeword.adjustDouble(tagged);
//						negword.adjust(tagged);
//						if (userDict != null) {
//							userDict.adjust(tagged);
//						}
//						if (useFilter) {
//							filter.adjust(tagged);
//						}
//
//						if (out != null) {
//							for (int j = 0; j < tagged.size(); j++)
//								tagged.get(j).print(out);
//							if (i == vec.size() - 1)
//								out.write('\n');
//							else
//								out.write(' ');
//						} else {
//							for (int j = 0; j < tagged.size(); j++)
//								tagged.get(j).print();
//							if (i == vec.size() - 1)
//								System.out.print("\n");
//							else
//								System.out.print(" ");
//						}
//					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

	}

	public Vector<String> getRaw(BufferedReader reader, int maxLength) {
		String ans = null;
		Vector<String> ans_vec = new Vector<String>();
		try {
			while ((ans = reader.readLine()) != null) {
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ans == null)
			return ans_vec;
		if (ans.length() < maxLength) {
			ans_vec.add(ans);
		} else {
			Pattern p = Pattern.compile(".*?[。？！；;!?]");
			Matcher m = p.matcher(ans);
			int num = 0, pos = 0;
			String tmp;
			while (m.find()) {
				tmp = m.group(0);
				if (num + tmp.length() > maxLength) {
					ans_vec.add(ans.substring(pos, pos + num));
					pos += num;
					num = tmp.length();
				} else {
					num += tmp.length();
				}
			}
			if (pos != ans.length())
				ans_vec.add(ans.substring(pos));
		}
		return ans_vec;
	}
	
	public List<String> getResult(){
		return this.result;
	}
}
