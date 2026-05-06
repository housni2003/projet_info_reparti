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
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 40px auto; padding: 0 20px; }
        h1   { color: #2c6fad; }
        h2   { color: #444; margin-top: 30px; border-bottom: 2px solid #2c6fad; padding-bottom: 6px; }
        .grille { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-top: 12px; }
        .case { display: flex; align-items: center; gap: 8px; padding: 8px 12px;
                border: 1px solid #ddd; border-radius: 6px; cursor: pointer; }
        .case:hover { background: #eef4fb; }
        .case input[type="checkbox"] { width: 18px; height: 18px; cursor: pointer; accent-color: #2c6fad; }
        button { padding: 12px 30px; margin-top: 30px; background: #2c6fad; color: white;
                 border: none; border-radius: 4px; cursor: pointer; font-size: 1em; }
        button:hover { background: #1a4f80; }
        .erreur { color: red; background: #ffe0e0; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .succes { color: #27ae60; background: #eafaf1; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
    </style>
</head>
<body>
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

        <button type="submit">Enregistrer mes choix</button>
    </form>
</body>
</html>
