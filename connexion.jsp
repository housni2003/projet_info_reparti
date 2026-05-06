<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-sm">
<h1 style="text-align: center;">Connexion à FanfareHub</h1>

<%
    String erreur = (String) request.getAttribute("erreur");
    String succes = (String) request.getAttribute("succes");
%>

<% if (erreur != null) { %><p class="erreur"><%= erreur %></p><% } %>
<% if (succes != null) { %><p class="succes"><%= succes %></p><% } %>

<form action="connexion" method="post">
    <label for="nomUtilisateur">Nom d'utilisateur :</label>
    <input type="text" id="nomUtilisateur" name="nomUtilisateur" value="<%= request.getAttribute("nomUtilisateur") != null ? request.getAttribute("nomUtilisateur") : "" %>" required autofocus>

    <label for="motDePasse">Mot de passe :</label>
    <input type="password" id="motDePasse" name="motDePasse" required>

    <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
</form>

<p class="lien">Pas encore de compte ? <a href="inscription.jsp">S'inscrire</a></p>
</body>
</html>