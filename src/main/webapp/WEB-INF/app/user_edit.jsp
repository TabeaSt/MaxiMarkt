<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        User bearbeiten
    </jsp:attribute>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeigen/edit/"/>">Anzeige anlegen</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/app/user/edit"/>">Benutzerkonto bearbeiten</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/categories/"/>">Kategorien bearbeiten</a>
        </div>
    </jsp:attribute>
    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <%-- CSRF-Token --%>
            <input type="hidden" name="csrf_token" value="${csrf_token}">

            <label for="user_name">Benutzername:</label>
            <div class="side-by-side">
                <input type="text" name="user_name" value="${user_form.values["user_name"][0]}">
            </div>
            <label for="first_name">Vorname:</label>
            <div class="side-by-side">
                <input type="text" name="first_name" value="${user_form.values["first_name"][0]}">
            </div>
            <label for="last_name">Nachname:</label>
            <div class="side-by-side">
                <input type="text" name="last_name" value="${user_form.values["last_name"][0]}">
            </div>
            <label for="locale">Anschrift:</label>
            <div class="side-by-side">
                <input type="text" name="locale" value="${user_form.values["locale"][0]}">
            </div>
            <label for="location">Ort:</label>
            <div class="side-by-side">
                <input type="text" name="location" value="${user_form.values["location"][0]}">
            </div>
            <label for="postal_code">PLZ:</label>
            <div class="side-by-side">
                <input type="text" name="postal_code" value="${user_form.values["postal_code"][0]}">
            </div>
            <label for="email">E-Mail:</label>
            <div class="side-by-side">
                <input type="text" name="email" value="${user_form.values["email"][0]}">
            </div>
            <label for="tel">Telefon:</label>
            <div class="side-by-side">
                <input type="text" name="tel" value="${user_form.values["tel"][0]}">
            </div>
            <%-- Button zum Abschicken --%>
            <div class="side-by-side">
                <button class="icon-pencil" type="submit" name="action" value="save">
                    Sichern
                </button>
            </div>
            <%-- Fehlermeldungen --%>
            <c:if test="${!empty user_form.errors}">
                <ul class="errors">
                    <c:forEach items="${user_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>