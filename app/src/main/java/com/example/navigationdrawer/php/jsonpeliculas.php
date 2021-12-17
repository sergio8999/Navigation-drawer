

<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "BDSergioRuiz";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM Peliculas";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$films = array(); //creamos un array

while($row = mysqli_fetch_array($result))
{
    $id=$row['id'];
    $titulo=$row['titulo'];
    $descripcion=$row['descripcion'];
    $url=$row['url'];
    $fecha=$row['fecha'];


    $films[] = array('id'=> $id,'titulo'=> $titulo, 'descripcion'=> $descripcion, 'url'=> $url, 'fecha'=> $fecha);

}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


//Creamos el JSON
$json_string = json_encode($films);

echo $json_string;

//Si queremos crear un archivo json, sería de esta forma:
/*
$file = 'clientes.json';
file_put_contents($file, $json_string);
*/


