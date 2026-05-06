<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Evenement, model.Inscription, java.util.List, java.time.format.DateTimeFormatter" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    Evenement           evenement        = (Evenement)    request.getAttribute("evenement");
    Inscription         monInscription   = (Inscription)  request.getAttribute("monInscription");
    List<Inscription>   inscriptions     = (List<Inscription>) request.getAttribute("inscriptions");
    String              erreur           = (String)       request.getAttribute("erreur");
    boolean             succes           = "1".equals(request.getParameter("succes"));

    String valInstrument = monInscription != null && monInscription.getInstrument() != null ? monInscription.getInstrument() : "";
    String valStatut     = monInscription != null && monInscription.getStatut()     != null ? monInscription.getStatut()     : "";

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= evenement != null ? evenement.getNom() : "Événement" %> - FanfareHub</title>
    <style>
        body  { font-family: Arial, sans-serif; max-width: 800px; margin: 40px auto; padding: 0 20px; }
        h1, h2 { color: #2c6fad; }
        .fiche { background: #eef4fb; padding: 18px 22px; border-radius: 6px; margin-bottom: 28px; }
        .fiche p { margin: 6px 0; }
        .badge { display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 0.85em; color: white; background: #8e44ad; }
        .formulaire { background: #f9f9f9; border: 1px solid #ddd; padding: 18px 22px; border-radius: 6px; margin-bottom: 28px; }
        .formulaire h2 { margin-top: 0; }
        .form-group { margin-bottom: 14px; }
        label { display: block; font-weight: bold; margin-bottom: 5px; }
        select, input[type=text] { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 0.95em; }
        .statut-group { display: flex; gap: 12px; flex-wrap: wrap; }
        .statut-option input { display: none; }
        .statut-option label {
            display: block; padding: 10px 22px; border-radius: 6px; cursor: pointer;
            font-weight: bold; border: 2px solid #ccc; color: #555; background: #fff;
        }
        .statut-option input[value="présent"]:checked  + label { border-color: #27ae60; background: #eafaf1; color: #27ae60; }
        .statut-option input[value="absent"]:checked   + label { border-color: #c0392b; background: #fdedec; color: #c0392b; }
        .statut-option input[value="incertain"]:checked + label { border-color: #f39c12; background: #fef9e7; color: #f39c12; }
        .btn-submit { padding: 10px 28px; background: #2c6fad; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1em; }
        .btn-submit:hover { background: #1a4f80; }
        .succes { color: #27ae60; background: #eafaf1; padding: 10px; border-radius: 4px; margin-bottom: 16px; }
        .erreur { color: #c0392b; background: #fdedec; padding: 10px; border-radius: 4px; margin-bottom: 16px; }
        a.retour { color: #2c6fad; text-decoration: none; }
        a.retour:hover { text-decoration: underline; }
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { padding: 9px 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #2c6fad; color: white; }
        tr:hover { background: #f0f6ff; }
        .s-present  { color: #27ae60; font-weight: bold; }
        .s-absent   { color: #c0392b; font-weight: bold; }
        .s-incertain { color: #f39c12; font-weight: bold; }
        .moi { background: #fffbe6; }
    </style>
</head>
<body>

<p><a class="retour" href="evenements">← Retour aux événements</a></p>

<% if (evenement == null) { %>
    <p class="erreur"><%= erreur != null ? erreur : "Événement introuvable." %></p>
<% } else { %>

<%-- Fiche de l'événement --%>
<div class="fiche">
    <h1><%= evenement.getNom() %></h1>
    <p><strong>Date :</strong> <%= evenement.getHorodatage().format(fmt) %></p>
    <p><strong>Durée :</strong> <%= evenement.getDuree() %> min</p>
    <p><strong>Lieu :</strong> <%= evenement.getLieu() %></p>
    <p><strong>Type :</strong> <span class="badge"><%= evenement.getType() %></span></p>
    <% if (evenement.getDescription() != null && !evenement.getDescription().isEmpty()) { %>
    <p><strong>Description :</strong> <%= evenement.getDescription() %></p>
    <% } %>
    <p><strong>Organisateur :</strong> <%= evenement.getNomOrganisateur() != null ? evenement.getNomOrganisateur() : "-" %></p>
</div>

<%-- Formulaire de participation --%>
<div class="formulaire">
    <h2>Ma participation</h2>

    <% if (succes) { %>
        <p class="succes">Votre participation a bien été enregistrée.</p>
    <% } %>
    <% if (erreur != null) { %>
        <p class="erreur"><%= erreur %></p>
    <% } %>

    <form method="post" action="evenement">
        <input type="hidden" name="idEvenement" value="<%= evenement.getIdEvenement() %>">

        <div class="form-group">
            <label>Statut *</label>
            <div class="statut-group">
                <div class="statut-option">
                    <input type="radio" name="statut" id="present" value="présent"
                           <%= "présent".equals(valStatut) ? "checked" : "" %> required>
                    <label for="present">✓ Présent</label>
                </div>
                <div class="statut-option">
                    <input type="radio" name="statut" id="absent" value="absent"
                           <%= "absent".equals(valStatut) ? "checked" : "" %>>
                    <label for="absent">✗ Absent</label>
                </div>
                <div class="statut-option">
                    <input type="radio" name="statut" id="incertain" value="incertain"
                           <%= "incertain".equals(valStatut) ? "checked" : "" %>>
                    <label for="incertain">? Incertain</label>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="instrument">Instrument</label>
            <select id="instrument" name="instrument">
                <option value="">-- Aucun / Non précisé --</option>
                <% String[] instruments = {"clarinette", "saxophone alto", "saxophone baryton", "trompette", "trombone", "euphonium", "basse", "percussion"}; %>
                <% for (String instr : instruments) { %>
                <option value="<%= instr %>" <%= instr.equals(valInstrument) ? "selected" : "" %>><%= instr.substring(0,1).toUpperCase() + instr.substring(1) %></option>
                <% } %>
            </select>
        </div>

        <button type="submit" class="btn-submit">Enregistrer</button>
    </form>
</div>

<%-- Tableau des participants --%>
<h2>Participants (<%= inscriptions != null ? inscriptions.size() : 0 %>)</h2>
<% if (inscriptions == null || inscriptions.isEmpty()) { %>
    <p style="color:#888;">Aucune réponse pour le moment.</p>
<% } else { %>
<table>
    <thead>
        <tr>
            <th>Fanfaron</th>
            <th>Instrument</th>
            <th>Statut</th>
        </tr>
    </thead>
    <tbody>
        <% for (Inscription ins : inscriptions) {
            boolean estMoi = ins.getNomUtilisateur().equals(utilisateur.getNomUtilisateur());
            String cssStatut = "présent".equals(ins.getStatut()) ? "s-present"
                             : "absent".equals(ins.getStatut())  ? "s-absent" : "s-incertain";
        %>
        <tr<%= estMoi ? " class=\"moi\"" : "" %>>
            <td><%= ins.getNomUtilisateur() %><%= estMoi ? " <em>(vous)</em>" : "" %></td>
            <td><%= ins.getInstrument() != null && !ins.getInstrument().isEmpty() ? ins.getInstrument() : "-" %></td>
            <td class="<%= cssStatut %>"><%= ins.getStatut() %></td>
        </tr>
        <% } %>
    </tbody>
</table>
<% } %>

<% } %>
</body>
</html>
