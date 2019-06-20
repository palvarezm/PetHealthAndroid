package pe.edu.upc.pethealth.network;

public interface EndpointUrls {
    String BASE_URL = "https://pethealthapi.herokuapp.com/api/";
    String LOGIN ="login";
    String APPOINTMENTS = "user/{user_id}/appts";
    String VETERINARIES = "veterinaries";
}
