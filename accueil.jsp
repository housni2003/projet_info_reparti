<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Accueil - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-md">
<h1>Bienvenue sur FanfareHub !</h1>

<div class="info">
    <p><strong>Nom d'utilisateur :</strong> <%= utilisateur.getNomUtilisateur() %></p>
    <p><strong>Prénom :</strong> <%= utilisateur.getPrenom() %></p>
    <p><strong>Nom :</strong> <%= utilisateur.getNom() %></p>
    <p><strong>Email :</strong> <%= utilisateur.getEmail() %></p>
    <p><strong>Genre :</strong> <%= utilisateur.getGenre() %></p>
    <p><strong>Contraintes alimentaires :</strong> <%= utilisateur.getContraintesAlimentaires() %></p>
    <p><strong>Inscrit le :</strong> <%= utilisateur.getDateCreation() %></p>
    <p><strong>Dernière connexion :</strong>
        <%= utilisateur.getDerniereConnexion() != null ? utilisateur.getDerniereConnexion() : "Première connexion" %>
    </p>
</div>

<a class="btn btn-purple" style="margin-right: 10px;" href="groupes">Mes groupes et pupitres</a>
<a class="btn btn-warning" style="margin-right: 10px;" href="evenements">Événements</a>
<% if (utilisateur.isEstAdmin()) { %>
<a class="btn btn-success" style="margin-right: 10px;" href="admin">Panneau d'administration</a>
<% } %>
<a class="btn btn-danger" style="margin-top: 15px;" href="deconnexion">Se déconnecter</a>
</body>
</html>