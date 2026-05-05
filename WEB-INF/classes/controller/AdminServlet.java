package controller;

import dao.UtilisateurDAO;
import dao.UtilisateurDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utilisateur;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() {
        utilisateurDAO = new UtilisateurDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur admin = verifierAdmin(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        if ("formulaireModifier".equals(action)) {
            String nomUtilisateur = request.getParameter("nomUtilisateur");
            try {
                Utilisateur cible = utilisateurDAO.trouverParNom(nomUtilisateur);
                if (cible == null) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                    return;
                }
                request.setAttribute("cible", cible);
                request.getRequestDispatcher("/modifierUtilisateur.jsp").forward(request, response);
            } catch (Exception e) {
                getServletContext().log("Erreur chargement utilisateur", e);
                response.sendRedirect(request.getContextPath() + "/admin");
            }
        } else {
            afficherListe(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Utilisateur admin = verifierAdmin(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            String nomUtilisateur = request.getParameter("nomUtilisateur");
            // Empêcher l'admin de se supprimer lui-même
            if (nomUtilisateur != null && !nomUtilisateur.equals(admin.getNomUtilisateur())) {
                try {
                    utilisateurDAO.supprimer(nomUtilisateur);
                } catch (Exception e) {
                    getServletContext().log("Erreur suppression", e);
                }
            }

        } else if ("modifier".equals(action)) {
            String nomUtilisateur = request.getParameter("nomUtilisateur");
            String email          = trim(request.getParameter("email"));
            String prenom         = trim(request.getParameter("prenom"));
            String nom            = trim(request.getParameter("nom"));
            String genre          = trim(request.getParameter("genre"));
            String contraintes    = trim(request.getParameter("contraintesAlimentaires"));
            boolean estAdmin      = "on".equals(request.getParameter("estAdmin"));

            if (email.isEmpty() || prenom.isEmpty() || nom.isEmpty() || genre.isEmpty()) {
                try {
                    Utilisateur cible = utilisateurDAO.trouverParNom(nomUtilisateur);
                    request.setAttribute("cible", cible);
                    request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
                    request.getRequestDispatcher("/modifierUtilisateur.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                }
                return;
            }

            try {
                Utilisateur cible = utilisateurDAO.trouverParNom(nomUtilisateur);
                if (cible != null) {
                    cible.setEmail(email);
                    cible.setPrenom(prenom);
                    cible.setNom(nom);
                    cible.setGenre(genre);
                    cible.setContraintesAlimentaires(contraintes);
                    cible.setEstAdmin(estAdmin);
                    utilisateurDAO.mettreAJour(cible);

                    // Si l'admin modifie son propre compte, rafraîchir la session
                    if (nomUtilisateur.equals(admin.getNomUtilisateur())) {
                        request.getSession().setAttribute("utilisateur", cible);
                    }
                }
            } catch (Exception e) {
                getServletContext().log("Erreur modification", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin");
    }

    private void afficherListe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Utilisateur> utilisateurs = utilisateurDAO.listerTous();
            request.setAttribute("utilisateurs", utilisateurs);
        } catch (Exception e) {
            getServletContext().log("Erreur liste utilisateurs", e);
            request.setAttribute("erreur", "Erreur lors du chargement de la liste.");
        }
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    private Utilisateur verifierAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return null;
        }
        if (!utilisateur.isEstAdmin()) {
            response.sendRedirect(request.getContextPath() + "/accueil.jsp");
            return null;
        }
        return utilisateur;
    }

    private String trim(String s) {
        return s != null ? s.trim() : "";
    }
}
