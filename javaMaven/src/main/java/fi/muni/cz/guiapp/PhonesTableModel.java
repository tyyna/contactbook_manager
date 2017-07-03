package fi.muni.cz.guiapp;

import fi.muni.cz.contacts.Phone;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xbendel
 */
public class PhonesTableModel extends AbstractTableModel {

    private List<Phone> phones;

    public PhonesTableModel(Collection<Phone> phones) {
        this.phones = new ArrayList<>(phones);
    }

    public void addPhone(Phone phone) {
        synchronized (this) {
            if (phone != null) {
                phones.add(phone);
                fireTableRowsInserted(phones.size() - 1, phones.size());
            }
        }
    }
    
    public Phone getPhone(int index) {
        synchronized (this) {
            return phones.get(index);
        }
    }
    
    public void removePhone(int index) {
        synchronized (this) {
            phones.remove(index);            
            fireTableRowsDeleted(index, index);
        }
    }
    
    public void refreshData(Collection<Phone> dbPhones) {
        synchronized (this) {
            phones = new ArrayList<>(dbPhones);
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
            return phones.size();
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
                return phones.get(row).toString();
            } else {
                return null;
            }
        }
    }

}
