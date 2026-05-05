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

/**
 * Contrôleur MVC pour l'inscription.
 * GET  → affiche le formulaire inscription.jsp
 * POST → valide les données, les persiste via le DAO, redirige vers accueil
 */
@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

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
            // Unicité du nom d'utilisateur et de l'email
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
            String motDePasseHash = ConnexionServlet.hasher(motDePasse);
            Utilisateur u = new Utilisateur(nomUtilisateur, email, motDePasseHash,
                                            prenom, nom, genre, contraintes);

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
