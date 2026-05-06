<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, java.util.List" %>
<%
    Utilisateur admin = (Utilisateur) session.getAttribute("utilisateur");
    if (admin == null || !admin.isEstAdmin()) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    List<Utilisateur> utilisateurs = (List<Utilisateur>) request.getAttribute("utilisateurs");
    String erreur = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Administration - FanfareHub</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-xl">
<p><a class="retour" href="accueil.jsp">← Retour à l'accueil</a></p>

<h1>Administration des comptes</h1>
<a class="btn btn-purple" style="margin-bottom: 20px;" href="admin-groupes">Gérer les pupitres et commissions</a>

<% if (erreur != null) { %>
<p class="erreur"><%= erreur %></p>
<% } %>

<% if (utilisateurs == null || utilisateurs.isEmpty()) { %>
<p>Aucun utilisateur trouvé.</p>
<% } else { %>
<table>
    <thead>
    <tr>
        <th>Nom d'utilisateur</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th>Email</th>
        <th>Genre</th>
        <th>Rôle</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% for (Utilisateur u : utilisateurs) { %>
    <tr>
        <td><%= u.getNomUtilisateur() %></td>
        <td><%= u.getPrenom() %></td>
        <td><%= u.getNom() %></td>
        <td><%= u.getEmail() %></td>
        <td><%= u.getGenre() %></td>
        <td>
            <% if (u.isEstAdmin()) { %>
            <span class="badge badge-admin">Admin</span>
            <% } else { %>
            <span class="badge badge-membre">Membre</span>
            <% } %>
        </td>
        <td>
            <a class="btn btn-primary btn-sm"
               href="admin?action=formulaireModifier&nomUtilisateur=<%= u.getNomUtilisateur() %>">
                Modifier
            </a>
            &nbsp;
            <% if (!u.getNomUtilisateur().equals(admin.getNomUtilisateur())) { %>
            <form method="post" action="admin" style="display:inline;"
                  onsubmit="return confirm('Supprimer le compte de <%= u.getNomUtilisateur() %> ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="nomUtilisateur" value="<%= u.getNomUtilisateur() %>">
                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
            </form>
            <% } else { %>
            <em style="color:#999; font-size:0.85em;">(votre compte)</em>
            <% } %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
</body>
</html>