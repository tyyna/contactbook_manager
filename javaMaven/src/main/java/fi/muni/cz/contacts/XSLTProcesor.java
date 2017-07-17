/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.contacts;

import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author bar, tomp, tyyna
 */
public class XSLTProcesor {
    public static void main(String[] args) 
            throws TransformerConfigurationException, TransformerException {
        
        TransformerFactory tf = TransformerFactory.newInstance();
        
        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File("contacts_to_html.xsl")));
        
        xsltProc.transform(
                new StreamSource(new File("contacts.xml")), 
                new StreamResult(new File("contacts.html")));
    }
}
