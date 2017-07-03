package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */
public interface Phone {
    int getNumber();
    //void setNumber(int number); //chceme set?, mozno by stacil construktor [polymorfizmus]

    @Override //da sa vynutit tato logika?
    String toString();
}
