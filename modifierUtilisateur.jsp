<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur" %>
<%
    Utilisateur admin = (Utilisateur) session.getAttribute("utilisateur");
    if (admin == null || !admin.isEstAdmin()) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    Utilisateur cible = (Utilisateur) request.getAttribute("cible");
    if (cible == null) {
        response.sendRedirect(request.getContextPath() + "/admin");
        return;
    }

    String erreur = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier un compte - FanfareHub</title>
    <style>
        body   { font-family: Arial, sans-serif; max-width: 480px; margin: 40px auto; padding: 0 20px; }
        h1     { color: #2c6fad; }
        label  { display: block; margin-top: 12px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; margin-top: 4px; }
        .checkbox-ligne { display: flex; align-items: center; gap: 10px; margin-top: 16px; }
        .checkbox-ligne input { width: auto; margin: 0; }
        .checkbox-ligne label { margin: 0; font-weight: bold; }
        button { width: 100%; padding: 10px; margin-top: 20px; background: #2c6fad; color: white; border: none; cursor: pointer; font-size: 1em; }
        button:hover { background: #1a4f80; }
        .erreur { color: red; background: #ffe0e0; padding: 8px; border-radius: 4px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
        .nom-utilisateur { background: #eef4fb; padding: 8px 12px; border-radius: 4px; margin-top: 8px; font-weight: bold; }
    </style>
</head>
<body>
    <p><a class="retour" href="admin">← Retour à la liste</a></p>

    <h1>Modifier le compte</h1>
    <div class="nom-utilisateur"><%= cible.getNomUtilisateur() %></div>

    <% if (erreur != null) { %>
        <p class="erreur"><%= erreur %></p>
    <% } %>

    <form method="post" action="admin">
        <input type="hidden" name="action" value="modifier">
        <input type="hidden" name="nomUtilisateur" value="<%= cible.getNomUtilisateur() %>">

        <label for="email">Email :</label>
        <input type="email" id="email" name="email" value="<%= cible.getEmail() %>" required>

        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom" value="<%= cible.getPrenom() %>" required>

        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom" value="<%= cible.getNom() %>" required>

        <label for="genre">Genre :</label>
        <select id="genre" name="genre" required>
            <option value="homme" <%= cible.getGenre().equals("homme") ? "selected" : "" %>>Homme</option>
            <option value="femme" <%= cible.getGenre().equals("femme") ? "selected" : "" %>>Femme</option>
            <option value="autre" <%= cible.getGenre().equals("autre") ? "selected" : "" %>>Autre</option>
        </select>

        <label for="contraintes">Contraintes alimentaires :</label>
        <select id="contraintes" name="contraintesAlimentaires">
            <option value="aucune"     <%= cible.getContraintesAlimentaires().equals("aucune")     ? "selected" : "" %>>Aucune</option>
            <option value="végétarien" <%= cible.getContraintesAlimentaires().equals("végétarien") ? "selected" : "" %>>Végétarien</option>
            <option value="vegan"      <%= cible.getContraintesAlimentaires().equals("vegan")      ? "selected" : "" %>>Vegan</option>
            <option value="sans porc"  <%= cible.getContraintesAlimentaires().equals("sans porc")  ? "selected" : "" %>>Sans porc</option>
        </select>

        <div class="checkbox-ligne">
            <input type="checkbox" id="estAdmin" name="estAdmin" <%= cible.isEstAdmin() ? "checked" : "" %>>
            <label for="estAdmin">Administrateur</label>
        </div>

        <button type="submit">Enregistrer les modifications</button>
    </form>
</body>
</html>
