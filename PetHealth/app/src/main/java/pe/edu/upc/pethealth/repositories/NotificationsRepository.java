package pe.edu.upc.pethealth.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.models.Notification;

/**
 * Created by genob on 29/09/2017.
 */

public class NotificationsRepository {
    public static List<Notification> getNotifications(){
        List<Notification> notifications=new ArrayList<>();
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Desparasitacion en dos dias"));
        notifications.add(new Notification("Importante","Corte en dos dias"));
        notifications.add(new Notification("Cumpleaños","Celebra el cumpleaños de tu mascota"));
        notifications.add(new Notification("Recordatorio","Control en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        notifications.add(new Notification("Recordatorio","Vacuna en dos dias"));
        return notifications;
    }
}
