package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */
public class EmailImpl implements Email {
    private String name;
    private String domain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailImpl email = (EmailImpl) o;

        if (name != null ? !name.equals(email.name) : email.name != null) return false;
        return domain != null ? domain.equals(email.domain) : email.domain == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Cannot set name to Null");
        if (name.isEmpty())
            throw new InvalidFieldException("Cannot set name to Empty");

        this.name = name;
    }

    public void setDomain(String domain) {
        if (domain == null)
            throw new IllegalArgumentException("Cannot set domain to Null");
        if (domain.isEmpty())
            throw new InvalidFieldException("Cannot set domain to Empty");

        this.domain = domain;
    }

    //for dummies
    public String getEmail() { return this.toString(); }

    public EmailImpl() {}

    public EmailImpl(String rawEmail) {
        parseEmailFromString(rawEmail);
    }


    public boolean parseEmailFromString(String rawEmail) {
        if (rawEmail == null)
            throw new IllegalArgumentException("Null string passed to email parser");

        String[] splitted = rawEmail.split("@");

        if (splitted.length == 2) {
                if (splitted[0].isEmpty() || splitted[1].isEmpty())
                    throw new InvalidFieldException("Name or Domain missing");

                name = splitted[0];
                domain = splitted[1];
                return true;
        }

        throw new InvalidFieldException("More than one '@' detected");
    }

    @Override
    public String toString() {
        return name + "@" + domain;
    }

}
