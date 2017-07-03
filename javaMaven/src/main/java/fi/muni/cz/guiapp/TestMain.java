/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.guiapp;

import fi.muni.cz.contacts.Address;
import fi.muni.cz.contacts.AddressBuilder;
import fi.muni.cz.contacts.ContactInformation;
import fi.muni.cz.contacts.ContactManager;
import fi.muni.cz.contacts.ContactManagerImpl;
import fi.muni.cz.contacts.DBUtils;
import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.EmailBuilder;
import fi.muni.cz.contacts.Person;
import fi.muni.cz.contacts.PersonBuilder;
import fi.muni.cz.contacts.Phone;
import fi.muni.cz.contacts.PhoneBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author PC
 */
public class TestMain {
    
    private static ContactInfoWindowForm form;
    private static ContactsWindowForm form2;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ContactInfoWindowForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ContactInfoWindowForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ContactInfoWindowForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ContactInfoWindowForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        Logger logger = Logger.getLogger("logg");
        
        logger.log(Level.INFO, "info funguje takto");
        logger.log(Level.SEVERE, "exception logg");
        
        
        Person person = new PersonBuilder()
                .name("Janko")
                .surname("Mrkvicka")
                .build();
        Address address = new AddressBuilder()
                .street("Hlavna")
                .number("42")
                .city("ZivotVesmirAVubec")
                .buildValid();
        Phone phone = new PhoneBuilder()
                .number(123456789)
                .buildValid();
        Email email = new EmailBuilder()
                .name("stopar")
                .domain("galaxy.guide")
                .buildValid();
        
        DataSource ds = DBUtils.prepareEmbeddedDatabaseHome();
        ContactInformation ci = null;
        ContactManager manager = null;
        try {            
            DBUtils.executeSqlScript(ds, ContactManager.class.getResource("createTables.sql"));
        
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException: " + ex.getMessage());
        }
            System.out.println(ContactManager.class.getResource("createTables.sql") == null ? "script not found" : "script found");
            manager = new ContactManagerImpl(ds);
            //manager.addPerson(person);
            //Collection<Person> col = manager.getAllPersons();
            //Integer id = col.iterator().next().getID();
            //System.out.println(id);
            //ci = manager.selectPerson(id);
            //ci.updateAddress(address);
            //ci.addEmail(email);
            //ci.addPhone(phone);
        
            //System.out.println("Email: " + ci.getEmails().size());
            //System.out.println("Phones: " + ci.getPhones().size());
            
            //Collection<Person> test = manager.getAllPersons();
            //System.out.println("Persons empty = " + test.isEmpty());
        //if (ci == null)
        //    throw new NullPointerException("fail pri inicializacii testovych dat");
        
        
        
        //form = new ContactInfoWindowForm(ci);
        /* Create and display the form */
        
        form2 = new ContactsWindowForm(manager);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                form2.setVisible(true);
            }
        });
        
        
        
        
    }
}
