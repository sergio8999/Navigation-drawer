<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "BDSergioRuiz";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$titulo = $_GET["titulo"];
$descripcion = $_GET["descripcion"];
$url = $_GET["url"];
$fecha = $_GET["fecha"];

  $sql = "INSERT INTO Peliculas ( titulo, descripcion, url, fecha) VALUES ('$titulo', '$descripcion', '$url', '$fecha')";
echo $sql;

mysqli_set_charset($conexion, "utf8"); //formato de datos utf8
if (mysqli_query($conexion, $sql)) {
      echo "New record created successfully";
} else {
      echo "Error: " . $sql . "<br>" . mysqli_error($conexion);
}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


