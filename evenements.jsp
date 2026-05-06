<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Evenement, java.util.List, java.time.format.DateTimeFormatter" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    List<Evenement> evenements  = (List<Evenement>) request.getAttribute("evenements");
    boolean         peutGerer   = Boolean.TRUE.equals(request.getAttribute("peutGerer"));
    boolean         succes      = "1".equals(request.getParameter("succes"));
    DateTimeFormatter fmtAff    = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Événements - FanfareHub</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 1000px; margin: 40px auto; padding: 0 20px; }
        h1   { color: #2c6fad; }
        .entete { display: flex; justify-content: space-between; align-items: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #2c6fad; color: white; }
        tr:hover { background: #f0f6ff; }
        .btn-ajouter   { padding: 10px 20px; background: #27ae60; color: white; text-decoration: none; border-radius: 4px; }
        .btn-ajouter:hover { background: #1e8449; }
        .btn-modifier  { padding: 5px 10px; background: #2c6fad; color: white; text-decoration: none; border-radius: 4px; font-size: 0.9em; }
        .btn-modifier:hover { background: #1a4f80; }
        .btn-supprimer { padding: 5px 10px; background: #c0392b; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 0.9em; }
        .btn-supprimer:hover { background: #96281b; }
        .succes { color: #27ae60; background: #eafaf1; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .vide   { color: #888; margin-top: 20px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
        .badge { display: inline-block; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; background: #8e44ad; color: white; }
    </style>
</head>
<body>
    <p><a class="retour" href="accueil.jsp">← Retour à l'accueil</a></p>

    <div class="entete">
        <h1>Événements</h1>
        <% if (peutGerer) { %>
        <a class="btn-ajouter" href="evenements?action=formulaireAjouter">+ Ajouter un événement</a>
        <% } %>
    </div>

    <% if (succes) { %>
        <p class="succes">L'opération a bien été effectuée.</p>
    <% } %>

    <% if (evenements == null || evenements.isEmpty()) { %>
        <p class="vide">Aucun événement prévu pour le moment.</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Date et heure</th>
                    <th>Durée (min)</th>
                    <th>Lieu</th>
                    <th>Type</th>
                    <th>Organisateur</th>
                    <% if (peutGerer) { %><th>Actions</th><% } %>
                </tr>
            </thead>
            <tbody>
                <% for (Evenement e : evenements) { %>
                <tr>
                    <td><strong><a href="evenement?id=<%= e.getIdEvenement() %>" style="color:#2c6fad;"><%= e.getNom() %></a></strong></td>
                    <td><%= e.getHorodatage().format(fmtAff) %></td>
                    <td><%= e.getDuree() %></td>
                    <td><%= e.getLieu() %></td>
                    <td><span class="badge"><%= e.getType() %></span></td>
                    <td><%= e.getNomOrganisateur() %></td>
                    <% if (peutGerer) { %>
                    <td>
                        <a class="btn-modifier"
                           href="evenements?action=formulaireModifier&id=<%= e.getIdEvenement() %>">Modifier</a>
                        &nbsp;
                        <form method="post" action="evenements" style="display:inline;"
                              onsubmit="return confirm('Supprimer « <%= e.getNom() %> » ?');">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id" value="<%= e.getIdEvenement() %>">
                            <button type="submit" class="btn-supprimer">Supprimer</button>
                        </form>
                    </td>
                    <% } %>
                </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</body>
</html>
