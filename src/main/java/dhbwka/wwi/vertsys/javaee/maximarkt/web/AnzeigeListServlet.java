/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.maximarkt.web;

import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.AnzeigeBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Category;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.PreisArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Task;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.TaskStatus;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ta_st
 */
@WebServlet(urlPatterns = {"/app/anzeigen/"})
public class AnzeigeListServlet extends HttpServlet {
    
    @EJB
    private CategoryBean categoryBean;
    
    @EJB
    private AnzeigeBean anzeigeBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("anzeige_art", AnzeigeArt.values());
        request.setAttribute("preis_art", PreisArt.values());

        // Suchparameter aus der URL auslesen
        String searchTitle = request.getParameter("search_title");
        String searchCategory = request.getParameter("search_category");
        String searchAnzeigeArt = request.getParameter("search_anzeige_art");
        String searchPreisArt = request.getParameter("search_preis_art");

        // Anzuzeigende Aufgaben suchen
        Category category = null;
        AnzeigeArt anzeigeArt = null;
        PreisArt preisArt = null;

        if (searchCategory != null) {
            try {
                category = this.categoryBean.findById(Long.parseLong(searchCategory));
            } catch (NumberFormatException ex) {
                category = null;
            }
        }
        
        if (searchAnzeigeArt != null) {
            try {
                anzeigeArt = AnzeigeArt.valueOf(searchAnzeigeArt);
            } catch (IllegalArgumentException ex) {
                anzeigeArt = null;
            }

        }
        
        if (preisArt != null) {
            try {
                preisArt = PreisArt.valueOf(searchPreisArt);
            } catch (IllegalArgumentException ex) {
                preisArt = null;
            }

        }

        List<Anzeige> anzeigen = this.anzeigeBean.search(searchTitle, category, anzeigeArt, preisArt);
        request.setAttribute("anzeigen", anzeigen);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_list.jsp").forward(request, response);
    }
}
