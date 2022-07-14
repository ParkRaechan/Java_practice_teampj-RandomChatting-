package RandomChatting.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    public static JSONArray usersLocations=new JSONArray(); // 유저들의 위치 정보 항상 저장
    public JSONArray getUsersLocation(String x, String y, String gender, String name){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("x",x);
        jsonObject.put("y",y);
        jsonObject.put("gender",gender);
        jsonObject.put("name",name);
        usersLocations.put(jsonObject);
        return usersLocations;
    }
}
