package controller;

import dao.EvenementDAO;
import dao.EvenementDAOImpl;
import dao.GroupeDAO;
import dao.GroupeDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Evenement;
import model.Commission;
import model.Utilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/evenements")
public class EvenementServlet extends HttpServlet {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private EvenementDAO evenementDAO;
    private GroupeDAO    groupeDAO;

    @Override
    public void init() {
        evenementDAO = new EvenementDAOImpl();
        groupeDAO    = new GroupeDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur utilisateur = verifierConnexion(request, response);
        if (utilisateur == null) return;

        String action = request.getParameter("action");

        try {
            boolean peutGerer = estMembreCommissionPrestation(utilisateur.getNomUtilisateur());
            request.setAttribute("peutGerer", peutGerer);

            if ("formulaireAjouter".equals(action)) {
                if (!peutGerer) { response.sendRedirect(request.getContextPath() + "/evenements"); return; }
                // pas d'attribut "evenement" → la JSP affiche un formulaire vide
                request.getRequestDispatcher("/formulaireEvenement.jsp").forward(request, response);

            } else if ("formulaireModifier".equals(action)) {
                if (!peutGerer) { response.sendRedirect(request.getContextPath() + "/evenements"); return; }
                int id = Integer.parseInt(request.getParameter("id"));
                Evenement evenement = evenementDAO.trouverParId(id);
                if (evenement == null) { response.sendRedirect(request.getContextPath() + "/evenements"); return; }
                request.setAttribute("evenement", evenement);
                request.getRequestDispatcher("/formulaireEvenement.jsp").forward(request, response);

            } else {
                // Liste de tous les événements
                request.setAttribute("evenements", evenementDAO.listerTous());
                request.getRequestDispatcher("/evenements.jsp").forward(request, response);
            }

        } catch (Exception e) {
            getServletContext().log("Erreur EvenementServlet GET", e);
            response.sendRedirect(request.getContextPath() + "/evenements");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Utilisateur utilisateur = verifierConnexion(request, response);
        if (utilisateur == null) return;

        try {
            if (!estMembreCommissionPrestation(utilisateur.getNomUtilisateur())) {
                response.sendRedirect(request.getContextPath() + "/evenements");
                return;
            }
        } catch (Exception e) {
            getServletContext().log("Erreur vérification commission", e);
            response.sendRedirect(request.getContextPath() + "/evenements");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                Evenement evenement = lireFormulaire(request, 0);
                if (evenement == null) {
                    request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
                    request.setAttribute("peutGerer", true);
                    request.getRequestDispatcher("/formulaireEvenement.jsp").forward(request, response);
                    return;
                }
                evenementDAO.ajouter(evenement);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Evenement evenement = lireFormulaire(request, id);
                if (evenement == null) {
                    request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
                    request.setAttribute("evenement", evenementDAO.trouverParId(id));
                    request.setAttribute("peutGerer", true);
                    request.getRequestDispatcher("/formulaireEvenement.jsp").forward(request, response);
                    return;
                }
                evenementDAO.mettreAJour(evenement);

            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                evenementDAO.supprimer(id);
            }

        } catch (Exception e) {
            getServletContext().log("Erreur EvenementServlet POST", e);
        }

        response.sendRedirect(request.getContextPath() + "/evenements?succes=1");
    }

    // Lit et valide les champs du formulaire. Retourne null si un champ est manquant.
    private Evenement lireFormulaire(HttpServletRequest request, int id) {
        String nom           = trim(request.getParameter("nom"));
        String horodatageStr = trim(request.getParameter("horodatage"));
        String dureeStr      = trim(request.getParameter("duree"));
        String lieu          = trim(request.getParameter("lieu"));
        String description   = trim(request.getParameter("description"));
        String type          = trim(request.getParameter("type"));
        String organisateur  = trim(request.getParameter("nomOrganisateur"));

        if (nom.isEmpty() || horodatageStr.isEmpty() || dureeStr.isEmpty()
                || lieu.isEmpty() || type.isEmpty() || organisateur.isEmpty()) {
            return null;
        }

        try {
            LocalDateTime horodatage = LocalDateTime.parse(horodatageStr, FMT);
            int duree = Integer.parseInt(dureeStr);
            return new Evenement(id, nom, horodatage, duree, lieu, description, type, organisateur);
        } catch (Exception e) {
            return null;
        }
    }

    // Vérifie si l'utilisateur appartient à la commission "commission prestation"
    private boolean estMembreCommissionPrestation(String nomUtilisateur) throws Exception {
        List<Integer> idsUtilisateur = groupeDAO.getCommissionsUtilisateur(nomUtilisateur);
        if (idsUtilisateur.isEmpty()) return false;

        for (Commission c : groupeDAO.listerToutesLesCommissions()) {
            if (c.getNom().equalsIgnoreCase("commission prestation")) {
                return idsUtilisateur.contains(c.getId());
            }
        }
        return false;
    }

    private Utilisateur verifierConnexion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        Utilisateur u = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return null;
        }
        return u;
    }

    private String trim(String s) {
        return s != null ? s.trim() : "";
    }
}
