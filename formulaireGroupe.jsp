<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Pupitre, model.Commission" %>
<%
    Utilisateur admin = (Utilisateur) session.getAttribute("utilisateur");
    if (admin == null || !admin.isEstAdmin()) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    String type = (String) request.getAttribute("type");
    Pupitre pupitre = (Pupitre) request.getAttribute("pupitre");
    Commission commission = (Commission) request.getAttribute("commission");

    boolean modeEdition = (pupitre != null || commission != null);
    String valNom = modeEdition ? (pupitre != null ? pupitre.getNom() : commission.getNom()) : "";
    String typeAffiche = "pupitre".equals(type) ? "Pupitre" : "Commission";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= modeEdition ? "Modifier" : "Ajouter" %> <%= typeAffiche %> - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-sm">
<p><a class="retour" href="admin-groupes">← Retour à la gestion des groupes</a></p>

<h1><%= modeEdition ? "Modifier" : "Ajouter" %> <%= typeAffiche.toLowerCase() %></h1>

<form method="post" action="admin-groupes">
    <input type="hidden" name="action" value="<%= modeEdition ? "modifier" : "ajouter" %>">
    <input type="hidden" name="type" value="<%= type %>">

    <% if (modeEdition) { %>
    <input type="hidden" name="id" value="<%= pupitre != null ? pupitre.getId() : commission.getId() %>">
    <% } %>

    <label for="nom">Nom :</label>
    <input type="text" id="nom" name="nom" value="<%= valNom %>" required autofocus>

    <button type="submit" class="btn btn-primary btn-block">Enregistrer</button>
</form>
</body>
</html>