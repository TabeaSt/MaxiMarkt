/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.maximarkt.jpa;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Datenbankklasse für einen Benutzer.
 */
@Entity
@Table(name = "MAXIMARKT_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USERNAME", length = 64)
    @Size(min = 5, max = 64, message = "Der Benutzername muss zwischen fünf und 64 Zeichen lang sein.")
    @NotNull(message = "Der Benutzername darf nicht leer sein.")
    private String username;

    public class Password {

        @Size(min = 6, max = 64, message = "Das Passwort muss zwischen sechs und 64 Zeichen lang sein.")
        public String password = "";
        
           }
    
    
    //<editor-fold defaultstate="collapsed" desc="Tabellen">
    @Transient
    private final Password password = new Password();

    @Column(name = "VORNMAE", length = 64)
    @Size(min = 5, max = 64, message = "Der Vorname muss zwischen fünf und 64 Zeichen lang sein.")
    private String vorname;

    @Column(name = "NACHNAME", length = 64)
    @Size(min = 5, max = 64, message = "Der Nachname muss zwischen fünf und 64 Zeichen lang sein.")
    private String nachname;

    @Column(name = "ANSCHRIFT", length = 64)
    @Size(min = 5, max = 64, message = "Der Anschrift muss zwischen fünf und 64 Zeichen lang sein.")
    private String anschrift;

    @Column(name = "PLZ", length = 64)
    @Size(min = 5, max = 64, message = "Der PLZ muss zwischen fünf und 64 Zeichen lang sein.")
    private String plz;

    @Column(name = "ORT", length = 64)
    @Size(min = 5, max = 64, message = "Der Ort muss zwischen fünf und 64 Zeichen lang sein.")
    private String ort;

    @Column(name = "EMAIL", length = 64)
    @Size(min = 5, max = 64, message = "Der email muss zwischen fünf und 64 Zeichen lang sein.")
    private String email;

    @Column(name = "TEL", length = 64)
    @Size(min = 5, max = 64, message = "Der Tel muss zwischen fünf und 64 Zeichen lang sein.")
    private String tel;

       

    @Column(name = "PASSWORD_HASH", length = 64)
    @NotNull(message = "Das Passwort darf nicht leer sein.")
    private String passwordHash;
    
    //</editor-fold>

    @ElementCollection
    @CollectionTable(
            name = "MAXIMAKRT_USER_GROUP",
            joinColumns = @JoinColumn(name = "USERNAME")
    )
    @Column(name = "GROUPNAME")
    List<String> groups = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Task> tasks = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public User() {
    }
    
    public User(String username, String password, String vorname, String nachname, String anschrift, String plz, String ort, String email, String tel) {
        this.username = username;
        this.password.password = password;
        this.passwordHash = this.hashPassword(password);
        this.vorname = vorname;
        this.nachname = nachname;
        this.anschrift = anschrift;
        this.plz = plz;
        this.ort = ort;
        this.email = email;
        this.tel = tel;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getVorname() {
        return vorname;
    }

    public String getAnschrift() {
        return anschrift;
    }

    public String getPlz() {
        return plz;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setAnschrift(String anschrift) {
        this.anschrift = anschrift;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getNachname() {
        return nachname;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Passwort setzen und prüfen">
    /**
     * Berechnet der Hash-Wert zu einem Passwort.
     *
     * @param password Passwort
     * @return Hash-Wert
     */
    private String hashPassword(String password) {
        byte[] hash;

        if (password == null) {
            password = "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            hash = "!".getBytes(StandardCharsets.UTF_8);
        }

        BigInteger bigInt = new BigInteger(1, hash);
        return bigInt.toString(16);
    }

    /**
     * Berechnet einen Hashwert aus dem übergebenen Passwort und legt ihn im
     * Feld passwordHash ab. Somit wird das Passwort niemals als Klartext
     * gespeichert.
     *
     * Gleichzeitig wird das Passwort im nicht gespeicherten Feld password
     * abgelegt, um durch die Bean Validation Annotationen überprüft werden zu
     * können.
     *
     * @param password Neues Passwort
     */
    public void setPassword(String password) {
        this.password.password = password;
        this.passwordHash = this.hashPassword(password);
    }

    /**
     * Nur für die Validierung bei einer Passwortänderung!
     *
     * @return Neues, beim Speichern gesetztes Passwort
     */
    public Password getPassword() {
        return this.password;
    }

    /**
     * Prüft, ob das übergebene Passwort korrekt ist.
     *
     * @param password Zu prüfendes Passwort
     * @return true wenn das Passwort stimmt sonst false
     */
    public boolean checkPassword(String password) {
        return this.passwordHash.equals(this.hashPassword(password));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Zuordnung zu Benutzergruppen">
    /**
     * @return Eine unveränderliche Liste aller Benutzergruppen
     */
    public List<String> getGroups() {
        List<String> groupsCopy = new ArrayList<>();

        this.groups.forEach((groupname) -> {
            groupsCopy.add(groupname);
        });

        return groupsCopy;
    }

    /**
     * Fügt den Benutzer einer weiteren Benutzergruppe hinzu.
     *
     * @param groupname Name der Benutzergruppe
     */
    public void addToGroup(String groupname) {
        if (!this.groups.contains(groupname)) {
            this.groups.add(groupname);
        }
    }

    /**
     * Entfernt den Benutzer aus der übergebenen Benutzergruppe.
     *
     * @param groupname Name der Benutzergruppe
     */
    public void removeFromGroup(String groupname) {
        this.groups.remove(groupname);
    }
    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User other = (User)o;
            return this.username.equals(other.username);
        }
        return false;
    }
}
