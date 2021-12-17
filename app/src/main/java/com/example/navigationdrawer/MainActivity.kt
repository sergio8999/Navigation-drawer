package com.example.navigationdrawer

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.navigationdrawer.ui.theme.NavigationDrawerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.URL
import androidx.compose.runtime.remember
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

var indice = 0

var indiceArray = 0

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationDrawerTheme {
                window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen();
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MainScreen() {

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        Navigation(navController = navController)
    }

}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavHostController) {
    val items = listOf(
        NavegacionItem.Home,
        NavegacionItem.Add,
    )

    Column(
        modifier = Modifier
            .background(color = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.imagen),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    //.height(100.dp)
                    .fillMaxSize()
            )

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { items ->
            DrawerItem(item = items, selected = currentRoute == items.route, onItemClick = {

                navController.navigate(items.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }

            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Sergio",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun DrawerItem(item: NavegacionItem, selected: Boolean, onItemClick: (NavegacionItem) -> Unit) {
    val background = if (selected) MaterialTheme.colors.primary else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(background)
            .padding(start = 10.dp)
    ) {

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = Color.Black
        )

    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = "Navegación películas", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.Black
    )
}


@ExperimentalFoundationApi
@Composable
fun HomeScreen() {
    ArrayPeliculas.listapeliculas = cargarJson()

    // Variables AlertDialog
    val (mostrarDialogo, setMostrarDialogo) = remember { mutableStateOf(false) }

    Column() {
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)){
            itemsIndexed(ArrayPeliculas.listapeliculas){
                    index,pelicula->
                Card(elevation= 4.dp,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { },
                            onLongClick = {
                                indice = pelicula.id
                                indiceArray = index
                                setMostrarDialogo(true)
                            })) {
                    Column() {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                        ) {
                            Column() {
                                Surface(modifier = Modifier.height(240.dp)) {
                                    CargarImagen(url =  pelicula.url)
                                }
                            }
                            val scrollState = rememberScrollState()
                            Column(modifier = Modifier
                                .padding(7.dp)
                                .fillMaxWidth()
                                .verticalScroll(scrollState)) {
                                Text(text = pelicula.titulo,fontWeight = FontWeight.Bold,fontSize = 17.sp)
                                Text(text = pelicula.descripcion, fontSize = 15.sp)
                            }
                        }
                        Row() {
                            Text(text = pelicula.fecha, Modifier.padding(start = 5.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        AlertDialog(mostrarDialogo, setMostrarDialogo)
    }
}



@Composable
fun CargarImagen(url:String){
    Image(
        painter = rememberImagePainter(url),
        contentDescription = "Imagen",
        contentScale = ContentScale.Crop
    )

}

@Composable
fun AlertDialog(mostrarDialogo: Boolean, setMostrarDialogo: (Boolean) -> Unit ){
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Borrar pelicula")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        borrar(indice)
                        ArrayPeliculas.listapeliculas.removeAt(indiceArray)
                        setMostrarDialogo(false)
                    },
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setMostrarDialogo(false)
                    },
                ) {
                    Text("Cancelar")
                }
            },
            text = {
                Text("¿Esta seguro de borrar la pelicula?")
            },
        )
    }
}

@Composable
fun AddScreeen() {
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Espacio()
        Text(text = "Añadir peliculas",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth())

        Espacio()

        OutlinedTextField(
            value = titulo,
            onValueChange = {nuevoTitulo ->
                titulo = nuevoTitulo
            },
            label = {
                Text(text = "Introducir título")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right),
            leadingIcon = {
                // In this method we are specifying
                // our leading icon and its color.
                Icon(
                    painter = painterResource(id = R.drawable.ic_title),
                    contentDescription = "image",
                    tint = MaterialTheme.colors.primary
                )
            }

        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {nuevaDescripcion ->
                descripcion = nuevaDescripcion
            },
            label = {
                Text(text = "Introducir descripción")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right),
            leadingIcon = {
                // In this method we are specifying
                // our leading icon and its color.
                Icon(
                    painter = painterResource(id = R.drawable.ic_descripcion),
                    contentDescription = "image",
                    tint = MaterialTheme.colors.primary
                )
            }
        )
        OutlinedTextField(
            value = url,
            onValueChange = { nuevaUrl ->
                url = nuevaUrl
            },
            label = {
                Text(text = "Introducir url")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right),
            leadingIcon = {
                // In this method we are specifying
                // our leading icon and its color.
                Icon(
                    painter = painterResource(id = R.drawable.ic_url),
                    contentDescription = "image",
                    tint = MaterialTheme.colors.primary
                )
            }
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { nuevaFecha ->
                fecha = nuevaFecha
            },
            label = {
                Text(text = "Introducir fecha")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right),
            leadingIcon = {
                // In this method we are specifying
                // our leading icon and its color.
                Icon(
                    painter = painterResource(id = R.drawable.ic_fecha),
                    contentDescription = "image",
                    tint = MaterialTheme.colors.primary
                )
            }
        )

        Boton(
            anadir = {
                if(!titulo.isEmpty() && !descripcion.isEmpty() && !url.isEmpty() && !fecha.isEmpty()){
                    try {
                        insertar(titulo, descripcion, url, fecha)
                        titulo = ""
                        descripcion = ""
                        url = ""
                        fecha= ""
                        Toast.makeText(context,"Peicula añadida",Toast.LENGTH_LONG).show()
                    }
                    catch (e: Exception)
                    {

                        Toast.makeText(context,"Error al añadir",Toast.LENGTH_LONG).show()
                    }
                }else
                    Toast.makeText(context,"Debe rellenar todos los campos",Toast.LENGTH_LONG).show()
            }
        )

    }
}

@Composable
fun Espacio(){
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun Boton(
    anadir:() -> Unit
){
    Button(
        onClick = {
            anadir()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp))
    {
        Text(text = "Añadir")


    }
}

@ExperimentalFoundationApi
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = NavegacionItem.Home.route) {

        composable(NavegacionItem.Home.route) {
            HomeScreen()
        }

        composable(NavegacionItem.Add.route) {
            AddScreeen()
        }
    }

}

fun insertar(titulo:String,descripcion:String,url:String,fecha:String){

    val url = "http://iesayala.ddns.net/sergioRuiz/insertPelicula.php/?titulo=$titulo&descripcion=$descripcion&url=$url&fecha=$fecha"
    leerUrl(url)
}

fun borrar(id:Int){

    val url = "http://iesayala.ddns.net/sergioRuiz/deletePelicula.php/?id=$id"
    leerUrl(url)
}


fun leerUrl(urlString:String){
    GlobalScope.launch(Dispatchers.IO)   {
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        } catch (e: Exception) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        }
    }

    return
}

@Composable
fun cargarJson(): FilmInfo {

    var films by rememberSaveable { mutableStateOf(FilmInfo()) }
    val film = FilmInstance.filmInterface.filmInformation()

    film.enqueue(object : Callback<FilmInfo> {
        override fun onResponse(
            call: Call<FilmInfo>,
            response: Response<FilmInfo>
        ) {
            val filmInfo: FilmInfo? = response.body()
            if (filmInfo != null) {
                films = filmInfo
            }
        }

        override fun onFailure(call: Call<FilmInfo>, t: Throwable)
        {
            //Error
        }

    })
    return films
}


@ExperimentalFoundationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    NavigationDrawerTheme {
        MainScreen()
    }
}