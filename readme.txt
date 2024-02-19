Distance Tracking App
This is a simple Android application developed using Jetpack Compose for tracking distances covered and remaining between user-defined stops.

Features
Navigation: The app supports navigation between different screens using Jetpack Navigation Components of LazyColumn and Normal Column.
 
Display: Users can view their stops and the distances between them, along with the total distance covered and remaining.
A button will Update the progress.

Progress Indicator: A linear progress indicator shows the overall progress of the journey.
Unit Conversion: Users can switch between displaying distances in kilometers or miles.

distanceCovered,distanceLeft, progress is initalizeed using mutableStateof()

isKilometer is initalaized as boolean value to take care of whether the units to display in km or miles

in the mainpage a column Container contains two Box() upper box shows the Stops and bottom box contain the progressbar and buttons for user interaction.

on pressing Next Stop Button , it will increment the progress by one and also modifies the distanceCovered and distanceLeft

on pressing Show miles , it will change the state of isKilometer from true to false and vice versa and do the conversion of distances