package fi.muni.cz.convertor;

import fi.muni.cz.contacts.Address;
import fi.muni.cz.contacts.ContactInformation;
import fi.muni.cz.contacts.ContactManager;
import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.Person;
import fi.muni.cz.contacts.Phone;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Converts database from given ContactManager to XML document (called in GUI).
 * Documented by XSD Schema "contacts.xsd".
 * 
 * @author pb138, tyyna
 */
public class Convertor {
    
    private static Document doc;
    private static File output;
    
    public static Element createElement(ContactManager manager) throws SQLException {
        Element contacts = doc.createElement("contacts");
        contacts.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        contacts.setAttribute("xsi:noNamespaceSchemaLocation", "contacts.xsd");
        
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
            
            List<Email> emailsObj = new ArrayList(contactInfo.getEmails());
            if (!emailsObj.isEmpty()) {
                Element emails = doc.createElement("emails");
                
                for (int j = 0; j < emailsObj.size(); j++) {
                    Element email = doc.createElement("email");
                    email.setTextContent(emailsObj.get(j).toString());
                    emails.appendChild(email);
                }
                
                info.appendChild(emails);
            }
            
            List<Phone> phonesObj = new ArrayList(contactInfo.getPhones());
            if (!phonesObj.isEmpty()) {
                Element phones = doc. createElement("phones");
                
                for (int k = 0; k < phonesObj.size(); k++) {
                    Element phone = doc.createElement("phone");
                    phone.setTextContent(phonesObj.get(k).toString());
                    phones.appendChild(phone);
                }
                
                info.appendChild(phones);
            }
            
            contact.appendChild(info);
            contacts.appendChild(contact);
        }
        
        return contacts;
    }

    public static void serializetoXML(ContactManager manager, File output)
            throws IOException, TransformerConfigurationException, TransformerException,
                    SQLException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(createElement(manager));
                
        StreamResult result = new StreamResult(output.toURI().toString());
        transformer.transform(source, result);
    }

    public static void convertToXML(ContactManager manager) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        Path home = Paths.get(System.getProperty("user.home")+ File.separator + "ContactBook");
        output = new File(home.toUri().toString().substring(5) + File.separator, "contacts.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            doc = builder.parse(output.toURI().toString());
        } catch(Exception e) {
            doc = builder.newDocument();
        }  

        try {            
            serializetoXML(manager, output);
        } catch (SQLException ex) {
            Logger.getLogger(Convertor.class.getName()).log(Level.SEVERE, "Error in createElement.", ex.getMessage());
        }
    }
}
