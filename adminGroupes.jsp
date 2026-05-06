<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Pupitre, model.Commission, java.util.List" %>
<%
    Utilisateur admin = (Utilisateur) session.getAttribute("utilisateur");
    if (admin == null || !admin.isEstAdmin()) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    List<Pupitre> pupitres = (List<Pupitre>) request.getAttribute("pupitres");
    List<Commission> commissions = (List<Commission>) request.getAttribute("commissions");
    boolean succes = "1".equals(request.getParameter("succes"));
    boolean erreur = "1".equals(request.getParameter("erreur"));
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Administration des groupes - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-xl">
<p><a class="retour" href="admin">← Retour à l'administration des comptes</a></p>

<h1>Gérer les Pupitres et Commissions</h1>

<% if (succes) { %><p class="succes">L'opération a été réalisée avec succès.</p><% } %>
<% if (erreur) { %><p class="erreur">Une erreur est survenue lors de l'opération.</p><% } %>

<div class="entete">
    <h2>Pupitres</h2>
    <a href="admin-groupes?action=formAjout&type=pupitre" class="btn btn-success btn-sm">+ Ajouter un pupitre</a>
</div>

<table>
    <thead>
    <tr><th>Nom du pupitre</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <% if(pupitres != null) { for(Pupitre p : pupitres) { %>
    <tr>
        <td><%= p.getNom() %></td>
        <td>
            <a class="btn btn-primary btn-sm" href="admin-groupes?action=formModif&type=pupitre&id=<%= p.getId() %>">Modifier</a>
            <form method="post" action="admin-groupes" style="display:inline;" onsubmit="return confirm('Supprimer ce pupitre ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="type" value="pupitre">
                <input type="hidden" name="id" value="<%= p.getId() %>">
                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
            </form>
        </td>
    </tr>
    <% } } %>
    </tbody>
</table>

<div class="entete">
    <h2>Commissions</h2>
    <a href="admin-groupes?action=formAjout&type=commission" class="btn btn-success btn-sm">+ Ajouter une commission</a>
</div>

<table>
    <thead>
    <tr><th>Nom de la commission</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <% if(commissions != null) { for(Commission c : commissions) { %>
    <tr>
        <td><%= c.getNom() %></td>
        <td>
            <a class="btn btn-primary btn-sm" href="admin-groupes?action=formModif&type=commission&id=<%= c.getId() %>">Modifier</a>
            <form method="post" action="admin-groupes" style="display:inline;" onsubmit="return confirm('Supprimer cette commission ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="type" value="commission">
                <input type="hidden" name="id" value="<%= c.getId() %>">
                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
            </form>
        </td>
    </tr>
    <% } } %>
    </tbody>
</table>
</body>
</html>