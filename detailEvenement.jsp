<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utilisateur, model.Evenement, model.Inscription, model.Pupitre, java.util.List, java.time.format.DateTimeFormatter" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/connexion");
        return;
    }

    Evenement           evenement        = (Evenement)    request.getAttribute("evenement");
    Inscription         monInscription   = (Inscription)  request.getAttribute("monInscription");
    List<Inscription>   inscriptions     = (List<Inscription>) request.getAttribute("inscriptions");

    // NOUVEAU : Récupération des pupitres
    List<Pupitre>       tousLesPupitres  = (List<Pupitre>) request.getAttribute("tousLesPupitres");
    List<Integer>       mesPupitresIds   = (List<Integer>) request.getAttribute("mesPupitresIds");

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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="body-lg">

<p><a class="retour" href="evenements">← Retour aux événements</a></p>

<% if (evenement == null) { %>
<p class="erreur"><%= erreur != null ? erreur : "Événement introuvable." %></p>
<% } else { %>

<div class="fiche">
    <h1><%= evenement.getNom() %></h1>
    <p><strong>Date :</strong> <%= evenement.getHorodatage().format(fmt) %></p>
    <p><strong>Durée :</strong> <%= evenement.getDuree() %> min</p>
    <p><strong>Lieu :</strong> <%= evenement.getLieu() %></p>
    <p><strong>Type :</strong> <span class="badge badge-purple"><%= evenement.getType() %></span></p>
    <% if (evenement.getDescription() != null && !evenement.getDescription().isEmpty()) { %>
    <p><strong>Description :</strong> <%= evenement.getDescription() %></p>
    <% } %>
    <p><strong>Organisateur :</strong> <%= evenement.getNomOrganisateur() != null ? evenement.getNomOrganisateur() : "-" %></p>
</div>

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
                <option value="">Sans instrument</option>
                <%
                    if (tousLesPupitres != null && mesPupitresIds != null) {
                        for (Pupitre p : tousLesPupitres) {
                            if (mesPupitresIds.contains(p.getId())) {
                %>
                <option value="<%= p.getNom() %>" <%= p.getNom().equals(valInstrument) ? "selected" : "" %>>
                    <%= p.getNom() %>
                </option>
                <%          }
                }
                }
                %>
            </select>
        </div>

        <button type="submit" class="btn btn-primary" style="margin-top: 15px;">Enregistrer</button>
    </form>
</div>

<h2>Participants (<%= inscriptions != null ? inscriptions.size() : 0 %>)</h2>
<% if (inscriptions == null || inscriptions.isEmpty()) { %>
<p class="vide">Aucune réponse pour le moment.</p>
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
        <td><%= ins.getInstrument() != null && !ins.getInstrument().isEmpty() ? ins.getInstrument() : "<em>Sans instrument</em>" %></td>
        <td class="<%= cssStatut %>"><%= ins.getStatut() %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<% } %>
</body>
</html>