package fi.muni.cz.contacts;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author bar
 */
public class SchemaValidator {
    class ValidationErrorsHandler implements ErrorHandler{

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            Logger.getAnonymousLogger(SchemaValidator.class.getName()).log(Level.INFO,exception.getMessage());
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            error = exception.getMessage();
            throw new SAXException(error);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            error = exception.getMessage();
            throw new SAXException(error);
        }

        
    }
    private DocumentBuilder docBuilder;
    private String error;
    public SchemaValidator(){
        try {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(new ValidationErrorsHandler());
        } catch (ParserConfigurationException ex) {
            System.err.println("Error initializing parser: "+ ex);
        }
        
    }
    public SchemaValidator(String schemaName){
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("company.xsd"));
            
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            
            dbf.setSchema(schema);
            docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(new ValidationErrorsHandler());
        } catch (SAXException ex) {
            System.err.println("Invalid schema: " + ex.getMessage());
            System.exit(-1);
        } catch (ParserConfigurationException ex) {
            System.err.println("Parser configuration error: " + ex.getMessage());
        }
    }
    
    public String validate(String xmlFilename) throws IOException{
        try {
            Document doc = docBuilder.parse(new File(xmlFilename));
        } catch (SAXException ex) {
            //Logger.getAnonymousLogger(SchemaValidator.class.getName()).log(Level.INFO,ex.getMessage());
            return xmlFilename+ " validation error: " + ex.getMessage();
        } 
        return xmlFilename+" is valid";
    }
    
    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println("Usage (replace slash with backslash on Windows):\n - to validate valid files:\n\t java -jar dist/Jaro2017_PB138_Uloha2.jar src/valid/xml_file\n - to validate invalid files:\n\t java -jar dist/Jaro2017_PB138_Uloha2.jar src/invalid/xml_file");
            System.exit(-1);
        }
        SchemaValidator validator = new SchemaValidator("company.xsd");
        try {
            System.out.println(validator.validate(args[0]));
        } catch (IOException ex) {
            System.err.println("File not found: "+ex.getMessage());
        }
    }
}
