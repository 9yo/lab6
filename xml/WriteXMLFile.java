package xml;


import java.io.File;
import java.io.StringWriter;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import collectible.Hero;


public class WriteXMLFile {

    public static void write (Stack<Hero> hero_stack, String file_name) {



      try {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("heroes_data");
        doc.appendChild(rootElement);

        for (Hero hero: hero_stack) {

	        // staff elements
	        Element staff = doc.createElement("hero");
	        rootElement.appendChild(staff);

	        // firstname elements
	        Element name = doc.createElement("firstname");
	        name.appendChild(doc.createTextNode(hero.get_name()));
	        staff.appendChild(name);

	        // age elements
	        Element age = doc.createElement("age");
	        age.appendChild(doc.createTextNode(Integer.toString(hero.get_age())));
	        staff.appendChild(age);

            //description elements
            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(hero.get_description()));
            staff.appendChild(description);

            //location
            Element location = doc.createElement("location");
            location.appendChild(doc.createTextNode(hero.get_location().toString()));
            staff.appendChild(location);

	    }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);

        //StreamResult result = new StreamResult(new File("C:\\file.xml"));

        // Output to console for testing

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String strResult = writer.toString();

        //FileOutputStream out= new FileOutputStream("base.xml"); 
        String file_path = "data/" + file_name + ".xml";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file_path));
        // перевод строки в байты
        byte[] buffer = strResult.getBytes();
        bos.write(buffer, 0, buffer.length);
        bos.close();

      } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
      } catch (TransformerException tfe) {
        tfe.printStackTrace();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
}