<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Pupitre, model.Commission, java.util.List" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    List<Pupitre>    tousLesPupitres      = (List<Pupitre>)    request.getAttribute("tousLesPupitres");
    List<Commission> toutesLesCommissions = (List<Commission>) request.getAttribute("toutesLesCommissions");
    List<Integer>    pupitresChoisis      = (List<Integer>)    request.getAttribute("pupitresChoisis");
    List<Integer>    commissionsChoisies  = (List<Integer>)    request.getAttribute("commissionsChoisies");
    String           erreur               = (String)           request.getAttribute("erreur");
    boolean          succes               = "1".equals(request.getParameter("succes"));
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Mes groupes - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-md">
<p><a class="retour" href="accueil.jsp">← Retour à l'accueil</a></p>

<h1>Mes groupes et pupitres</h1>

<% if (erreur != null) { %>
<p class="erreur"><%= erreur %></p>
<% } %>
<% if (succes) { %>
<p class="succes">Vos choix ont bien été enregistrés.</p>
<% } %>

<form method="post" action="groupes">

    <h2>Pupitres</h2>
    <div class="grille">
        <% if (tousLesPupitres != null) {
            for (Pupitre p : tousLesPupitres) {
                boolean coche = pupitresChoisis != null && pupitresChoisis.contains(p.getId());
        %>
        <label class="case">
            <input type="checkbox" name="pupitres" value="<%= p.getId() %>" <%= coche ? "checked" : "" %>>
            <%= p.getNom() %>
        </label>
        <% } } %>
    </div>

    <h2>Commissions</h2>
    <div class="grille">
        <% if (toutesLesCommissions != null) {
            for (Commission c : toutesLesCommissions) {
                boolean coche = commissionsChoisies != null && commissionsChoisies.contains(c.getId());
        %>
        <label class="case">
            <input type="checkbox" name="commissions" value="<%= c.getId() %>" <%= coche ? "checked" : "" %>>
            <%= c.getNom() %>
        </label>
        <% } } %>
    </div>

    <button type="submit" class="btn btn-primary" style="margin-top: 30px; padding: 12px 30px;">Enregistrer mes choix</button>
</form>
</body>
</html>