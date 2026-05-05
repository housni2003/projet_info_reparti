<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur" %>
<%
    // Protection de la page : rediriger si non connecté
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
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 60px auto; }
        h1   { color: #2c6fad; }
        .info { background: #eef4fb; padding: 15px; border-radius: 6px; margin-top: 20px; }
        .info p { margin: 6px 0; }
        a.admin { display: inline-block; margin-top: 25px; padding: 10px 20px;
                  background: #27ae60; color: white; text-decoration: none; border-radius: 4px; margin-right: 10px; }
        a.admin:hover { background: #1e8449; }
        a.deconnexion { display: inline-block; margin-top: 25px; padding: 10px 20px;
                        background: #c0392b; color: white; text-decoration: none; border-radius: 4px; }
        a.deconnexion:hover { background: #96281b; }
    </style>
</head>
<body>
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

    <% if (utilisateur.isEstAdmin()) { %>
    <a class="admin" href="admin">Panneau d'administration</a>
    <% } %>
    <a class="deconnexion" href="deconnexion">Se déconnecter</a>
</body>
</html>
