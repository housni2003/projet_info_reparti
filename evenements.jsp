<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Evenement, java.util.List, java.time.format.DateTimeFormatter" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    List<Evenement> evenements  = (List<Evenement>) request.getAttribute("evenements");
    boolean         peutGerer   = Boolean.TRUE.equals(request.getAttribute("peutGerer"));
    boolean         succes      = "1".equals(request.getParameter("succes"));
    DateTimeFormatter fmtAff    = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Événements - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-xl">
<p><a class="retour" href="accueil.jsp">← Retour à l'accueil</a></p>

<div class="entete">
    <h1>Événements</h1>
    <% if (peutGerer) { %>
    <a class="btn btn-success" href="evenements?action=formulaireAjouter">+ Ajouter un événement</a>
    <% } %>
</div>

<% if (succes) { %>
<p class="succes">L'opération a bien été effectuée.</p>
<% } %>

<% if (evenements == null || evenements.isEmpty()) { %>
<p class="vide">Aucun événement prévu pour le moment.</p>
<% } else { %>
<table>
    <thead>
    <tr>
        <th>Nom</th>
        <th>Date et heure</th>
        <th>Durée (min)</th>
        <th>Lieu</th>
        <th>Type</th>
        <th>Organisateur</th>
        <% if (peutGerer) { %><th>Actions</th><% } %>
    </tr>
    </thead>
    <tbody>
    <% for (Evenement e : evenements) { %>
    <tr>
        <td>
            <strong>
                <a href="evenement?id=<%= e.getIdEvenement() %>" style="color:#2c6fad; text-decoration: none;">
                    <%= e.getNom() %>
                </a>
            </strong>
        </td>
        <td><%= e.getHorodatage().format(fmtAff) %></td>
        <td><%= e.getDuree() %></td>
        <td><%= e.getLieu() %></td>
        <td><span class="badge badge-purple"><%= e.getType() %></span></td>
        <td><%= e.getNomOrganisateur() %></td>
        <% if (peutGerer) { %>
        <td>
            <a class="btn btn-primary btn-sm"
               href="evenements?action=formulaireModifier&id=<%= e.getIdEvenement() %>">Modifier</a>
            &nbsp;
            <form method="post" action="evenements" style="display:inline;"
                  onsubmit="return confirm('Supprimer « <%= e.getNom() %> » ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="id" value="<%= e.getIdEvenement() %>">
                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
            </form>
        </td>
        <% } %>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
</body>
</html>