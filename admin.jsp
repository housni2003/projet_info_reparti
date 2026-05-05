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
    <style>
        body { font-family: Arial, sans-serif; max-width: 1000px; margin: 40px auto; padding: 0 20px; }
        h1   { color: #2c6fad; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #2c6fad; color: white; }
        tr:hover { background: #f0f6ff; }
        .badge-admin  { background: #27ae60; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.85em; }
        .badge-membre { background: #95a5a6; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.85em; }
        .btn-modifier  { padding: 5px 12px; background: #2c6fad; color: white; text-decoration: none; border-radius: 4px; font-size: 0.9em; }
        .btn-modifier:hover { background: #1a4f80; }
        .btn-supprimer { padding: 5px 12px; background: #c0392b; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 0.9em; }
        .btn-supprimer:hover { background: #96281b; }
        .erreur { color: red; background: #ffe0e0; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <p><a class="retour" href="accueil.jsp">← Retour à l'accueil</a></p>

    <h1>Administration des comptes</h1>

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
                            <span class="badge-admin">Admin</span>
                        <% } else { %>
                            <span class="badge-membre">Membre</span>
                        <% } %>
                    </td>
                    <td>
                        <a class="btn-modifier"
                           href="admin?action=formulaireModifier&nomUtilisateur=<%= u.getNomUtilisateur() %>">
                            Modifier
                        </a>
                        &nbsp;
                        <% if (!u.getNomUtilisateur().equals(admin.getNomUtilisateur())) { %>
                        <form method="post" action="admin" style="display:inline;"
                              onsubmit="return confirm('Supprimer le compte de <%= u.getNomUtilisateur() %> ?');">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="nomUtilisateur" value="<%= u.getNomUtilisateur() %>">
                            <button type="submit" class="btn-supprimer">Supprimer</button>
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
