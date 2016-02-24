package by.intervale.task.dao.xml;


import java.io.File;
import java.io.IOException;

import java.util.Date;


import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;


import by.intervale.task.bean.Item;


public class AuditDAO {
	private File resource;

	public AuditDAO(File resource) {
		this.resource = resource;
	}

	public void add(String action, Item item) throws IOException, SAXException,
			ParserConfigurationException, TransformerException {
		Document doc = Util.buildDocument(resource);
        Node items = doc.getElementsByTagName("actions").item(0);
        Element newElement = buildElement(doc, action, item);
        items.appendChild(newElement);
        Util.save(doc, resource);
	}

	private Element buildElement(Document doc, String action, Item item) {
       	Element newItem = doc.createElement("action");

       	Element time = doc.createElement("time");
       	time.appendChild(doc.createTextNode(String.valueOf(new Date())));
       	newItem.appendChild(time);

       	Element type = doc.createElement("type");
       	type.appendChild(doc.createTextNode(action));
       	newItem.appendChild(type);

       	Element name = doc.createElement("name");
       	name.appendChild(doc.createTextNode(item.getName()));
       	newItem.appendChild(name);

       	return newItem;
	}
}