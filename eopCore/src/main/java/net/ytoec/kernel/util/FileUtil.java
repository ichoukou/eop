package net.ytoec.kernel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件处理集合类 主要功能：文件上传，拷贝，删除，显示文件夹下所有文件等方法
 * 
 */

public class FileUtil {

	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 构造方法
	 * 
	 */
	private FileUtil() {
	}

	/**
	 * 读文件
	 * 
	 * @param file
	 *            文件
	 * @return
	 */
	public static String readFile(File file) {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer buf = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				buf.append(line + "\n");
			}
			reader.close();
			return buf.toString();

		} catch (FileNotFoundException ex) {
			throw new RuntimeException("没有找到文件:" + file.getAbsolutePath());
		} catch (Exception ex) {
			log.debug(line);
			throw new RuntimeException(ex);
		}

	}

	/**
	 * 以UTF-8编码读取文件，避免中文乱码问题（上面的方法readFile(File file)以UTF-8编码在读取UTF8文件有乱码问题）
	 * 
	 * @param f
	 *            文件
	 * @return 文件内容
	 */
	public static String readFileByUTF8(File f) {
		StringBuffer fileContent = new StringBuffer();
		try {
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent.append(line + "\n");
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return fileContent.toString();
	}

	/**
	 * 重载 readFile方法
	 * 
	 * @param file
	 *            文件名
	 * @return
	 */
	public static String readFile(String file) {
		return readFile(new File(file));
	}

	/**
	 * 写入文件
	 * 
	 * @param content
	 *            内容
	 * @param file
	 *            文件名
	 */
	public static void writeFile(String content, String file) {
		try {
			File f = new File(file).getParentFile();
			if (!f.exists()) {
				f.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			out.flush();
			out.write(content);
			out.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		log.debug("Write File:" + file);
	}

	private static void writeFile(byte[] content, String file) {
		try {
			File f = new File(file).getParentFile();
			if (!f.exists()) {
				f.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content, 0, content.length);
			fos.flush();
			fos.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		log.debug("Write File:" + file);
	}

	/**
	 * xml文件写入
	 * 
	 * @param document
	 *            文档
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static File writeDom4jDocument(org.dom4j.Document document,
			String fileName) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		// OutputFormat format = OutputFormat.createCompactFormat();

		// format.setEncoding("utf-8");

		XMLWriter xmlWriter;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(fileName), format);
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// XMLWriter xmlWriter = new XMLWriter(new FileWriter(fileName),
		// format);
		return new File(fileName);
	}

	/**
	 * 写入W3C文档
	 * 
	 * @param doc
	 *            文档
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */
	public static void writeW3cDocument(org.w3c.dom.Document doc,
			String fileName) throws Exception {
		// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(
		// fileName), "utf-8"));
		// org.w3c.dom.Element root = doc.getDocumentElement();
		// writeW3cElement(bw, root);
		// bw.flush();
		// bw.close();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		// 设置输出的encoding为改变gb2312

		// transformer.setOutputProperty("encoding", "GB2312");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult("c:/doc.xml");
		transformer.transform(source, result);
	}

	/**
	 * 获取资源
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static InputStream getResource(String fileName) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		return classLoader.getResourceAsStream(fileName);
	}

	/**
	 * 获取资源
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getResourcePath(String fileName) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL fileUrl = classLoader.getResource(fileName);
		if (fileUrl != null) {
			String path = fileUrl.getPath();
			return path.substring(1, path.length());
		}
		return null;
	}

	/**
	 * 获取工程的根目录
	 * 
	 * @return
	 */
	public static String getProjectRoot() {
		return System.getProperty("user.dir");
	}

	/**
	 * 获取源文件的路径
	 * 
	 * @return
	 */
	public static String getSrc() {
		String path = FileUtil.getProjectRoot();
		path = path + "/src";
		return path;
	}

	public static String getToolsPath() {
		File file = new File(getClassRoot() + "/com/fulan/tools");
		return file.getAbsolutePath();
	}

	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            路径
	 * @param newName
	 *            文件名
	 * @return
	 */
	public static String rename(String path, String newName) {
		path = path.replaceAll("\\\\", "/");
		String folder = path.substring(0, path.lastIndexOf("/") + 1);
		String extName = FileUtil.getFileExtName(path);
		return folder + newName + "." + extName;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getFileExtName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos == -1) {
			return "NO_EXT_NAME";
		}
		return fileName.substring(pos + 1);
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 *            文件名
	 * @return
	 */
	public static String getFileName(String path) {
		path = path.replaceAll("\\\\", "/");
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 攻取文件名除去扩展名
	 * 
	 * @param path
	 *            文件名
	 * @return
	 */
	public static String getFileBaseName(String path) {
		path = path.replaceAll("\\\\", "/");
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		int pos = fileName.lastIndexOf(".");
		if (pos == -1) {
			return fileName;
		}
		return fileName.substring(0, pos);
	}

	/**
	 * 重命名文件
	 * 
	 * @param file
	 *            文件
	 * @return
	 */
	public static File rename(File file) {

		String path = file.getParent();

		String name = file.getName();
		int pos = name.lastIndexOf(".");
		String extName = getFileExtName(name);
		if (pos > 0) {
			name = name.substring(0, pos);

		}

		File newFile = new File(path + "/" + name + "-s." + extName);

		return newFile;
	}

	/**
	 * 获取编译文件目录
	 * 
	 * @return
	 */
	public static String getClassRoot() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource("mybatis-config.xml");
		String path;
		try {
			path = URLDecoder.decode(url.getFile(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		log.debug(path);
		// path = path.substring(1);
		path = path.substring(0, path.indexOf("classes") + 7);
		return path;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param file
	 *            文件名
	 */
	public static void createFolder(String file) {
		file = file.replaceAll("\\\\", "/");
		int n = file.lastIndexOf("/");
		int m = file.lastIndexOf(".");
		if (m > n) {
			file = file.substring(0, n);
		}
		log.debug(file);
		new File(file).mkdirs();
	}

	/**
	 * 列出文件目录下的所有文件
	 * 
	 * @param folderName
	 *            文件夹名
	 * @return
	 */
	public static File[] listFolders(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			throw new RuntimeException("(目录不存在。)folder [" + folder
					+ "]not exist。");
		}
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		return files;
	}

	/**
	 * 列出文件夹下面所有扩展名相同的文件名.jpg等
	 * 
	 * @param folderName
	 *            文件名
	 * @param extName
	 *            扩展名
	 * @return
	 */
	public static File[] listFiles(String folderName, final String extName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			throw new RuntimeException("(目录不存在。)folder [" + folder
					+ "]not exist。");
		}
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				log.info(pathname.getPath());
				return pathname.getName().toLowerCase()
						.lastIndexOf("." + extName.toLowerCase()) != -1;
			}
		});
		return files;
	}

	public static void gbk2utf8(File file, String[] exts)
			throws UnsupportedEncodingException {
		if (file.isFile()) {
			for (int i = 0; i < exts.length; i++) {
				if (file.getAbsolutePath().toLowerCase().endsWith(exts[i])) {
					String gbkContent = readFile(file);
					byte[] utf8Content = gbk2utf8(gbkContent);
					System.out.println(file.getAbsolutePath());
					writeFile(utf8Content, file.getAbsolutePath());
					break;
				}
			}
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				gbk2utf8(files[i], exts);
			}
		}
	}

	/**
	 * UTF-8 采用变长度字节来表示字符，理论上最多可以到 6 个字节长度(一个字符六个字节)。 UTF-8 编码兼容了 ASC II(0-127)，
	 * 也就是说 UTF-8 对于 ASC II 字符的编码是和 ASC II 一样的。 对于超过一个字节长度的字符，才用以下编码规范：
	 * 左边第一个字节1的个数表示这个字符编码字节的位数， 例如两位字节字符编码样式为为：110xxxxx 10xxxxxx；
	 * 三位字节字符的编码样式为：1110xxxx 10xxxxxx 10xxxxxx.； 以此类推，六位字节字符的编码样式为：1111110x
	 * 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx。 xxx
	 * 的值由字符编码的二进制表示的位填入。只用最短的那个足够表达一个字符编码的多字节串。 例如： Unicode 字符： 00 A9（版权符号） =
	 * 1010 1001， UTF-8 编码为：11000010 10101001 = 0x C2 0xA9; 字符 22 60 (不等于符号) =
	 * 0010 0010 0110 0000， UTF-8 编码为：11100010 10001001 10100000 = 0xE2 0x89
	 * 0xA0
	 * 
	 * UTF-8的编码原理和特性：
	 * 
	 * U+0000~U+007E 1 _ _ _ _ _ _ _ (7bits)
	 * 
	 * U+0080~U+07FF 1 1 0_ _ _ _ _ 1 0_ _ _ _ _ _ (11bits)
	 * 
	 * U+0800~U+FFFF 1 1 1 0 _ _ _ _ 1 0 _ _ _ _ _ _ 1 0 _ _ _ _ _ _ (16bits)
	 * 
	 * @param chenese
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] gbk2utf8(String chenese)
			throws UnsupportedEncodingException {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = c[i];
			String word = Integer.toBinaryString(m);

			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");

			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);

			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];

		}
		return fullByte;

	}

	/**
	 * 移动文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * 
	 */
	public static boolean moveFile(File sourceFile, File targetFile) {
		if (sourceFile.isFile()) {
			return moveOnlyFile(sourceFile, targetFile);
		} else {
			File[] files = sourceFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				String newName = targetFile.getAbsolutePath() + "/"
						+ files[i].getName();
				moveFile(files[i], new File(newName));
			}
			sourceFile.delete();
			return true;
		}
	}

	/**
	 * 仅移动文件夹下面的文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 */
	private static boolean moveOnlyFile(File sourceFile, File targetFile) {
		targetFile.getParentFile().mkdirs();
		if (targetFile.exists())
			targetFile.delete();
		log.debug("Move File. sourceFile:" + sourceFile + ", targetFile:"
				+ targetFile.getPath());
		boolean flag = sourceFile.renameTo(targetFile);
		if (!flag) {
			try {
				copyFile(sourceFile, targetFile);

				if (sourceFile.exists()) {
					log.debug("delete file:" + sourceFile + ":"
							+ sourceFile.delete());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		if (sourceFile.isFile()) {
			copyOnlyFile(sourceFile, targetFile);
		} else {
			File[] files = sourceFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				String newName = targetFile.getAbsolutePath() + "/"
						+ files[i].getName();
				copyFile(files[i], new File(newName));
			}
		}
	}

	/**
	 * 仅拷贝文件夹下面的文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 */
	private static void copyOnlyFile(File sourceFile, File targetFile)
			throws IOException {
		log.debug("copy from:" + sourceFile);
		log.debug("copy to:" + targetFile);
		targetFile.getParentFile().mkdirs();

		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(targetFile);

		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = fis.read(buf)) != -1) {
			fos.write(buf, 0, n);
		}

		fis.close();
		fos.flush();
		fos.close();
	}

	public static URL generate(URL url, File file) throws Exception {

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpConn.getInputStream()));
		String line;
		StringBuffer buf = new StringBuffer();
		while ((line = in.readLine()) != null) {
			buf.append("\n" + line);
		}
		in.close();

		FileWriter fw = new FileWriter(file);
		fw.write(buf.toString());
		fw.flush();
		fw.close();

		return url;
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            文件名
	 */
	public static void delFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				delOnlyFile(file);
			} else {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					delFile(files[i]);
				}
				file.delete();
			}
		} else {
			log.error("Delete File:" + file);
		}
	}

	/**
	 * 仅删除文件夹下面的文件
	 * 
	 * @param file
	 *            文件夹名
	 * 
	 */
	private static void delOnlyFile(File file) {
		if (file.exists()) {
			file.delete();
			log.error("Delete File:" + file);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件目录
	 */
	public static void delFile(String filePath) {
		File file = new File(filePath);
		log.debug(filePath);
		delFile(file);
	}

	/**
	 * 根据URL加载文件头文件
	 * 
	 * @param urlName
	 *            文件名
	 */
	public static String loadResponseByUrl(String urlName) throws IOException {
		URL url = new URL(urlName);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection
				.addRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705)");
		connection.addRequestProperty("Cache-Control", "no-cache");
		// connection.addRequestProperty( "Referer",
		// "http://www.szzfw.com/search1.asp?search=capture&page=2" );
		connection.addRequestProperty("Accept", "*/*");
		// connection.addRequestProperty( "Accept-Language", "zh-cn");
		// connection.addRequestProperty( "Accept-Encoding", "gzip,
		// deflate");
		connection.addRequestProperty("Connection", "Keep-Alive");
		// connection.addRequestProperty( "Cookie",
		// "ASPSESSIONIDQSQTABDT=CACAMLKBFDFJFPLFDPMCIIPI");
		connection.connect();
		connection.disconnect();
		System.out.println(connection.getResponseCode());
		System.out.println(connection.getResponseMessage());
		return connection.getURL().toString();
	}

	/**
	 * 根据URL加载文件头文件
	 * 
	 * @param urlName
	 *            文件名
	 */
	public static String loadByUrl(String urlName) throws IOException {
		StringBuffer buf = new StringBuffer();

		URL url = new URL(urlName);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection
				.addRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705)");
		connection.addRequestProperty("Cache-Control", "no-cache");
		// connection.addRequestProperty( "Referer",
		// "http://www.szzfw.com/search1.asp?search=capture&page=2" );
		connection.addRequestProperty("Accept", "*/*");
		// connection.addRequestProperty( "Accept-Language", "zh-cn");
		// connection.addRequestProperty( "Accept-Encoding", "gzip,
		// deflate");
		connection.addRequestProperty("Connection", "Keep-Alive");
		// connection.addRequestProperty( "Cookie",
		// "ASPSESSIONIDQSQTABDT=CACAMLKBFDFJFPLFDPMCIIPI");
		connection.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			buf.append("\n" + line);
		}
		in.close();

		return buf.toString();
	}

	/**
	 * 创建文件目录
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	@SuppressWarnings("unused")
	private static File createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static void replaceInFolder(File file, String oldStr, String newStr) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				replaceInFolder(files[i], oldStr, newStr);
			}
		} else {
			String content = FileUtil.readFile(file);
			if (file.getName().endsWith(".html")) {
				if (content.indexOf(oldStr) != -1) {
					content = content.replace(oldStr, newStr);
					FileUtil.writeFile(content, file.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 用于读取svn版本比较的文件:特征，差异文件以"Index:" 开头
	 * 
	 * @param file
	 * @return
	 */
	public static String readFileForSvnDiff(File file) {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer buf = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				if (line.indexOf("Index:") != -1) {
					buf.append(line.substring(line.indexOf(":") + 1) + "\n");
				}

			}
			reader.close();
			return buf.toString();

		} catch (FileNotFoundException ex) {
			throw new RuntimeException("没有找到文件:" + file.getAbsolutePath());
		} catch (Exception ex) {
			log.debug(line);
			throw new RuntimeException(ex);
		}

	}

	/**
	 * 读取XML文件
	 * 
	 * @param name
	 *            文件名
	 */
	public static Document readXmlDocument(String name) throws IOException,
			DocumentException {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(name);

		String filePath = url.getFile();
		filePath = URLDecoder.decode(filePath, "utf-8");
		log.info(filePath);
		File file = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buf = new StringBuffer();
		String line = null;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			if (i == 1) {
				// log.debug(line);
			} else {
				buf.append(line + "\n");
			}
			i++;
		}
		reader.close();
		// log.debug(buf.toString());
		return DocumentHelper.parseText(buf.toString());
	}

	public static Document readXmlDocument(File file) throws IOException,
			DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	public static void main(String[] args) {
		// File file = new File("C:/sb/JavaSource");
		// try {
		// zip("c:/java.zip", file);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		String classpath = Thread.currentThread().getContextClassLoader()
				.getResource("/").toString();
		System.out.println(classpath);
		System.out.println(getResourcePath("mybatis-config.xml"));
		System.out.println(getSrc());
	}

}
