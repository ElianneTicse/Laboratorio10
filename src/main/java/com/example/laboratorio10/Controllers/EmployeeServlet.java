package com.example.laboratorio10.Controllers;

import com.example.laboratorio10.Beans.Department;
import com.example.laboratorio10.Beans.Employee;
import com.example.laboratorio10.Beans.Job;
import com.example.laboratorio10.Beans.JobHistory;
import com.example.laboratorio10.Daos.DepartmentDao;
import com.example.laboratorio10.Daos.EmployeeDao;
import com.example.laboratorio10.Daos.JobDao;
import com.example.laboratorio10.Daos.JobHistoryDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");

        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

            RequestDispatcher view;
            EmployeeDao employeeDao = new EmployeeDao();
            JobDao jobDao = new JobDao();
            DepartmentDao departmentDao = new DepartmentDao();
            JobHistoryDao jhDao= new JobHistoryDao();

            String rol = (String) session.getAttribute("rolUsuario");

            switch (rol) {
                case "Top 4":
                    action = "lista";
                    break;
                case "Top 3":
                    if (action.equals("agregar") || action.equals("borrar")) {
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
                case "lista":
                    request.setAttribute("listaEmpleados", employeeDao.listarEmpleados());
                    view = request.getRequestDispatcher("employees/lista.jsp");
                    view.forward(request, response);
                    break;
                case "agregar":
                    request.setAttribute("listaTrabajos", jobDao.listarTrabajos());
                    request.setAttribute("listaDepartamentos", departmentDao.listaDepartamentos());
                    request.setAttribute("listaJefes", employeeDao.listarEmpleados());

                    view = request.getRequestDispatcher("employees/formularioNuevo.jsp");
                    view.forward(request, response);
                    break;
                case "editar":
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int employeeId = 0;
                        try {
                            employeeId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("EmployeeServlet");
                        }

                        Employee emp = employeeDao.obtenerEmpleado(employeeId);

                        if (emp != null) {
                            request.setAttribute("empleado", emp);
                            request.setAttribute("listaTrabajos", jobDao.listarTrabajos());
                            request.setAttribute("listaDepartamentos", departmentDao.listaDepartamentos());
                            request.setAttribute("listaJefes", employeeDao.listarEmpleados());
                            JobHistory jh=jhDao.obtenerUltimoJob(employeeId);
                            if(jh==null){
                                String msg="No tiene registro de anteriores trabajos";
                                session.setAttribute("msg",msg);
                            }else{
                                request.setAttribute("listaJH",jhDao.listarJobHistories(employeeId));
                            }
                            view = request.getRequestDispatcher("employees/formularioEditar.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect("EmployeeServlet");
                        }

                    } else {
                        response.sendRedirect("EmployeeServlet");
                    }

                    break;
                case "borrar":
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int employeeId = 0;
                        try {
                            employeeId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("EmployeeServlet");
                        }

                        Employee emp = employeeDao.obtenerEmpleado(employeeId);

                        if (emp != null) {
                            try {
                                employeeDao.borrarEmpleado(employeeId);
                                request.getSession().setAttribute("err", "Empleado borrado exitosamente");
                            } catch (SQLException e) {
                                request.getSession().setAttribute("err", "Error al borrar el empleado");
                                e.printStackTrace();
                            }
                            response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                        }
                    } else {
                        response.sendRedirect("EmployeeServlet");
                    }

                    break;
                case "est":
                    request.setAttribute("listaSalarioPorDepa", departmentDao.listaSalarioPorDepartamento());
                    request.setAttribute("listaEmpleadPorRegion", employeeDao.listaEmpleadosPorRegion());
                    view = request.getRequestDispatcher("employees/estadisticas.jsp");
                    view.forward(request, response);
                    break;
                default:
                    response.sendRedirect("EmployeeServlet");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");

        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {

            Employee e = new Employee();
            e.setFirstName(request.getParameter("first_name"));
            e.setLastName(request.getParameter("last_name"));
            e.setEmail(request.getParameter("email"));
            e.setPhoneNumber(request.getParameter("phone"));
            e.setHireDate(request.getParameter("hire_date"));
            e.setSalary(new BigDecimal(request.getParameter("salary")));
            e.setCommissionPct(request.getParameter("commission").equals("") ? null : new BigDecimal(request.getParameter("commission")));

            String jobId = request.getParameter("job_id");
            Job job = new Job(jobId);
            e.setJob(job);

            String managerId = request.getParameter("manager_id");
            if (!managerId.equals("sin-jefe")) {
                Employee manager = new Employee(Integer.parseInt(managerId));
                e.setManager(manager);
            }

            String departmentId = request.getParameter("department_id");
            Department department = new Department(Integer.parseInt(departmentId));
            e.setDepartment(department);

            EmployeeDao employeeDao = new EmployeeDao();
            JobHistoryDao jhDao= new JobHistoryDao();

            if (request.getParameter("employee_id") == null) {
                try {
                    employeeDao.guardarEmpleado(e);
                    session.setAttribute("msg", "Empleado creado exitosamente");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } catch (SQLException exc) {
                    session.setAttribute("err", "Error al crear el empleado");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=agregar");
                }
            } else {
                e.setEmployeeId(Integer.parseInt(request.getParameter("employee_id")));
                try {
                    JobHistory jh=jhDao.obtenerUltimoJob(e.getEmployeeId());
                    Employee emp=employeeDao.obtenerEmpleado(e.getEmployeeId());
                    DateTimeFormatter end_date=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    employeeDao.actualizarEmpleado(e);
                    if(!e.getJob().getJobId().equals(emp.getJob().getJobId())){
                        if(jh==null){
                            jhDao.insertarJobHistory(new JobHistory(e.getEmployeeId(),emp.getHireDate(),end_date.format(LocalDateTime.now()),emp.getJob(),emp.getDepartment()));
                        }else{
                            jhDao.insertarJobHistory(new JobHistory(e.getEmployeeId(),jh.getEndDate(),end_date.format(LocalDateTime.now()),emp.getJob(),emp.getDepartment()));
                        }
                    }
                    session.setAttribute("msg", "Empleado actualizado exitosamente");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } catch (SQLException ex) {
                    session.setAttribute("err", "Error al actualizar el empleado");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=editar");
                }
            }
        }
    }

}
