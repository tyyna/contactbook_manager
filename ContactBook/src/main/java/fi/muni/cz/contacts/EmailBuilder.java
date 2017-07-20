package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/14/17
 */
public class EmailBuilder {
    private String name = null;
    private String domain = null;

    public EmailBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EmailBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public Email build() {
        EmailImpl email = new EmailImpl();
        email.setName(name);
        email.setDomain(domain);

        return email;
    }

    public Email buildValid() {
        String constructedRawEmail = name + "@" + domain;
        return new EmailImpl(constructedRawEmail);
    }

}
