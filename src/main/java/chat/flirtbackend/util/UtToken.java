package chat.flirtbackend.util;

public class UtToken {

    public static String removeBearer(String authorization) {
        return authorization.replace("Bearer ", "");
    }

}
