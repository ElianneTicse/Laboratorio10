<%@page import="com.example.laboratorio10.Beans.Country" %>
<%@page import="java.util.ArrayList" %>
<%@ page import="com.mysql.cj.Session" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean type="java.util.ArrayList<com.example.laboratorio10.Beans.Country>" scope="request" id="lista"/>
<jsp:useBean id="employeeSession" type="com.example.laboratorio10.Beans.Employee" scope="session"
             class="com.example.laboratorio10.Beans.Employee"/>
<html>
    <head>
        <title>Lista de paises</title>
        <jsp:include page="../includes/headCss.jsp"/>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="cou"/>
            </jsp:include>
            <div class="row mb-5 mt-4">
                <div class="col-md-7">
                    <h1>Lista de Paises</h1>
                </div>
                <% if (!session.getAttribute("rolUsuario").equals("Top 3") && !session.getAttribute("rolUsuario").equals("Top 4")){ %>
                <div class="col-md-5 col-lg-4 ms-auto my-auto text-md-end">
                    <a href="<%= request.getContextPath()%>/CountryServlet?action=formCrear" class="btn btn-primary">
                        Crear País</a>
                </div>
                <%}%>
                
            </div>
            <jsp:include page="../includes/infoMsgs.jsp"/>
            <table class="table">
                <tr>
                    <th>#</th>
                    <th>Country ID</th>
                    <th>Country name</th>
                    <th>Region ID</th>
                    <th></th>
                    <th></th>
                </tr>
                <%
                    int i = 1;
                    for (Country country : lista) {
                %>
                <tr>
                    <td><%=i%>
                    </td>
                    <td><%=country.getCountryId()%>
                    </td>
                    <td><%=country.getCountryName() %>
                    </td>
                    <td><%=country.getRegion().getName()%>
                    </td>

                    <% if (!session.getAttribute("rolUsuario").equals("Top 2") && !session.getAttribute("rolUsuario").equals("Top 4")){ %>
                    <td>
                        <a href="<%=request.getContextPath()%>/CountryServlet?action=editar&id=<%=country.getCountryId()%>">
                            Editar
                        </a>
                    </td>
                    <%}%>
                    <% if (!session.getAttribute("rolUsuario").equals("Top 3") && !session.getAttribute("rolUsuario").equals("Top 4")){ %>
                    <td>
                        <a href="<%=request.getContextPath()%>/CountryServlet?action=borrar&id=<%=country.getCountryId()%>">
                            Borrar
                        </a>
                    </td>
                    <%}%>
                </tr>
                <%
                        i++;
                    }
                %>
            </table>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>