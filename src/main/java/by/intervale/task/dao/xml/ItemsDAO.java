package by.intervale.task.dao.xml;


import java.io.File;
import java.io.IOException;


import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;


import by.intervale.task.bean.Item;


public class ItemsDAO {
	private File resource;

	public ItemsDAO(File resource) {
		this.resource = resource;
	}

	public void create(Item item) throws IOException, SAXException,
			ParserConfigurationException, TransformerException {
		Document doc = Util.buildDocument(resource);

        int newItemId = 1;
        NodeList items = doc.getElementsByTagName("item");
        if (items.getLength() > 0) {
        	int maxId = 1;
        	for (int i = 0; i < items.getLength(); i++) {
        		Element element = (Element) items.item(i);
        		String id = element.getAttribute("id");
        		int idNumber = Integer.parseInt(id.split("-")[1]);
        		if (idNumber > maxId) {
        			maxId = idNumber;
        		}
        	}
	       	newItemId = maxId + 1;
        }
        item.setId("item-" + newItemId);
		buildElement(doc, item);

        Node root = doc.getElementsByTagName("items").item(0);
        Element newElement = buildElement(doc, item);
        root.appendChild(newElement);

        Util.save(doc, resource);
	}

	public void edit(Item item) throws IOException, SAXException,
			ParserConfigurationException, TransformerException {
		Document doc = Util.buildDocument(resource);
        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
			Element element = (Element) items.item(i);
			if (item.getId().equals(element.getAttribute("id"))) {
				Element newElement = buildElement(doc, item);
				Node root = element.getParentNode();
				root.replaceChild(newElement, element);
			    break;
			}
        }
        Util.save(doc, resource);
	}

	public void delete(Item item) throws IOException, SAXException,
			ParserConfigurationException, TransformerException {
		Document doc = Util.buildDocument(resource);
        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
			Element element = (Element) items.item(i);
			if (item.getId().equals(element.getAttribute("id"))) {
				element.getParentNode().removeChild(element);
				break;
			}
        }
        Util.save(doc, resource);
	}

	private Element buildElement(Document doc, Item item) {
       	Element newItem = doc.createElement("item");
       	newItem.setAttribute("id", item.getId());

       	Element newDate = doc.createElement("date");
       	newDate.appendChild(doc.createTextNode(item.getDate()));
       	newItem.appendChild(newDate);

       	Element newName = doc.createElement("name");
       	newName.appendChild(doc.createTextNode(item.getName()));
       	newItem.appendChild(newName);

       	Element newStatus = doc.createElement("status");
       	String status = "on".equals(item.getStatus()) ? "true" : "false";
       	newStatus.appendChild(doc.createTextNode(status));
       	newItem.appendChild(newStatus);

       	Element newDescription = doc.createElement("description");
       	newDescription.appendChild(doc.createTextNode(item.getDescription()));
       	newItem.appendChild(newDescription);

       	return newItem;
	}
}