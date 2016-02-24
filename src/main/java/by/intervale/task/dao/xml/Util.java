package by.intervale.task.dao.xml;


import java.io.File;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;

import org.xml.sax.SAXException;


class Util {
	static Document buildDocument(File resource)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        return documentBuilder.parse(resource);
	}

	static void save(Document doc, File destination)
			throws TransformerException {
       	TransformerFactory tFactory = TransformerFactory.newInstance();
       	Transformer transformer = tFactory.newTransformer();
       	DOMSource source = new DOMSource(doc);
       	StreamResult result = new StreamResult(destination);
       	transformer.transform(source, result);
	}
}