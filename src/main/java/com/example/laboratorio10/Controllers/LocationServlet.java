package com.example.laboratorio10.Controllers;

import com.example.laboratorio10.Beans.Employee;
import com.example.laboratorio10.Beans.Location;
import com.example.laboratorio10.Daos.CountryDao;
import com.example.laboratorio10.Daos.LocationDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "LocationServlet", urlPatterns = {"/LocationServlet"})
public class LocationServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");

        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            if (session.getAttribute("rolUsuario").equals("Top 4")) {
                response.sendRedirect(request.getContextPath() + "/CountryServlet");
            } else {
                String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

                LocationDao locationDao = new LocationDao();
                CountryDao countryDao = new CountryDao();
                RequestDispatcher view;
                Location location;
                int locationId;

                String rol = (String) session.getAttribute("rolUsuario");

                switch (rol) {
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
                        request.setAttribute("listaPaises", countryDao.listar());
                        view = request.getRequestDispatcher("location/newLocation.jsp");
                        view.forward(request, response);
                        break;
                    case "crear":
                        locationId = Integer.parseInt(request.getParameter("id"));
                        String streetAddress = request.getParameter("streetAddress");
                        String postalCode = request.getParameter("postalCode");
                        String city = request.getParameter("city");
                        String stateProvince = request.getParameter("stateProvince");
                        String countryId = request.getParameter("countryId");

                        location = locationDao.obtener(locationId);

                        if (location == null) {
                            locationDao.crear(locationId, streetAddress, postalCode, city, stateProvince, countryId);
                        } else {
                            locationDao.actualizar(locationId, streetAddress, postalCode, city, stateProvince, countryId);
                        }
                        response.sendRedirect(request.getContextPath() + "/LocationServlet");
                        break;
                    case "lista":
                        ArrayList<Location> lista = locationDao.listar();

                        request.setAttribute("lista", lista);

                        view = request.getRequestDispatcher("location/listaLocation.jsp");
                        view.forward(request, response);
                        break;

                    case "editar":
                        locationId = Integer.parseInt(request.getParameter("id"));
                        location = locationDao.obtener(locationId);
                        if (location == null) {
                            response.sendRedirect(request.getContextPath() + "/LocationServlet");
                        } else {
                            request.setAttribute("listaPaises", countryDao.listar());
                            request.setAttribute("location", location);
                            view = request.getRequestDispatcher("location/updateLocation.jsp");
                            view.forward(request, response);
                        }
                        break;
                    case "borrar":
                        locationId = Integer.parseInt(request.getParameter("id"));
                        if (locationDao.obtener(locationId) != null) {
                            locationDao.borrar(locationId);
                        }
                        response.sendRedirect(request.getContextPath() + "/LocationServlet");
                        break;
                }
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
