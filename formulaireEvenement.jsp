<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Evenement, java.time.format.DateTimeFormatter" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    Evenement evenement = (Evenement) request.getAttribute("evenement");
    boolean   modeEdition = (evenement != null);
    String    erreur      = (String) request.getAttribute("erreur");

    // Valeurs pré-remplies (edit) ou vides (ajout)
    String valNom           = modeEdition ? evenement.getNom()           : "";
    String valHorodatage    = modeEdition
        ? evenement.getHorodatage().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    String valDuree         = modeEdition ? String.valueOf(evenement.getDuree()) : "";
    String valLieu          = modeEdition ? evenement.getLieu()          : "";
    String valDescription   = modeEdition ? evenement.getDescription()   : "";
    String valType          = modeEdition ? evenement.getType()          : "";
    String valOrganisateur  = modeEdition ? evenement.getNomOrganisateur() : utilisateur.getNomUtilisateur();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= modeEdition ? "Modifier" : "Ajouter" %> un événement - FanfareHub</title>
    <style>
        body     { font-family: Arial, sans-serif; max-width: 540px; margin: 40px auto; padding: 0 20px; }
        h1       { color: #2c6fad; }
        label    { display: block; margin-top: 14px; font-weight: bold; }
        input, select, textarea { width: 100%; padding: 8px; box-sizing: border-box; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px; }
        textarea { height: 90px; resize: vertical; }
        button   { width: 100%; padding: 11px; margin-top: 22px; background: #2c6fad; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1em; }
        button:hover { background: #1a4f80; }
        .erreur  { color: red; background: #ffe0e0; padding: 10px; border-radius: 4px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
        .aide    { font-size: 0.82em; color: #777; margin-top: 3px; }
    </style>
</head>
<body>
    <p><a class="retour" href="evenements">← Retour aux événements</a></p>

    <h1><%= modeEdition ? "Modifier" : "Ajouter" %> un événement</h1>

    <% if (erreur != null) { %>
        <p class="erreur"><%= erreur %></p>
    <% } %>

    <form method="post" action="evenements">
        <input type="hidden" name="action" value="<%= modeEdition ? "modifier" : "ajouter" %>">
        <% if (modeEdition) { %>
        <input type="hidden" name="id" value="<%= evenement.getIdEvenement() %>">
        <% } %>

        <label for="nom">Nom de l'événement *</label>
        <input type="text" id="nom" name="nom" value="<%= valNom %>" required autofocus>

        <label for="horodatage">Date et heure *</label>
        <input type="datetime-local" id="horodatage" name="horodatage" value="<%= valHorodatage %>" required>

        <label for="duree">Durée (en minutes) *</label>
        <input type="number" id="duree" name="duree" value="<%= valDuree %>" min="1" required>
        <p class="aide">Exemple : 90 pour 1h30</p>

        <label for="lieu">Lieu *</label>
        <input type="text" id="lieu" name="lieu" value="<%= valLieu %>" required>

        <label for="type">Type *</label>
        <select id="type" name="type" required>
            <option value="">-- Choisir --</option>
            <option value="atelier"    <%= valType.equals("atelier")    ? "selected" : "" %>>Atelier</option>
            <option value="répétition" <%= valType.equals("répétition") ? "selected" : "" %>>Répétition</option>
            <option value="prestation" <%= valType.equals("prestation") ? "selected" : "" %>>Prestation</option>
        </select>

        <label for="nomOrganisateur">Organisateur *</label>
        <input type="text" id="nomOrganisateur" name="nomOrganisateur" value="<%= valOrganisateur %>" required>

        <label for="description">Description</label>
        <textarea id="description" name="description"><%= valDescription %></textarea>

        <button type="submit"><%= modeEdition ? "Enregistrer les modifications" : "Ajouter l'événement" %></button>
    </form>
</body>
</html>
