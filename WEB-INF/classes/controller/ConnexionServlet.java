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

    /**
     * Contrôleur MVC pour la connexion.
     * GET  → affiche le formulaire connexion.jsp
     * POST → vérifie les identifiants via le DAO, ouvre une session si OK
     */
    @WebServlet("/connexion")
    public static class ConnexionServlet extends HttpServlet {

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

    /** Contrôleur MVC pour la déconnexion. */
    @WebServlet("/deconnexion")
    public static class DeconnexionServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();

            response.sendRedirect(request.getContextPath() + "/connexion.jsp");
        }
    }

    /**
     * Contrôleur MVC pour l'inscription.
     * GET  → affiche le formulaire inscription.jsp
     * POST → valide les données, les persiste via le DAO, redirige vers accueil
     */
    @WebServlet("/inscription")
    public static class InscriptionServlet extends HttpServlet {

        private UtilisateurDAO utilisateurDAO;

        @Override
        public void init() {
            utilisateurDAO = new UtilisateurDAOImpl();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            request.setCharacterEncoding("UTF-8");

            String nomUtilisateur   = trim(request.getParameter("nomUtilisateur"));
            String email            = trim(request.getParameter("email"));
            String confirmEmail     = trim(request.getParameter("confirmEmail"));
            String motDePasse       = request.getParameter("motDePasse");
            String confirmMotDePasse= request.getParameter("confirmMotDePasse");
            String prenom           = trim(request.getParameter("prenom"));
            String nom              = trim(request.getParameter("nom"));
            String genre            = trim(request.getParameter("genre"));
            String contraintes      = trim(request.getParameter("contraintesAlimentaires"));

            // --- Validations ---
            if (nomUtilisateur.isEmpty() || email.isEmpty() || motDePasse == null
                    || prenom.isEmpty() || nom.isEmpty() || genre.isEmpty()) {
                retour(request, response, "Veuillez remplir tous les champs obligatoires.",
                       nomUtilisateur, email, prenom, nom, genre, contraintes);
                return;
            }

            if (!email.equals(confirmEmail)) {
                retour(request, response, "Les adresses email ne correspondent pas.",
                       nomUtilisateur, email, prenom, nom, genre, contraintes);
                return;
            }

            if (!motDePasse.equals(confirmMotDePasse)) {
                retour(request, response, "Les mots de passe ne correspondent pas.",
                       nomUtilisateur, email, prenom, nom, genre, contraintes);
                return;
            }

            if (motDePasse.length() < 6) {
                retour(request, response, "Le mot de passe doit contenir au moins 6 caractères.",
                       nomUtilisateur, email, prenom, nom, genre, contraintes);
                return;
            }

            try {
                if (utilisateurDAO.nomUtilisateurExiste(nomUtilisateur)) {
                    retour(request, response, "Ce nom d'utilisateur est déjà pris.",
                           nomUtilisateur, email, prenom, nom, genre, contraintes);
                    return;
                }

                if (utilisateurDAO.emailExiste(email)) {
                    retour(request, response, "Cette adresse email est déjà utilisée.",
                           nomUtilisateur, email, prenom, nom, genre, contraintes);
                    return;
                }

                // Construction du bean
                String motDePasseHash = hasher(motDePasse);
                Utilisateur u = new Utilisateur(nomUtilisateur, email, motDePasseHash, prenom, nom, genre, contraintes, false);

                boolean ok = utilisateurDAO.inserer(u);
                if (!ok) {
                    retour(request, response, "Erreur lors de l'inscription, veuillez réessayer.",
                           nomUtilisateur, email, prenom, nom, genre, contraintes);
                    return;
                }

                // Connexion automatique après inscription
                Utilisateur cree = utilisateurDAO.trouverParNom(nomUtilisateur);
                HttpSession session = request.getSession(true);
                session.setAttribute("utilisateur", cree);

                response.sendRedirect(request.getContextPath() + "/accueil.jsp");

            } catch (Exception e) {
                getServletContext().log("Erreur inscription", e);
                retour(request, response, "Erreur interne, veuillez réessayer.",
                       nomUtilisateur, email, prenom, nom, genre, contraintes);
            }
        }

        private void retour(HttpServletRequest req, HttpServletResponse resp, String erreur,
                            String nomUtilisateur, String email, String prenom,
                            String nom, String genre, String contraintes)
                throws ServletException, IOException {
            req.setAttribute("erreur",                    erreur);
            req.setAttribute("nomUtilisateur",            nomUtilisateur);
            req.setAttribute("email",                     email);
            req.setAttribute("prenom",                    prenom);
            req.setAttribute("nom",                       nom);
            req.setAttribute("genre",                     genre);
            req.setAttribute("contraintesAlimentaires",   contraintes);
            req.getRequestDispatcher("/inscription.jsp").forward(req, resp);
        }

        private String trim(String s) {
            return s != null ? s.trim() : "";
        }
    }
}
