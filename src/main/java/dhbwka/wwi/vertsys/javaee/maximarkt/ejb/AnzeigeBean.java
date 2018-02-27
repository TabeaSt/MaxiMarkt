/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.maximarkt.ejb;

import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Category;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.PreisArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Task;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.TaskStatus;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ta_st
 */
@Stateless
@RolesAllowed("maximarkt-app-user")
public class AnzeigeBean extends EntityBean<Anzeige, Long> {
    
    public AnzeigeBean() {
        super(Anzeige.class);
    }
    
    @Override
    public List<Anzeige> findAll() {
        return em.createQuery("SELECT a FROM Anzeige a ORDER BY a.date")
                 .getResultList();
    }
    
    public List<Anzeige> search(String title, Category category, AnzeigeArt anzeigeArt, PreisArt preisArt) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT a FROM Anzeige a
        CriteriaQuery<Anzeige> query = cb.createQuery(Anzeige.class);
        Root<Anzeige> from = query.from(Anzeige.class);
        query.select(from);

        // ORDER BY date
        query.orderBy(cb.asc(from.get("date")));
        
        // WHERE a.titel LIKE :title
        if (title != null && !title.trim().isEmpty()) {
            query.where(cb.like(from.get("titel"), "%" + title + "%"));
        }
        
        // WHERE a.category = :category
        if (category != null) {
            query.where(cb.equal(from.get("category"), category));
        }
        
        // WHERE a.art = :art
        if (anzeigeArt != null) {
            query.where(cb.equal(from.get("art"), anzeigeArt));
        }
        
        // WHERE a.artDesPreises = :artDesPreises
        if (preisArt != null) {
            query.where(cb.equal(from.get("artDesPreises"), preisArt));
        }
        
        return em.createQuery(query).getResultList();
    }
}
