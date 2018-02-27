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

import dhbwka.wwi.vertsys.javaee.maximarkt.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.Task;
import dhbwka.wwi.vertsys.javaee.maximarkt.jpa.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
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
@WebServlet(urlPatterns = "/app/user/edit")
public class UserEditServlet extends HttpServlet {

    @EJB
    UserBean userbean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = this.userbean.getCurrentUser();
        request.setAttribute("edit", user != null);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();

        if (session.getAttribute("user_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("user_form", this.createUserForm(user));
        }

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/app/user_edit.jsp");
        dispatcher.forward(request, response);
        session.removeAttribute("user_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        User user = this.userbean.getCurrentUser();

        String userName = request.getParameter("user_name");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String locale = request.getParameter("locale");
        String location = request.getParameter("location");
        String postalCode = request.getParameter("postal_code");
        String email = request.getParameter("email");
        String tel = request.getParameter("tel");

        if (userName != null && !userName.isEmpty()) {
            user.setUsername(userName);
        } else {
            errors.add("Benutzername darf nicht leer sein.");
        }
        if (firstName != null && !firstName.isEmpty()) {
            user.setVorname(firstName);
        } else {
            errors.add("Vorname darf nicht leer sein.");
        }
        if (lastName != null && !lastName.isEmpty()) {
            user.setNachname(lastName);
        } else {
            errors.add("Nachname darf nicht leer sein.");
        }
        if (locale != null && !locale.isEmpty()) {
            user.setAnschrift(locale);
        } else {
            errors.add("Anschrift darf nicht leer sein.");
        }
        if (location != null && !location.isEmpty()) {
            user.setOrt(location);
        } else {
            errors.add("Ort darf nicht leer sein.");
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            user.setPlz(postalCode);
        } else {
            errors.add("Postleitzahl darf nicht leer sein.");
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        } else {
            errors.add("Email darf nicht leer sein.");
        }
        if (tel != null && !tel.isEmpty()) {
            user.setTel(tel);
        } else {
            errors.add("Telefonnummer darf nicht leer sein.");
        }
        
        if (errors.isEmpty()) {
            this.userbean.update(user);
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("user_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    private FormValues createUserForm(User user) {
        Map<String, String[]> values = new HashMap<>();

        values.put("user_name", new String[]{
            user.getUsername()
        });

        values.put("first_name", new String[]{
            user.getVorname()
        });

        values.put("last_name", new String[]{
            user.getNachname()
        });

        values.put("locale", new String[]{
            user.getAnschrift()
        });

        values.put("location", new String[]{
            user.getOrt()
        });

        values.put("postal_code", new String[]{
            user.getPlz()
        });

        values.put("email", new String[]{
            user.getEmail()
        });

        values.put("tel", new String[]{
            user.getTel()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
