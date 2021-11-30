package com.example.laboratorio10.Controllers;


import com.example.laboratorio10.Beans.Department;
import com.example.laboratorio10.Beans.Employee;
import com.example.laboratorio10.Beans.Job;
import com.example.laboratorio10.Daos.DepartmentDao;
import com.example.laboratorio10.Daos.EmployeeDao;
import com.example.laboratorio10.Daos.JobDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "loginform" : request.getParameter("action");
        HttpSession session = request.getSession();

        switch (action) {
            case "loginform":
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession != null && employeeSession.getEmployeeId() > 0) {
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } else {
                    RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                    view.forward(request, response);
                }
                break;
            case "logout":
                session.invalidate();
                response.sendRedirect(request.getContextPath());
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String username = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");

        if (username == null || password == null) {
            request.setAttribute("err", "El usuario o password no pueden ser vacíos");
            RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
            view.forward(request, response);
        } else {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.validarUsuarioPasswordHashed(username, password);
            JobDao jobDao = new JobDao();
            DepartmentDao departmentDao = new DepartmentDao();

            if (employee != null) {
                HttpSession session = request.getSession();
                session.setAttribute("employeeSession", employee);

                session.setMaxInactiveInterval(10 * 60); // 10 minutos
                Job job = jobDao.obtenerTrabajo(employee.getJob().getJobId());
                Department department = departmentDao.obtener(employee.getDepartment().getDepartmentId());

                if (job.getMaxSalary()>15000 || (employee.getEmployeeId() == department.getManager().getEmployeeId()) ){
                    session.setAttribute("rolUsuario","Top 1");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                }else if (job.getMaxSalary()>8500){
                    session.setAttribute("rolUsuario","Top 2");
                    response.sendRedirect(request.getContextPath() + "/JobServlet");
                }else if (job.getMaxSalary()>5000){
                    session.setAttribute("rolUsuario","Top 3");
                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                }else {
                    session.setAttribute("rolUsuario","Top 4");
                    response.sendRedirect(request.getContextPath() + "/CountryServlet");
                }

            } else {
                request.setAttribute("err", "El usuario o password no existen");
                RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                view.forward(request, response);
            }
        }

    }
}
