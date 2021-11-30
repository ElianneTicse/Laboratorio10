<%@ page import="com.example.laboratorio10.Beans.Region" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean type="java.util.ArrayList<com.example.laboratorio10.Beans.Region>" scope="request" id="lista"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Crear un Pais</title>
        <jsp:include page="../includes/headCss.jsp" />
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="cou" />
            </jsp:include>
            <div class="row justify-content-center mb-4">
                <div class="col-md-6">
                    <h1 class='mb-3'>Crear un Pais</h1>
                    <jsp:include page="../includes/infoMsgs.jsp" />
                    <form method="POST" action="<%=request.getContextPath()%>/CountryServlet?action=crear">
                        <div class="mb-3">
                            <label class="form-label" for="id">Country ID</label>
                            <input type="text" class="form-control" name="id" id="id"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="countryName">Country name</label>
                            <input type="text" class="form-control" name="countryName" id="countryName" />
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Region name</label>
                            <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example" name="regionId">
                                <%for(Region region: lista){%>
                                <option value="<%=region.getIdRegion()%>"><%=region.getName()%></option>
                                <%}%>
                            </select>
                        </div>
                        <a href="<%= request.getContextPath()%>/CountryServlet" class="btn btn-danger">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>
