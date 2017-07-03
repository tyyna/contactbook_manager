/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.guiapp;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import fi.muni.cz.contacts.Person;
import java.util.ResourceBundle;

/**
 *
 * @author teena
 */
public class PersonTableModel extends AbstractTableModel {
    
    private List<Person> contacts;
    ResourceBundle bundle = java.util.ResourceBundle.getBundle("fi/muni/cz/guiapp/Texts");
    
    
    public PersonTableModel(Collection<Person> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }
    
    public void setContacts(Collection<Person> contacts) {
        synchronized (this) {
            this.contacts = new ArrayList<>(contacts);
            fireTableDataChanged();
        }
    }
    

    public void addContact(Person person) {
        synchronized (this) {
            if (person != null) {
                contacts.add(person);
                fireTableRowsInserted(contacts.size() - 1, contacts.size());
            }
        }
    }
        
    public void removeContact(int index) {
        synchronized (this) {
            contacts.remove(index);
            fireTableRowsDeleted(index, index);
        }        
    }

    public Person getPerson(int index) {
        synchronized (this) {
            return contacts.get(index);
        }
    }
    
    @Override
    public String getColumnName(int column) {
        synchronized (this) {
            if (column == 0) {
                return bundle.getString("name");
            }
            if (column == 1) {
                return bundle.getString("surname");
            }
            return null;
        }
    }
    
    @Override
    public int getRowCount() {
        synchronized (this) {
            return contacts.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        synchronized (this) {
            if (columnIndex == 0) {
                return contacts.get(rowIndex).getName();
            } else if (columnIndex == 1) {
                return contacts.get(rowIndex).getSurname();            
            } else {
                return null;
            }
        }
    }
    
    
    
}
