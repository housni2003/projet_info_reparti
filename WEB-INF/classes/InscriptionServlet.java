import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nomUtilisateur = request.getParameter("nomUtilisateur");
        String email = request.getParameter("email");
        String confirmEmail = request.getParameter("confirmEmail");
        String motDePasse = request.getParameter("motDePasse");
        String confirmMotDePasse = request.getParameter("confirmMotDePasse");
        String prenom = request.getParameter("prenom");
        String nom = request.getParameter("nom");
        String genre = request.getParameter("genre");
        String contraintes = request.getParameter("contraintesAlimentaires");

        // Vérifications
        if (!email.equals(confirmEmail)) {
            request.setAttribute("erreur", "Les adresses email ne correspondent pas.");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }

        if (!motDePasse.equals(confirmMotDePasse)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }

        // Vérifier unicité en base
        // if (utilisateurExiste(nomUtilisateur) || emailExiste(email)) ...

        // Hachage mot de passe
        // String motDePasseHash = ...

        // Insertion en base
        // ...

        // Connexion automatique
        HttpSession session = request.getSession();
        session.setAttribute("utilisateur", nomUtilisateur);

        response.sendRedirect("accueil.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().println("Page inscription");
    }
}