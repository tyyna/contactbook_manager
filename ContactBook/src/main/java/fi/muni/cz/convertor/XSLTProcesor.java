/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.convertor;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Converts XML document do HTML using XSLT file "contacts_to_html.xsl".
 * 
 * @author bar, tomp, tyyna
 */
public class XSLTProcesor {
    
    public void convertToHTML()
            throws TransformerConfigurationException, TransformerException, URISyntaxException {

        TransformerFactory tf = TransformerFactory.newInstance();

        Path home = Paths.get(System.getProperty("user.home") + File.separator + "ContactBook");
        String wd = Paths.get(".").toAbsolutePath().normalize().toString();


        File xslFile = new File(wd + File.separator + "contacts_to_html.xsl");
        File xmlFile = new File(home.toUri().toString().substring(5) + File.separator, "contacts.xml");

        Transformer xsltProc = tf.newTransformer(
                new StreamSource(xslFile));
        
        xsltProc.transform(
                new StreamSource(xmlFile),
                new StreamResult(new File(home.toUri().toString().substring(5) + File.separator, "contacts.html")));

    }
}
