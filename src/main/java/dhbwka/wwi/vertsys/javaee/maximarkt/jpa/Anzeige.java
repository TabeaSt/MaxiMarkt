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
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ta_st
 */
@Entity
public class Anzeige implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "task_ids")
    @TableGenerator(name = "task_ids", initialValue = 0, allocationSize = 50)
    private long id;

//<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public Category getCategory() {
        return category;
    }

    public AnzeigeArt getArt() {
        return art;
    }

    public String getTitel() {
        return titel;
    }

    public String getPreis() {
        return preis;
    }

    public PreisArt getArtDesPreises() {
        return artDesPreises;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setArt(AnzeigeArt art) {
        this.art = art;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    public void setArtDesPreises(PreisArt artDesPreises) {
        this.artDesPreises = artDesPreises;
    }

    //</editor-fold> 
    public Anzeige() {
    }

    public Anzeige(long id, User owner, Category category, String titel, String preis, PreisArt artDesPreises, Date date, String beschreibung, AnzeigeArt art) {
        this.id = id;
        this.owner = owner;
        this.category = category;
        this.art = art;
        this.titel = titel;
        this.preis = preis;
        this.artDesPreises = artDesPreises;
        this.date = date;
        this.beschreibung = beschreibung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @ManyToOne
    @NotNull(message = "Die Aufgabe muss einem Benutzer geordnet werden.")
    private User owner;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigeArt art = AnzeigeArt.BIETE;
    
     @ManyToOne
    private Category category;



    private String titel;

    private String preis;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PreisArt artDesPreises = PreisArt.FESTPREIS;
    
    @NotNull
    private Date date;
    
    private String beschreibung;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
