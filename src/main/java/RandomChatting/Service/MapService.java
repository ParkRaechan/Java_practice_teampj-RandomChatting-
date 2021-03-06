package RandomChatting.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    public static JSONArray usersLocations=new JSONArray(); // 유저들의 위치 정보 항상 저장

    //usersLocations 정보 리턴하는 메소드
    public JSONArray getUsersLocation(String x, String y, String gender, String name){
        JSONObject jsonObject=new JSONObject();

        jsonObject.put("x",x);
        jsonObject.put("y",y);
        jsonObject.put("gender",gender);
        jsonObject.put("name",name);
        usersLocations.put(jsonObject);
        return usersLocations;
    }

    //거리 가까운 순서대로 다른 유저들 정렬해서 반환하는 메소드
//    public ArrayList<User> nearUsers(String x, String y){
//        ArrayList<User> nearsUsersList=new ArrayList<>();
//        double x1=Double.parseDouble(x); // 내 x좌표
//        double y1=Double.parseDouble(y); // 내 y좌표
//        for(int i=0; i<usersLocations.length(); i++){
//         JSONObject user=(JSONObject) usersLocations.get(i);
//         double x2=user.getDouble("x"); // 비교상대 x좌표
//         double y2=user.getDouble("y"); // 비교상대 y좌표
//         double dis=distance(x1,y1,x2,y2,"kilometer"); // 거리 구하기
//            User userobject=new User();
//            //이름 설정
//            userobject.setUName((String)((JSONObject) usersLocations.get(i)).get("name"));
//            //성별 설정
//            userobject.setUGender((String)((JSONObject) usersLocations.get(i)).get("gender"));
//            //거리 설정
//            userobject.setUDistance(dis);
//            nearsUsersList.add(userobject);
//        }
//            //거리순 정렬
//        Collections.sort(nearsUsersList, (o1, o2) -> {
//            double testint1 = o1.getUDistance();
//            double testint2 = o2.getUDistance();
//
//            if(testint1 > testint2){
//                return 1;
//            }else if(testint1 < testint2){
//                return -1;
//            }else{
//                return 0;
//            }
//        });
//        return nearsUsersList;
//    }

    //거리 계산하는 메소드
    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

//        if (unit == "kilometer") {
//            dist = dist * 1.609344;
//        } else if(unit == "meter"){
//            dist = dist * 1609.344;
//        }
        //킬로미터로 통일
        dist = dist * 1.609344;
        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
