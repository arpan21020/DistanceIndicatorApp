package com.example.assignment1

data class UserStops constructor(val id: Int, val name: String, val distance:Float)

val userStopsList = arrayListOf(
    UserStops(1, "Central Park", 10F),
    UserStops(2, "Times Square", 15F),
    UserStops(3, "Empire State Building", 20F),
    UserStops(4, "Statue of Liberty", 30F),
    UserStops(5, "Brooklyn Bridge", 25F),
    UserStops(6, "Grand Central Terminal", 12F),
    UserStops(7, "Metropolitan Museum of Art", 18F),
    UserStops(8, "One World Trade Center", 28F),
    UserStops(9, "The High Line", 8F),
    UserStops(10, "Broadway", 22F),
    UserStops(11, "Rockefeller Center", 17F),
    UserStops(12, "Museum of Modern Art", 21F),
    UserStops(13, "Central Park Zoo", 11F),
    UserStops(14, "Brooklyn Botanic Garden", 27F),
    UserStops(15, "New York Public Library", 14F),


)