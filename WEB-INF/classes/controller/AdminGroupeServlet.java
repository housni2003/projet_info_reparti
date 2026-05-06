package controller;

import dao.GroupeDAO;
import dao.GroupeDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Commission;
import model.Pupitre;
import model.Utilisateur;

import java.io.IOException;

@WebServlet("/admin-groupes")
public class AdminGroupeServlet extends HttpServlet {

    private GroupeDAO groupeDAO;

    @Override
    public void init() {
        groupeDAO = new GroupeDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verifierAdmin(request, response)) return;

        String action = request.getParameter("action");
        String type = request.getParameter("type");

        try {
            if ("formAjout".equals(action)) {
                request.setAttribute("type", type);
                request.getRequestDispatcher("/formulaireGroupe.jsp").forward(request, response);

            } else if ("formModif".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("type", type);

                if ("pupitre".equals(type)) {
                    request.setAttribute("pupitre", groupeDAO.trouverPupitreParId(id));
                } else if ("commission".equals(type)) {
                    request.setAttribute("commission", groupeDAO.trouverCommissionParId(id));
                }
                request.getRequestDispatcher("/formulaireGroupe.jsp").forward(request, response);

            } else {
                request.setAttribute("pupitres", groupeDAO.listerTousLesPupitres());
                request.setAttribute("commissions", groupeDAO.listerToutesLesCommissions());
                request.getRequestDispatcher("/adminGroupes.jsp").forward(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Erreur AdminGroupeServlet GET", e);
            response.sendRedirect(request.getContextPath() + "/admin-groupes?erreur=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!verifierAdmin(request, response)) return;

        String action = request.getParameter("action");
        String type = request.getParameter("type");
        String nom = request.getParameter("nom");

        try {
            if ("ajouter".equals(action)) {
                if ("pupitre".equals(type)) {
                    Pupitre p = new Pupitre();
                    p.setNom(nom);
                    groupeDAO.ajouterPupitre(p);
                } else if ("commission".equals(type)) {
                    Commission c = new Commission();
                    c.setNom(nom);
                    groupeDAO.ajouterCommission(c);
                }
            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if ("pupitre".equals(type)) {
                    Pupitre p = new Pupitre(id, nom);
                    groupeDAO.modifierPupitre(p);
                } else if ("commission".equals(type)) {
                    Commission c = new Commission(id, nom);
                    groupeDAO.modifierCommission(c);
                }
            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if ("pupitre".equals(type)) {
                    groupeDAO.supprimerPupitre(id);
                } else if ("commission".equals(type)) {
                    groupeDAO.supprimerCommission(id);
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin-groupes?succes=1");

        } catch (Exception e) {
            getServletContext().log("Erreur AdminGroupeServlet POST", e);
            response.sendRedirect(request.getContextPath() + "/admin-groupes?erreur=1");
        }
    }

    private boolean verifierAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Utilisateur u = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (u == null || !u.isEstAdmin()) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return false;
        }
        return true;
    }
}