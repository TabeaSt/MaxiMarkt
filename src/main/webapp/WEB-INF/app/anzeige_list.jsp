<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Übersicht
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
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_title" value="${param.search_title}" placeholder="Titel"/>

            <select name="search_category">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${param.search_category == category.id ? 'selected' : ''}>
                        <c:out value="${category.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_anzeige_art">
                <option value="">Alle Anzeigearten</option>

                <c:forEach items="${anzeige_art}" var="art">
                    <option value="${art}" ${param.search_anzeige_art == art ? 'selected' : ''}>
                        <c:out value="${art.label}"/>
                    </option>
                </c:forEach>
            </select>

            <select name="search_preis_art">
                <option value="">Alle Preisarten</option>

                <c:forEach items="${preis_art}" var="art">
                    <option value="${art}" ${param.search_preis_art == art ? 'selected' : ''}>
                        <c:out value="${art.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Anzeigen --%>
        <c:choose>
            <c:when test="${empty anzeigen}">
                <p>
                    Es wurden keine Anzeigen gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.maximarkt.web.WebUtils"/>

                <table>
                    <thead>
                        <tr>
                            <th>Titel</th>
                            <th>Kategorie</th>
                            <th>Eigentümer</th>
                            <th>Anzeigeart</th>
                            <th>Preisart</th>
                            <th>Preis</th>
                            <th>Datum</th>
                        </tr>
                    </thead>
                    <c:forEach items="${anzeigen}" var="anzeige">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/anzeigen/edit/${anzeige.id}/"/>">
                                    <c:out value="${anzeige.titel}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${anzeige.category.name}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.art.label}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.artDesPreises.label}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.preis}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(anzeige.date)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>