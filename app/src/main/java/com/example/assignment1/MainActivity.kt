package com.example.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment1.ui.theme.Assignment1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment1Theme {
                // A surface container using the 'background' color from the theme
                HomePage()

            }
        }
    }
}




@Composable
fun HomePage(userStops: List<UserStops> = userStopsList){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination ="main" ){
        composable("main"){
            Home(navController)
        }
        composable("normalColumn"){
            MainScreen(userStops,navController)
        }
        composable("lazyColumn")
        {
            MainScreen2(userStops,navController)

        }
    }
}
@Composable
fun Home(navController:NavHostController? ){
    Column(modifier=Modifier.fillMaxSize()
    ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController?.navigate("normalColumn") }) {
            Text(text = "NormalColumn")

        }
        Button(onClick = { navController?.navigate("lazyColumn")}) {
            Text(text = "LazyColumn")

        }

    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(userStops: List<UserStops>,navController: NavHostController? ) {
    var totalDistance:Float=0F;
    for(i in userStops){
        totalDistance+=i.distance;
    }
    val distanceCovered= remember {
        mutableStateOf(0F)
    }
    val distanceLeft= remember {
        mutableStateOf(totalDistance)
    }
    val progress= remember {
        mutableStateOf(0F)
    }
    val isKilometer= remember {
        mutableStateOf(true)
    }
    val scrollState = rememberScrollState()
    val scope= rememberCoroutineScope()
    Scaffold(topBar = { Appbar(title = "Distance App", icon = Icons.Default.Home, {}) }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ){
            Column(modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)

            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f),
//                    .background(Color.Green),
                    contentAlignment = Alignment.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally

//                    .height(400.dp)

                ){
                    Column(modifier=Modifier.verticalScroll(scrollState)) {
                        for (stop in userStops){
                            DisplayStop(
                                name = stop.name,
                                distance = stop.distance,
                                id = stop.id,
                                progress = progress.value,
                                isKilometer =isKilometer.value
                            )
                        }
                        
                    }
//                    LazyColumn(
//                        state = listState
//                    ){
//                        items(userStops){userStop->
//                            DisplayStop(userStop.name,userStop.distance,userStop.id,progress.value,isKilometer.value)
//
//                        }
//
//                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .background(Color.LightGray)
                    .weight(2f)){
                    Column() {
                        ProgressIndicator(progress.value,userStops.size.toFloat())
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color.White)
                            .padding(10.dp)

                            ,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(text = if(isKilometer.value) "Total distance Covered : ${distanceCovered.value} Km" else "Total distance Covered : ${distanceCovered.value} miles")
                            Text(text = if(isKilometer.value) "Total distance Left : ${distanceLeft.value} Km" else "Total distance left : ${distanceLeft.value} miles")

                        }
                        Row(
                            modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                if(distanceLeft.value>0){

                                    val offset:Int=6;
                                    progress.value++;
                                    if(isKilometer.value){
                                        distanceCovered.value+=userStops[progress.value.toInt()-1].distance
                                        distanceLeft.value-=userStops[progress.value.toInt()-1].distance
                                    }else{
                                        distanceCovered.value+=(userStops[progress.value.toInt()-1].distance*0.621371F)
                                        distanceLeft.value-=(userStops[progress.value.toInt()-1].distance*0.621371F)
                                    }
                                    distanceCovered.value = "%.2f".format(distanceCovered.value).toFloat()
                                    distanceLeft.value = "%.2f".format(distanceLeft.value).toFloat()
                                }

//                                scope.launch {
////                                    scrollState.animateScrollTo(if(progress.value.toInt()>offset) (progress.value.toInt()-offset) else 0)
//                                    scrollState.animateScrollTo(13)
//                                }
                            }) {
                                Text(text = "Next Stop")

                            }

                            Button(onClick = {
                                isKilometer.value = !isKilometer.value
                                if(isKilometer.value){
                                    distanceCovered.value*=1.609344F;
                                    distanceLeft.value*=1.609344F;
                                }
                                else{
                                    distanceCovered.value*=0.621371F;
                                    distanceLeft.value*=0.621371F;
                                }
                                distanceCovered.value = "%.2f".format(distanceCovered.value).toFloat()
                                distanceLeft.value = "%.2f".format(distanceLeft.value).toFloat()
                            }) {

                                if(isKilometer.value){
                                    Text(text = "Show in miles")
                                }
                                else {Text(text = "Show in Km")}

                            }
                        }

                    }
                }

            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen2(userStops: List<UserStops>,navController: NavHostController? ) {
    var totalDistance:Float=0F;
    for(i in userStops){
        totalDistance+=i.distance;
    }
    val distanceCovered= remember {
        mutableStateOf(0F)
    }
    val distanceLeft= remember {
        mutableStateOf(totalDistance)
    }
    val progress= remember {
        mutableStateOf(0F)
    }
    val isKilometer= remember {
        mutableStateOf(true)
    }
    val listState = rememberLazyListState()
    val scope= rememberCoroutineScope()
    Scaffold(topBar = { Appbar(title = "Distance App", icon = Icons.Default.Home, {}) }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ){
            Column(modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)

            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f),
//                    .background(Color.Green),
                    contentAlignment = Alignment.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally

//                    .height(400.dp)

                ){
                    LazyColumn(
                        state = listState
                    ){
                        items(userStops){userStop->
                            DisplayStop(userStop.name,userStop.distance,userStop.id,progress.value,isKilometer.value)

                        }

                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .background(Color.LightGray)
                    .weight(2f)){
                    Column() {
                        ProgressIndicator(progress.value,userStops.size.toFloat())
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color.White)
                            .padding(10.dp)

                            ,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(text = if(isKilometer.value) "Total distance Covered : ${distanceCovered.value} Km" else "Total distance Covered : ${distanceCovered.value} miles")
                            Text(text = if(isKilometer.value) "Total distance Left : ${distanceLeft.value} Km" else "Total distance left : ${distanceLeft.value} miles")

                        }
                        Row(
                            modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                if(distanceLeft.value>0){
                                    val offset:Int=6;
                                    progress.value++;
                                    if(isKilometer.value){
                                        distanceCovered.value+=userStops[progress.value.toInt()-1].distance
                                        distanceLeft.value-=userStops[progress.value.toInt()-1].distance
                                    }else{
                                        distanceCovered.value+=(userStops[progress.value.toInt()-1].distance*0.621371F)
                                        distanceLeft.value-=(userStops[progress.value.toInt()-1].distance*0.621371F)
                                    }
                                    distanceCovered.value = "%.2f".format(distanceCovered.value).toFloat()
                                    distanceLeft.value = "%.2f".format(distanceLeft.value).toFloat()
                                    scope.launch {
                                        listState.animateScrollToItem(if(progress.value.toInt()>offset) (progress.value.toInt()-offset) else 0)
                                    }

                                }
                            }) {
                                Text(text = "Next Stop")

                            }

                            Button(onClick = {
                                isKilometer.value = !isKilometer.value
                                if(isKilometer.value){
                                    distanceCovered.value*=1.609344F;
                                    distanceLeft.value*=1.609344F;
                                }
                                else{
                                    distanceCovered.value*=0.621371F;
                                    distanceLeft.value*=0.621371F;
                                }
                                distanceCovered.value = "%.2f".format(distanceCovered.value).toFloat()
                                distanceLeft.value = "%.2f".format(distanceLeft.value).toFloat()
                            }) {

                                if(isKilometer.value){
                                    Text(text = "Show in miles")
                                }
                                else {Text(text = "Show in Km")}

                            }
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun ProgressIndicator(progress:Float,size:Float){
    val incrementTo=progress/size;

    LinearProgressIndicator(
        progress=incrementTo,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .height(20.dp)
            .background(Color.White)

        ,
        color=Color.Green,
    )
}

@Composable
fun Appbar(title:String, icon: ImageVector, backAction:()->Unit){
    TopAppBar(
        navigationIcon= {
            Icon(imageVector = icon,"Content Description",
                Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = { backAction.invoke() })
            )
        },
        title = {Text(title)}

    )
}

@Composable
fun DisplayStop(name:String,distance :Float,id:Int,progress: Float,isKilometer:Boolean){
    var miles:Float=distance*0.621371F;
    miles=".2f".format(miles).toFloat()
    Column(modifier = Modifier) {

        Card(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
            ,

            elevation = 10.dp,
            shape= RoundedCornerShape(20.dp)
        )
        {
            Column(modifier= Modifier
                .background(if (id <= progress.toInt()) Color.Yellow else Color.LightGray)
                .padding(10.dp)

            ) {
                Text(text = name)
                if(isKilometer)Text(text = "Distance ${distance.toInt()} Km") else Text(text = "Distance ${miles} miles")

            }
        }

    }


}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assignment1Theme {
//        MainScreen(userStops = userStopsList,null)
        Home(null)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Assignment1Theme {
        MainScreen(userStops= userStopsList,null)
    }
}