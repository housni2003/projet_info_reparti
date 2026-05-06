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
import model.Inscription;
import model.Pupitre;
import model.Utilisateur;

import java.io.IOException;
import java.util.List;

@WebServlet("/evenement")
public class DetailEvenementServlet extends HttpServlet {

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

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/evenements");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Evenement evenement = evenementDAO.trouverParId(id);
            if (evenement == null) {
                response.sendRedirect(request.getContextPath() + "/evenements");
                return;
            }

            Inscription monInscription = evenementDAO.obtenirInscription(utilisateur.getNomUtilisateur(), id);
            List<Inscription> toutesInscriptions = evenementDAO.listerInscriptionsParEvenement(id);

            List<Pupitre> tousLesPupitres = groupeDAO.listerTousLesPupitres();
            List<Integer> mesPupitresIds  = groupeDAO.getPupitresUtilisateur(utilisateur.getNomUtilisateur());

            request.setAttribute("evenement", evenement);
            request.setAttribute("monInscription", monInscription);
            request.setAttribute("inscriptions", toutesInscriptions);

            request.setAttribute("tousLesPupitres", tousLesPupitres);
            request.setAttribute("mesPupitresIds", mesPupitresIds);

        } catch (Exception e) {
            getServletContext().log("Erreur DetailEvenementServlet GET", e);
            request.setAttribute("erreur", "Erreur lors du chargement de l'événement.");
        }

        request.getRequestDispatcher("/detailEvenement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Utilisateur utilisateur = verifierConnexion(request, response);
        if (utilisateur == null) return;

        String idStr      = request.getParameter("idEvenement");
        String instrument = request.getParameter("instrument") != null ? request.getParameter("instrument").trim() : "";
        String statut     = request.getParameter("statut");

        if (idStr == null || statut == null || statut.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/evenements");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            evenementDAO.inscrireFanfaron(utilisateur.getNomUtilisateur(), id, instrument.isEmpty() ? null : instrument, statut);
            response.sendRedirect(request.getContextPath() + "/evenement?id=" + id + "&succes=1");
        } catch (Exception e) {
            getServletContext().log("Erreur DetailEvenementServlet POST", e);
            response.sendRedirect(request.getContextPath() + "/evenement?id=" + idStr);
        }
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