<%-- 
    Document   : anzeige_edit
    Created on : 27.02.2018, 20:13:09
    Author     : ta_st
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit.equals('bearbeiten')}">
                Anzeige bearbeiten
            </c:when>
                <c:when test="${edit.equals('anlegen')}">
                Anzeige anlegen
            </c:when>
            <c:otherwise>
                Anzeige ansehen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeigen/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <%-- CSRF-Token --%>
            <input type="hidden" name="csrf_token" value="${csrf_token}">
            <label for="anzeige_category">Kategorie:</label>
            <div class="side-by-side">
                <select name="anzeige_category">
                    <option value="">Keine Kategorie</option>

                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}" ${task_form.values["anzeige_category"][0] == category.id ? 'selected' : ''}>
                            <c:out value="${category.name}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <label for="anzeige_art">
                Anzeigeart
                <span class="required">*</span>
            </label>
            <div class="side-by-side margin">
                <select name="anzeige_art">
                    <c:forEach items="${anzeige_art}" var="art">
                        <option value="${art}" ${anzeige_form.values["anzeige_art"][0] == status ? 'selected' : ''}>
                            <c:out value="${art.label}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <label for="anzeige_titel">Titel<span class="required">*</span></label>
            <div class="side-by-side">
                <input type="text" name="anzeige_titel" value="${anzeige_form.values["anzeige_titel"][0]}">
            </div>

            <label for="anzeige_beschreibung">
                Beschreibung:
            </label>
            <div class="side-by-side">
                <textarea name="anzeige_beschreibung"><c:out value="${anzeige_form.values['anzeige_beschreibung'][0]}"/></textarea>
            </div>

            <label for="anzeige_preis_art">
                Preisart:
                <span class="required">*</span>
            </label>
            <div class="side-by-side margin">
                <select name="anzeige_preis_art">
                    <c:forEach items="${preis_art}" var="art">
                        <option value="${art}" ${anzeige_form.values["anzeige_preis_art"][0] == status ? 'selected' : ''}>
                            <c:out value="${art.label}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <label for="anzeige_preis">Preis<span class="required">*</span></label>
            <div class="side-by-side">
                <input type="text" name="anzeige_preis" value="${anzeige_form.values["anzeige_preis"][0]}">
            </div>

            <button class="icon-pencil" type="submit" name="action" value="save">
                Sichern
            </button>
            
            <%-- Fehlermeldungen --%>
            <c:if test="${!empty anzeige_form.errors}">
                <ul class="errors">
                    <c:forEach items="${anzeige_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
            
        </form>

        <h3>Angelegt am:</h3>
        <p>${anzeige_form.values["anzeige_date"][0]}</p>

        <h3>Anbieter</h3>
        <p>
            ${anzeige_form.values["user_first_name"][0]} ${anzeige_form.values["user_last_name"][0]}<br>
            ${anzeige_form.values["user_locale"][0]}<br>
            ${anzeige_form.values["user_postal_code"][0]} ${anzeige_form.values["user_location"][0]}
        </p>
    </jsp:attribute>
</template:base>