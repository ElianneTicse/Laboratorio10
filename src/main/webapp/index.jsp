<%@ page import="com.example.laboratorio10.Beans.Employee" %>

<%
    Employee emp = (Employee) session.getAttribute("employeeSession");
    if (emp != null) {
        response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Gestión HR</title>
        <jsp:include page="includes/headCss.jsp"></jsp:include>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="includes/navbar.jsp">
                <jsp:param name="currentPage" value="emp"/>
            </jsp:include>


            <div class="row mt-4">
                <div class="col">
                    <ul class="list-group">
                        <li class="list-group-item">Alessandra Morales - 20185719</li>
                        <li class="list-group-item">Elianne Ticse - 20185361</li>
                        <li class="list-group-item">Diana Sologuren - 20185521</li>
                    </ul>
                </div>

                <div class="col"><img width="100%" src="<%=request.getContextPath()%>/resources/images/hr.jpg"></div>
            </div>
            <jsp:include page="includes/footer.jsp"/>
        </div>
    </body>
</html>