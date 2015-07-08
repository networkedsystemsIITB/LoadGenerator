<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Load Generator</title>
</head>
<body>
<center>
<h2>LoadGen Parameters</h2>
<form:form method="POST" action="/LoadGen/loadgen">
   <table>
    <tr>
        <td><form:label path="reqRate">Request Rate(reqs/sec)</form:label></td>
        <td><form:input path="reqRate" /></td>
    </tr>
    <tr>
        <td><form:label path="duration">Duration(secs)</form:label></td>
        <td><form:input path="duration" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Start"/>
        </td>
    </tr>
</table>  
</form:form>
</center>
</body>
</html>