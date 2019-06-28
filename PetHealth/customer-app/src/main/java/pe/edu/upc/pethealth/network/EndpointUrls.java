package pe.edu.upc.pethealth.network;

public interface EndpointUrls {
    String BASE_URL = "https://pethealthapi.herokuapp.com/api/";
    String LOGIN ="login";
    String APPOINTMENTS = "users/{user_id}/appointments";
    String VETERINARIES = "veterinaries";
}
