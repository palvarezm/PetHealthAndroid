package pe.edu.upc.pethealth.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.models.Chat;

/**
 * Created by genob on 29/09/2017.
 */

public class ChatsRepository {
    public static List<Chat> getChats(){
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat(1,1,"Doc tores","hola"));
        chats.add(new Chat(1,2,"Pet Health","message"));
        chats.add(new Chat(1,3,"Dr","message"));
        chats.add(new Chat(1,4,"Dr Vet","message"));
        chats.add(new Chat(1,5,"Verinary 5","message"));
        chats.add(new Chat(1,6,"Verinary 6","message"));
        chats.add(new Chat(1,7,"Verinary 7","message"));
        chats.add(new Chat(1,8,"Verinary 8","message"));
        chats.add(new Chat(1,9,"Verinary 9","message"));
        return chats;
    }
}
