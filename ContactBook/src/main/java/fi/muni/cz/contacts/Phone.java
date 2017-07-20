package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public interface Phone {
    int getNumber();
    //void setNumber(int number); //chceme set?, mozno by stacil construktor [polymorfizmus]

    @Override //da sa vynutit tato logika?
    String toString();
}
