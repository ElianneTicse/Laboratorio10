package com.example.laboratorio10.Controllers;

import com.example.laboratorio10.Beans.Country;
import com.example.laboratorio10.Beans.Employee;
import com.example.laboratorio10.Beans.Region;
import com.example.laboratorio10.Daos.CountryDao;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(name = "CountryServlet", urlPatterns = {"/CountryServlet"})
public class CountryServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");
        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {

            String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

            CountryDao countryDao = new CountryDao();
            RequestDispatcher view;
            Country country;
            String countryId;

            String rol = (String) session.getAttribute("rolUsuario");

            switch (rol) {
                case "Top 4":
                    action = "lista";
                    break;
                case "Top 3":
                    if (action.equals("formCrear") || action.equals("crear") || action.equals("borrar")) {
                        action = "lista";
                    }
                    break;
                case "Top 2":
                    if (action.equals("editar")) {
                        action = "lista";
                    }
                    break;
            }

            switch (action) {
                case "formCrear":
                    ArrayList<Region> lista1 = countryDao.listaRegiones();
                    request.setAttribute("lista", lista1);
                    view = request.getRequestDispatcher("country/newCountry.jsp");
                    view.forward(request, response);
                    break;
                case "crear":
                    countryId = request.getParameter("id");
                    String countryName = request.getParameter("countryName");
                    BigDecimal regionId = new BigDecimal(request.getParameter("regionId"));

                    country = countryDao.obtener(countryId);

                    if (country == null) {
                        countryDao.crear(countryId, countryName, regionId);
                    } else {
                        countryDao.actualizar(countryId, countryName, regionId);
                    }
                    response.sendRedirect(request.getContextPath() + "/CountryServlet");
                    break;
                case "lista":
                    ArrayList<Country> lista = countryDao.listar();

                    request.setAttribute("lista", lista);

                    view = request.getRequestDispatcher("country/listaCountry.jsp");
                    view.forward(request, response);
                    break;

                case "editar":
                    countryId = request.getParameter("id");
                    country = countryDao.obtener(countryId);
                    if (country == null) {
                        response.sendRedirect(request.getContextPath() + "/CountryServlet");
                    } else {
                        request.setAttribute("country", country);
                        view = request.getRequestDispatcher("country/updateCountry.jsp");
                        view.forward(request, response);
                    }
                    break;
                case "borrar":
                    countryId = request.getParameter("id");
                    if (countryDao.obtener(countryId) != null) {
                        countryDao.borrar(countryId);
                        response.sendRedirect(request.getContextPath() + "/CountryServlet?msg=??Borrado exitoso!");
                    }else{
                        response.sendRedirect(request.getContextPath() + "/CountryServlet?err=??Error al borrar!");
                    }
                    break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
