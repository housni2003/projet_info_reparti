<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion - FanfareHub</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 60px auto; }
        h1   { text-align: center; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { width: 100%; padding: 8px; box-sizing: border-box; margin-top: 4px; }
        button { width: 100%; padding: 10px; margin-top: 20px; background: #2c6fad; color: white; border: none; cursor: pointer; font-size: 1em; }
        button:hover { background: #1a4f80; }
        .erreur { color: red; background: #ffe0e0; padding: 8px; border-radius: 4px; }
        .lien   { text-align: center; margin-top: 15px; }
    </style>
</head>
<body>
    <h1>Connexion à FanfareHub</h1>

    <%
        String erreur = (String) request.getAttribute("erreur");
        String succes = (String) request.getAttribute("succes");
    %>

    <% if (erreur != null) { %>
        <p class="erreur"><%= erreur %></p>
    <% } %>
    <% if (succes != null) { %>
        <p style="color:green;"><%= succes %></p>
    <% } %>

    <form action="connexion" method="post">
        <label for="nomUtilisateur">Nom d'utilisateur :</label>
        <input type="text" id="nomUtilisateur" name="nomUtilisateur"
               value="<%= request.getAttribute("nomUtilisateur") != null ? request.getAttribute("nomUtilisateur") : "" %>"
               required autofocus>

        <label for="motDePasse">Mot de passe :</label>
        <input type="password" id="motDePasse" name="motDePasse" required>

        <button type="submit">Se connecter</button>
    </form>

    <p class="lien">Pas encore de compte ? <a href="inscription.jsp">S'inscrire</a></p>
</body>
</html>
