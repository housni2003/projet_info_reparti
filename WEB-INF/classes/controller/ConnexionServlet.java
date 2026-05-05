package controller;

import dao.UtilisateurDAO;
import dao.UtilisateurDAOImpl;
import model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * Contrôleur MVC pour la connexion.
 * GET  → affiche le formulaire connexion.jsp
 * POST → vérifie les identifiants via le DAO, ouvre une session si OK
 */
@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() {
        utilisateurDAO = new UtilisateurDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/connexion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nomUtilisateur = request.getParameter("nomUtilisateur");
        String motDePasse     = request.getParameter("motDePasse");

        // Validation basique des champs
        if (nomUtilisateur == null || nomUtilisateur.isBlank()
                || motDePasse == null || motDePasse.isBlank()) {
            redirect(request, response, "Veuillez remplir tous les champs.", nomUtilisateur);
            return;
        }

        try {
            Utilisateur utilisateur = utilisateurDAO.trouverParNom(nomUtilisateur.trim());

            if (utilisateur == null) {
                redirect(request, response, "Nom d'utilisateur ou mot de passe incorrect.", nomUtilisateur);
                return;
            }

            String hashSaisi = hasher(motDePasse);
            if (!hashSaisi.equals(utilisateur.getMotDePasse())) {
                redirect(request, response, "Nom d'utilisateur ou mot de passe incorrect.", nomUtilisateur);
                return;
            }

            // Authentification réussie : ouvrir la session
            utilisateurDAO.mettreAJourDerniereConnexion(utilisateur.getNomUtilisateur());

            HttpSession session = request.getSession(true);
            session.setAttribute("utilisateur", utilisateur);

            response.sendRedirect(request.getContextPath() + "/accueil.jsp");

        } catch (Exception e) {
            getServletContext().log("Erreur connexion", e);
            redirect(request, response, "Erreur interne, veuillez réessayer.", nomUtilisateur);
        }
    }

    private void redirect(HttpServletRequest req, HttpServletResponse resp,
                          String erreur, String nomUtilisateur)
            throws ServletException, IOException {
        req.setAttribute("erreur", erreur);
        req.setAttribute("nomUtilisateur", nomUtilisateur);
        req.getRequestDispatcher("/connexion.jsp").forward(req, resp);
    }

    /** Hache le mot de passe en SHA-256 (hexadécimal). */
    public static String hasher(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(motDePasse.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }
}
