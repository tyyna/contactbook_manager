package fi.muni.cz.guiapp;

import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.Phone;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xbendel
 */
public class EmailsTableModel extends AbstractTableModel {

    private List<Email> emails;

    public EmailsTableModel(Collection<Email> emails) {
        this.emails = new ArrayList<>(emails);
    }

    public void addEmail(Email email) {
        synchronized (this) {
            if (email != null) {
                emails.add(email);
                fireTableRowsInserted(emails.size() - 1, emails.size());            
            }
        }
    }
    
    public Email getEmail(int index) {
        synchronized (this) {
            return emails.get(index);
        }
    }
    
    public void removeEmail(int index) {
        synchronized (this) {
            emails.remove(index);            
            fireTableRowsDeleted(index, index);
        }
    }
    
    public void refreshData(Collection<Email> dbEmails) {
        synchronized (this) {
            emails = new ArrayList<>(dbEmails);
            fireTableDataChanged();
        }
    }

    @Override
    public String getColumnName(int i) {
        return "";
    }    
    
    @Override
    public int getRowCount() {
        synchronized (this) {
            return emails.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        synchronized (this) {
            if (col == 0) {
                return emails.get(row).toString();
            } else {
                return null;
            }
        }
    }
}
