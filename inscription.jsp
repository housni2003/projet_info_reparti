<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription - FanfareHub</title>
    <style>
        body   { font-family: Arial, sans-serif; max-width: 480px; margin: 40px auto; }
        h1     { text-align: center; }
        label  { display: block; margin-top: 12px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; margin-top: 4px; }
        button { width: 100%; padding: 10px; margin-top: 20px; background: #2c6fad; color: white; border: none; cursor: pointer; font-size: 1em; }
        button:hover { background: #1a4f80; }
        .erreur { color: red; background: #ffe0e0; padding: 8px; border-radius: 4px; }
        .lien   { text-align: center; margin-top: 15px; }
    </style>
</head>
<body>
    <h1>Inscription à FanfareHub</h1>

    <%
        String erreur      = (String) request.getAttribute("erreur");
        String nomUtil     = request.getAttribute("nomUtilisateur") != null ? (String) request.getAttribute("nomUtilisateur") : "";
        String emailVal    = request.getAttribute("email")          != null ? (String) request.getAttribute("email")          : "";
        String prenomVal   = request.getAttribute("prenom")         != null ? (String) request.getAttribute("prenom")         : "";
        String nomVal      = request.getAttribute("nom")            != null ? (String) request.getAttribute("nom")            : "";
        String genreVal    = request.getAttribute("genre")          != null ? (String) request.getAttribute("genre")          : "";
        String contraintes = request.getAttribute("contraintesAlimentaires") != null
                             ? (String) request.getAttribute("contraintesAlimentaires") : "aucune";
    %>

    <% if (erreur != null) { %>
        <p class="erreur"><%= erreur %></p>
    <% } %>

    <form action="inscription" method="post">

        <label for="nomUtilisateur">Nom d'utilisateur :</label>
        <input type="text" id="nomUtilisateur" name="nomUtilisateur" value="<%= nomUtil %>" required autofocus>

        <label for="email">Adresse email :</label>
        <input type="email" id="email" name="email" value="<%= emailVal %>" required>

        <label for="confirmEmail">Confirmer l'adresse email :</label>
        <input type="email" id="confirmEmail" name="confirmEmail" required>

        <label for="motDePasse">Mot de passe :</label>
        <input type="password" id="motDePasse" name="motDePasse" required>

        <label for="confirmMotDePasse">Confirmer le mot de passe :</label>
        <input type="password" id="confirmMotDePasse" name="confirmMotDePasse" required>

        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom" value="<%= prenomVal %>" required>

        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom" value="<%= nomVal %>" required>

        <label for="genre">Genre :</label>
        <select id="genre" name="genre" required>
            <option value="">-- Choisir --</option>
            <option value="homme"  <%= genreVal.equals("homme")  ? "selected" : "" %>>Homme</option>
            <option value="femme"  <%= genreVal.equals("femme")  ? "selected" : "" %>>Femme</option>
            <option value="autre"  <%= genreVal.equals("autre")  ? "selected" : "" %>>Autre</option>
        </select>

        <label for="contraintes">Contraintes alimentaires :</label>
        <select id="contraintes" name="contraintesAlimentaires">
            <option value="aucune"     <%= contraintes.equals("aucune")     ? "selected" : "" %>>Aucune</option>
            <option value="végétarien" <%= contraintes.equals("végétarien") ? "selected" : "" %>>Végétarien</option>
            <option value="vegan"      <%= contraintes.equals("vegan")      ? "selected" : "" %>>Vegan</option>
            <option value="sans porc"  <%= contraintes.equals("sans porc")  ? "selected" : "" %>>Sans porc</option>
        </select>

        <button type="submit">S'inscrire</button>
    </form>

    <p class="lien">Déjà inscrit ? <a href="connexion.jsp">Se connecter</a></p>
</body>
</html>
