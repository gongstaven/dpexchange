import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量更改dimens.xml文件中的dp值<br>
 * 命令行执行的方式
 * 
 * @author gongyingjun
 *
 */
public class ExchangeDp {

	/**
	 * ChatGame2 (Eclipse Ant Build)
	 */
	// private static final String RES_DIR = "/res/";

	/**
	 * ChatGameAS (Android Studio Gradle Build)
	 */
	private static final String RES_DIR = "/ChatGame2/src/main/res/";

	/**
	 * 主函数
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File dirNow = new File(ExchangeDp.class.getClass().getResource("/")
				.getPath());
		debug("当前目录：" + dirNow.getAbsolutePath() + "\n");
		BufferedReader buf = new BufferedReader(
				new InputStreamReader(System.in));
		debug("请输入项目根目录，如果是当前目录，请直接按下回车");
		String dirProject = buf.readLine();
		if (dirProject.equals("")) {
			dirProject = dirNow.getAbsolutePath();
		}
		if (!new File(dirProject).exists()) {
			debug("\n错误！找不到项目根目录，退出！(" + dirProject + ")");
			return;
		}
		File[] rootFiles = new File(dirProject).listFiles();
		boolean isValid = false;
		for (File f : rootFiles) {
			if (f.getName().equals("res")) {
				isValid = true;
				break;
			}
		}
		if (!isValid) {
			debug("\n错误！不是有效的Android项目目录，退出！(" + dirProject + ")");
			return;
		}
		debug("\n根目录为：\n" + dirProject);
		debug("\n请输入基本dp值，输入\"*\"代表默认values文件夹，例如：360（按下回车）");
		String baseDp = buf.readLine();
		boolean isDefault = baseDp.equals("*");
		if (baseDp.equals("")) {
			baseDp = "360";
		}
		File baseFile = new File(dirProject, RES_DIR + "values"
				+ ((isDefault ? "" : "-w" + baseDp + "dp")) + "/dimens.xml");
		if (!baseFile.exists()) {
			debug("\n错误！找不到文件，退出! (" + baseFile.getAbsolutePath() + ")");
			return;
		}
		debug("\n基本dp值为：" + (isDefault ? "默认" : baseDp + "dp") + "\n文件：\n"
				+ baseFile.getAbsolutePath());
		File resDir = new File(dirProject, RES_DIR);
		File[] files = resDir.listFiles();
		List<File> dstFiles = new ArrayList<File>();
		for (File f : files) {
			if (f.getName().equals("values")
					|| (f.getName().contains("value") && f.getName().contains(
							"dp"))) {
				dstFiles.add(f);
				continue;
			}
		}
		String dirValues = "";
		for (File f : dstFiles) {
			dirValues += f.getName() + "\n";
		}
		String defaultDstValue = "320;360;400;480;600";
		debug("\n找到如下可以转换的dp配置文件夹：\n" + dirValues + "请输入需要转换的dp值，分号分割，例如："
				+ defaultDstValue + "（按下回车）");
		// 等待输入
		String dstValueStr = buf.readLine();
		boolean isDefaultDstValue = dstValueStr.equals("");
		if (isDefaultDstValue) {
			dstValueStr = defaultDstValue;
		}
		if (!dstValueStr.contains(";")) {
			debug("\n非法输入，退出！(" + dstValueStr + ")");
			return;
		}
		String[] dstValues = dstValueStr.split(";");
		if (dstValues.length != dstFiles.size()) {
			debug("\n输入数组长度不匹配，退出！(" + dstValues.length + ", "
					+ dstFiles.size() + ")");
			return;
		}
		debug("\n正在转换dp值，请稍候...");
		String dimenFileStr = "dimens.xml";
		String baseString = readTextFile(baseFile.getAbsolutePath());
		baseString = deleteBlankLines(baseString);
		File file;
		long time = System.currentTimeMillis();
		for (int i = 0; i < dstFiles.size(); i++) {
			file = dstFiles.get(i);
			String resultString = compute(baseString, baseDp, dstValues[i]);
			resultString = deleteBlankLines(resultString);
			boolean result = writeFile(file.getAbsolutePath() + "/"
					+ dimenFileStr, resultString);
			debug("转换结果：" + result + " --> " + file.getName() + "/"
					+ dimenFileStr + " " + (System.currentTimeMillis() - time)
					+ "ms");
			time = System.currentTimeMillis();
		}
		debug("\n批量转换成功，请检查相应的目录。");
	}

	/**
	 * 删除空行
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteBlankLines(String input) {
		return input.trim();
	}

	/**
	 * 重新生成字符串
	 * 
	 * @param str
	 * @return
	 */
	private static String compute(String str, String oriValue, String dstValue) {

		double scale = (Integer.valueOf(dstValue) + 0.0)
				/ Integer.valueOf(oriValue);
		/** 保留三位小数点，四舍五入 */
		BigDecimal bigDecimal = new BigDecimal(scale);
		scale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		debug("缩放比例：" + scale + "f (" + oriValue + " --> " + dstValue + ")");

		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new StringReader(str));
			String line = "";
			while ((line = br.readLine()) != null) {
				String breakStr = "\n";
				String tmpStr = "";
				if (line.contains("<dimen")) {

					if (line.contains("dp")) {
						tmpStr = line.substring(line.indexOf(">") + 1,
								line.lastIndexOf("dp"));
						tmpStr = line.replace(
								">" + tmpStr,
								">"
										+ String.valueOf(toScaleSize(tmpStr,
												scale)));
					} else if (line.contains("sp")) {
						tmpStr = line.substring(line.indexOf(">") + 1,
								line.lastIndexOf("sp"));
						tmpStr = line.replace(
								">" + tmpStr,
								">"
										+ String.valueOf(toScaleSize(tmpStr,
												scale)));
					}

					sb.append(tmpStr + breakStr);

				} else if (line.contains("<!--")) {
					if (line.contains(oriValue)) {
						tmpStr = line.replace(oriValue, dstValue);
					} else {
						tmpStr = line;
					}
					sb.append(tmpStr + breakStr);
				} else {
					sb.append(line + breakStr);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 计算结果 四舍五入 整数
	 * 
	 * @param str
	 * @param scale
	 * @return
	 */
	private static int toScaleSize(String str, double scale) {
		BigDecimal big = new BigDecimal(String.valueOf(scale
				* Integer.valueOf(str))).setScale(0, BigDecimal.ROUND_HALF_UP);
		return big.intValue();
	}

	/**
	 * 读取文本文件到字符串中
	 * 
	 * @param path
	 * @return
	 */
	private static String readTextFile(String path) {
		File file = new File(path);
		if (file.isFile()) {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader buffreader = null;
			try {
				String content = "";
				is = new FileInputStream(file);

				if (is != null) {
					isr = new InputStreamReader(is);
					buffreader = new BufferedReader(isr);
					String line = "";
					while ((line = buffreader.readLine()) != null) {
						content += line + "\n";// 这里要不要加\n看应用中具体需求吧。
					}
				}
				return content;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					isr = null;
				}
				if (buffreader != null) {
					try {
						buffreader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					buffreader = null;
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					is = null;
				}
			}
		}
		return null;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 * @param sets
	 * @throws IOException
	 */
	private synchronized static boolean writeFile(String filePath,
			String content) {
		FileWriter fw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(filePath);
			out = new PrintWriter(fw);
			out.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 打印日志
	 * 
	 * @param str
	 */
	private static void debug(String str) {
		System.out.println(str);
	}

}
