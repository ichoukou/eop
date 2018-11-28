package net.ytoec.kernel.action.common;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * 
 * Document读取类.
 * 
 */
public class DocumentReader {

	private static Logger logger=LoggerFactory.getLogger(DocumentReader.class);

	public Document getDocument(InputStream inputStream) {
		Document document = null;
		DocumentBuilderFactory dbf = this.getDocumentBuilderFactory();
		DocumentBuilder db = this.getDocumentBuilder(dbf);
		try {
			document = db.parse(inputStream);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return document;
	}

	private DocumentBuilderFactory getDocumentBuilderFactory() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		return dbf;
	}

	private DocumentBuilder getDocumentBuilder(DocumentBuilderFactory dbf) {
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return db;
	}
}
