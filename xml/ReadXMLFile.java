package xml;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.util.Stack;
import collectible.Hero;


public class ReadXMLFile {

  public static Stack<Hero> read(String file_name) {

    Stack<Hero> hero_stack = new Stack<>();

    try {
    String file_path = "data/" + file_name + ".xml";
    FileInputStream fXmlFile = new FileInputStream(new File(file_path));
    BufferedInputStream buf = new BufferedInputStream(fXmlFile);

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(buf);
    doc.getDocumentElement().normalize();

    NodeList nList = doc.getElementsByTagName("hero");

    for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;
            Hero hero = new Hero();

            hero.set_name(eElement.getElementsByTagName("firstname")
                                 .item(0).getTextContent());
            hero.set_age(Integer.parseInt(eElement.getElementsByTagName("age")
                                 .item(0).getTextContent()));

            hero_stack.push(hero);
        }    
    }
    } catch (Exception e) {
    	e.printStackTrace();
    }

    return hero_stack;
  }
}