/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.contacts;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.BufferedWriter;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author T. Pitner, tyyna
 */
public class Convertor {
    
    private ContactManagerImpl manager;
    private Document doc;
    private static File output;
    
    public Element createElement() throws SQLException {
        Element contacts = doc.createElement("contacts");
        
        List<Person> persons = new ArrayList(manager.getAllPersons());
        
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person == null) {
                continue;
            }
            Element contact = doc.createElement("contact");
            contact.setAttribute("cid", person.getID().toString());
            
            Element name = doc.createElement("name");
            name.setTextContent(person.getName());
            contact.appendChild(name);
            
            Element surname = doc.createElement("surname");
            surname.setTextContent(person.getSurname());
            contact.appendChild(surname);
            
            Element info = doc.createElement("info");            
            ContactInformation contactInfo = manager.selectPerson(person.getID());
            
            Address addressObj = contactInfo.getAddress();
            if (addressObj != null) {
                Element address = doc.createElement("address");
                
                Element street = doc.createElement("street");
                street.setTextContent(addressObj.getStreet());
                address.appendChild(street);
                
                Element number = doc.createElement("number");
                number.setTextContent(addressObj.getNumber());
                address.appendChild(number);
                
                Element city = doc.createElement("city");
                city.setTextContent(addressObj.getCity());
                address.appendChild(city);
                
                info.appendChild(address);
            }
            contact.appendChild(info);
            contacts.appendChild(contact);
        }
        
        return contacts;
    }

    private Convertor(DataSource ds) throws SAXException, ParserConfigurationException,
            IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            doc = builder.parse(output.toURI().toString());
        } catch(Exception e) {
            doc = builder.newDocument();
        }
        manager = new ContactManagerImpl(ds);       
    }

    public void serializetoXML(URI output)
            throws IOException, TransformerConfigurationException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(doc);
                
        StreamResult result = new StreamResult(output.toString());
        transformer.transform(source, result);
            
            
        transformer.transform(
            source, 
            new StreamResult(new File("contacts.xml")));
    }

    public void serializetoXML(File output) throws IOException,
            TransformerException {
        serializetoXML(output.toURI());
        System.out.println(output.toURI().toString());
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        output = new File("contacts.xml");
        DataSource ds = DBUtils.prepareEmbeddedDatabaseHome();
        try {            
            DBUtils.executeSqlScript(ds, ContactManager.class.getResource("createTables.sql"));
        
        } catch (SQLException ex) {
            //ignore - tables already created
        }
        
        Convertor convertor = new Convertor(ds);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();

        try {            
            trans.transform(new DOMSource(convertor.createElement()), new StreamResult(System.out));
            convertor.createElement();
            convertor.serializetoXML(output);
        } catch (SQLException ex) {
            Logger.getLogger(Convertor.class.getName()).log(Level.SEVERE, "Error in createElement.", ex.getMessage());
        }
        System.out.println(output.canWrite());
    }
}
