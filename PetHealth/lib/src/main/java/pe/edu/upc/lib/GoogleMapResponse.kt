package pe.edu.upc.lib

data class GoogleMapResponse (
        var routes: ArrayList<Routes>
)

data class Routes (
    var legs : ArrayList<Legs>
)

data class Legs (
    var distance : Distance,
    var duration : Duration,
    var end_address : String,
    var start_address : String,
    var end_location :Location,
    var start_location : Location,
    var steps : ArrayList<Steps>
)

data class Steps (
    var distance : Distance,
    var duration : Duration,
    var end_address : String,
    var start_address : String,
    var end_location :Location,
    var start_location : Location,
    var polyline : PolyLine,
    var travel_mode : String,
    var maneuver : String
)

data class Duration (
    var text : String,
    var value : Int
)

data class Distance (
    var text : String,
    var value : Int
)

data class PolyLine (
    var points : String
)

data class Location(
    var lat :String,
    var lng :String
)