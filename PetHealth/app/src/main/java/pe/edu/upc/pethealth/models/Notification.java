package pe.edu.upc.pethealth.models;

import android.os.Bundle;

/**
 * Created by genob on 29/09/2017.
 */

public class Notification {
    private String notificationtittle;
    private String notificationcontent;

    public Notification() {
    }

    public Notification(String notificationtittle, String notificationcontent) {
        this.notificationtittle = notificationtittle;
        this.notificationcontent = notificationcontent;
    }

    public String getNotificationtittle() {
        return notificationtittle;
    }

    public Notification setNotificationtittle(String notificationtittle) {
        this.notificationtittle = notificationtittle;
        return  this;
    }

    public String getNotificationcontent() {
        return notificationcontent;
    }

    public Notification setNotificationcontent(String notificationcontent) {
        this.notificationcontent = notificationcontent;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("notificationtittle",notificationtittle);
        bundle.putString("notificationcontent",notificationcontent);
        return bundle;
    }

    public static Notification from(Bundle bundle){
        Notification notification = new Notification();
        notification.setNotificationtittle(bundle.getString("notificationtittle"))
                .setNotificationcontent(bundle.getString("notificationcontent"));
        return notification;
    }
}
