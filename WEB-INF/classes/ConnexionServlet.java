import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.PrintWriter;


@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nomUtilisateur = request.getParameter("nomUtilisateur");
        String motDePasse = request.getParameter("motDePasse");

        // Vérifier dans la base
        boolean authentifie = false;

        // authentifie = verifierMotDePasse(...)

        if (authentifie) {
            HttpSession session = request.getSession();
            session.setAttribute("utilisateur", nomUtilisateur);

            // Mettre à jour derniere_connexion en base

            response.sendRedirect("accueil.jsp");
        } else {
            request.setAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrect.");
            request.getRequestDispatcher("/connexion.jsp").forward(request, response);
        }
    }

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().println("Page inscription");
    }
}