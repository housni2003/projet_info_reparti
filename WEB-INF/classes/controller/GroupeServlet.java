package controller;

import dao.GroupeDAO;
import dao.GroupeDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utilisateur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/groupes")
public class GroupeServlet extends HttpServlet {

    private GroupeDAO groupeDAO;

    @Override
    public void init() {
        groupeDAO = new GroupeDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur utilisateur = verifierConnexion(request, response);
        if (utilisateur == null) return;

        try {
            request.setAttribute("tousLesPupitres",    groupeDAO.listerTousLesPupitres());
            request.setAttribute("toutesLesCommissions", groupeDAO.listerToutesLesCommissions());
            request.setAttribute("pupitresChoisis",    groupeDAO.getPupitresUtilisateur(utilisateur.getNomUtilisateur()));
            request.setAttribute("commissionsChoisies", groupeDAO.getCommissionsUtilisateur(utilisateur.getNomUtilisateur()));
        } catch (Exception e) {
            getServletContext().log("Erreur chargement groupes", e);
            request.setAttribute("erreur", "Erreur lors du chargement des groupes.");
        }

        request.getRequestDispatcher("/groupes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur utilisateur = verifierConnexion(request, response);
        if (utilisateur == null) return;

        // getParameterValues retourne null si aucune case cochée — on gère avec une liste vide
        List<Integer> idPupitres    = parserIds(request.getParameterValues("pupitres"));
        List<Integer> idCommissions = parserIds(request.getParameterValues("commissions"));

        try {
            groupeDAO.mettreAJourPupitresUtilisateur(utilisateur.getNomUtilisateur(), idPupitres);
            groupeDAO.mettreAJourCommissionsUtilisateur(utilisateur.getNomUtilisateur(), idCommissions);
        } catch (Exception e) {
            getServletContext().log("Erreur mise à jour groupes", e);
        }

        response.sendRedirect(request.getContextPath() + "/groupes?succes=1");
    }

    private List<Integer> parserIds(String[] valeurs) {
        List<Integer> ids = new ArrayList<>();
        if (valeurs != null) {
            for (String v : valeurs) {
                try { ids.add(Integer.parseInt(v)); } catch (NumberFormatException ignored) {}
            }
        }
        return ids;
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
}
