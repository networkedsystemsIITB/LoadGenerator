<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Load Generator</title>
</head>
<body>
<center>
<h2>LoadGen Parameters</h2>
   <table>
   
    <tr>
        <td>Request Rate(reqs/sec)</td>
        <td>${reqRate}</td>
    </tr>
    <tr>
        <td>Duration</td>
        <td>${duration}</td>
    </tr>
</table>  
</center>
</body>
</html>