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
import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.TaskBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.PreisArt;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Task;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.User;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ta_st
 */
@WebServlet(urlPatterns = {"/app/anzeigen/edit/*"})
public class AnzeigeEditServlet extends HttpServlet {

    @EJB
    AnzeigeBean anzeigeBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("anzeige_art", AnzeigeArt.values());
        request.setAttribute("preis_art", PreisArt.values());

        HttpSession session = request.getSession();
        
        User user = this.userBean.getCurrentUser();

        Anzeige anzeige = this.getRequestedAnzeige(request);
        if (anzeige.getCategory() != null) {
            request.setAttribute("category_name", anzeige.getCategory().getName());
        }
        else {
            request.setAttribute("category_name", "Keiner Kategorie zugeordnet");
        }
        
        if (anzeige.getOwner().equals(user)) {
            System.err.println(anzeige.getOwner().getUsername());
            System.err.println(user.getUsername());
            if (anzeige.getId() != 0) {
                request.setAttribute("edit", "bearbeiten");
            }
            else {
                request.setAttribute("edit", "anlegen");
            }
        }
        else {
            request.setAttribute("edit", "ansehen");
        }
        
        if (session.getAttribute("anzeige_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("anzeige_form", this.createAnzeigeForm(anzeige));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_edit.jsp").forward(request, response);

        session.removeAttribute("anzeige_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if (action != null) {
            this.saveAnzeige(request, response);
        }

    }

    private void saveAnzeige(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        Anzeige anzeige = this.getRequestedAnzeige(request);
        
        String anzeigeKategorie = request.getParameter("anzeige_category");
        String anzeigeArt = request.getParameter("anzeige_category");
        String anzeigeTitel = request.getParameter("anzeige_titel");
        String anzeigeBeschreibung = request.getParameter("anzeige_beschreibung");
        String anzeigePreisArt = request.getParameter("anzeige_preis_art");
        String anzeigePreis = request.getParameter("anzeige_preis");
        
        AnzeigeArt anzeigeArtValue = null;
        PreisArt anzeigePreisValue = null;
        
        if (anzeigeKategorie != null && !anzeigeKategorie.trim().isEmpty()) {
            try {
                anzeige.setCategory(this.categoryBean.findById(Long.parseLong(anzeigeKategorie)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }
        
        if (anzeigeArt != null) {
            try {
                anzeigePreisValue = PreisArt.valueOf(anzeigeArt);
            } catch (IllegalArgumentException ex) {
                anzeigePreisValue = null;
            }
        }
        
        if (anzeigePreisArt != null) {
            try {
                anzeigeArtValue = AnzeigeArt.valueOf(anzeigePreisArt);
            } catch (IllegalArgumentException ex) {
                anzeigeArtValue = null;
            }
        }
        
        if (anzeigeTitel != null && !anzeigeTitel.trim().isEmpty()) {
            anzeige.setTitel(anzeigeTitel);
        }
        else {
            errors.add("Titel darf nicht leer sein.");
        }
        
        if (anzeigeBeschreibung != null && !anzeigeBeschreibung.trim().isEmpty()) {
            anzeige.setBeschreibung(anzeigeBeschreibung);
        }
        else {
            errors.add("Beschreibung darf nicht leer sein.");
        }
        
        if (anzeigePreis != null && !anzeigePreis.trim().isEmpty()) {
            anzeige.setPreis(anzeigePreis);
        }
        else {
            errors.add("Preis darf nicht leer sein.");
        }
        
        this.validationBean.validate(anzeige, errors);
        
        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            this.anzeigeBean.update(anzeige);
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeigen/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("anzeige_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    private Anzeige getRequestedAnzeige(HttpServletRequest request) {
        Anzeige anzeige = new Anzeige();
        anzeige.setOwner(this.userBean.getCurrentUser());
        anzeige.setDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String anzeigeId = request.getPathInfo();
        if (anzeigeId == null) {
            anzeigeId = "";
        }
        if (anzeigeId.length() != 0) {
            anzeigeId = anzeigeId.substring(1);
        }

        if (anzeigeId.endsWith("/")) {
            anzeigeId = anzeigeId.substring(0, anzeigeId.length() - 1);
        }

        try {
            anzeige = this.anzeigeBean.findById(Long.parseLong(anzeigeId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return anzeige;
    }

    private FormValues createAnzeigeForm(Anzeige anzeige) {
        Map<String, String[]> values = new HashMap<>();

        if (anzeige.getCategory() != null) {
            values.put("anzeige_category", new String[]{
                anzeige.getCategory().toString()
            });
        }

        values.put("anzeige_art", new String[]{
            anzeige.getArt().toString()
        });

        values.put("anzeige_titel", new String[]{
            anzeige.getTitel()
        });

        values.put("anzeige_beschreibung", new String[]{
            anzeige.getBeschreibung()
        });

        values.put("anzeige_preis_art", new String[]{
            anzeige.getArtDesPreises().toString()
        });

        values.put("anzeige_preis", new String[]{
            anzeige.getPreis()
        });

        values.put("anzeige_date", new String[]{
            WebUtils.formatDate(anzeige.getDate())
        });

        values.put("user_first_name", new String[]{
            anzeige.getOwner().getVorname()
        });

        values.put("user_last_name", new String[]{
            anzeige.getOwner().getNachname()
        });

        values.put("user_locale", new String[]{
            anzeige.getOwner().getAnschrift()
        });

        values.put("user_location", new String[]{
            anzeige.getOwner().getOrt()
        });

        values.put("user_postal_code", new String[]{
            anzeige.getOwner().getPlz()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }
}
