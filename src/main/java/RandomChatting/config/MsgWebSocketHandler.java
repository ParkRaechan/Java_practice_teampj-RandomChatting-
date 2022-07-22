package RandomChatting.config;

import RandomChatting.service.MapService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgWebSocketHandler extends TextWebSocketHandler {

    // 접속된 세션의 리스트 [ 세션 , 회원ID ]
    private Map< WebSocketSession , JSONArray> list = new HashMap<>();
    MapService mapService = new MapService();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 경로에서 아이디 추출
        String path = session.getUri().getPath();
        String mid = path.substring( path.lastIndexOf("/") +1 );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from",mid);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        list.put( session , jsonArray );
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        list.remove( session );
    }
    @Override
    protected void handleTextMessage( WebSocketSession session, TextMessage message) throws Exception {
        JSONObject object = new JSONObject( message.getPayload() ); // Payload() : 메시지내용
        //System.out.println(object.length());
        if(object.length()==8){
            for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출
                if( (((list.get( socketSession)).getJSONObject(0)).get("from")).equals( object.get("from")  ) ){
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from",object.get("from"));
                    jsonObject.put("tsex",object.get("tsex"));
                    jsonObject.put("ysex",object.get("ysex"));
                    jsonObject.put("locationX",object.get("locationX"));
                    jsonObject.put("locationY",object.get("locationY"));
                    jsonObject.put("yname",object.get("yname"));
                    jsonObject.put("yage",object.get("yage"));
                    jsonObject.put("inchat","");
                    jsonArray.put(jsonObject);
                    list.put( session , jsonArray );
                    //System.out.println(list.get( socketSession));
                }
            }
        }else{
            //System.out.println(session);
            String continue_target = "666"; //채팅방유무(상대아이디적혀있음)
            String continue_tsex = "666";   //타겟성별
            String continue_ysex = "666";   //자기성별
            String continue_locationX = "666";  //x좌표
            String continue_locationY = "666";  //y좌표
            String continue_yname = "666";  //닉네임
            String continue_yage = "666";   //나이

            for (WebSocketSession socketSession99 : list.keySet()) {
                if (list.get(socketSession99).getJSONObject(0).get("from").equals(object.get("from"))) {
                    continue_target = (String) list.get(socketSession99).getJSONObject(0).get("inchat");
                    continue_tsex = (String) list.get(socketSession99).getJSONObject(0).get("tsex");
                    continue_ysex = (String) list.get(socketSession99).getJSONObject(0).get("ysex");
                    //맞는 사람들끼리 채팅방 만들기 알고리즘에 필요한 값들 받아오기
                    continue_locationX = (String) list.get(socketSession99).getJSONObject(0).get("locationX");
                    continue_locationY = (String) list.get(socketSession99).getJSONObject(0).get("locationY");
                    continue_yname = (String) list.get(socketSession99).getJSONObject(0).get("yname");
                    continue_yage = (String) list.get(socketSession99).getJSONObject(0).get("yage");
                }
            }

            Map< WebSocketSession , JSONArray> list1 = new HashMap<>();     //나이맞는애들모음
            Map< WebSocketSession , JSONArray> list2 = new HashMap<>();     //나이틀린애들모음
            int theEnd = 0;

            if(continue_target.equals("")) {                                //내 자신이 채팅방에 들어가 있지 않은 경우
                for (WebSocketSession socketSession : list.keySet()) {    // 모든 키값 호출
                    if (!((String) (((list.get(socketSession)).getJSONObject(0)).get("from"))).equals(object.get("from"))) {    //자신제외  대상 검색
                        if (((String) (((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {               //채팅방 있는 놈들 제외 대상 검색
                            int index_age = Integer.parseInt((String) (((list.get(socketSession)).getJSONObject(0)).get("yage")));  //상대 나이 가져오기

                            if (index_age <= Integer.parseInt(continue_yage) + 1 && index_age >= Integer.parseInt(continue_yage) - 1) { //나이맞을시

                                //상대방 아이디
                                String t1 = (String) ((list.get(socketSession)).getJSONObject(0)).get("from");
                                //상대방 타겟성별
                                String t2 = (String) ((list.get(socketSession)).getJSONObject(0)).get("tsex");
                                //상대방 자신성별
                                String t3 = (String) ((list.get(socketSession)).getJSONObject(0)).get("ysex");
                                //상대방 닉네임
                                String t4 = (String) ((list.get(socketSession)).getJSONObject(0)).get("yname");
                                //상대방 나이
                                String t5 = (String) ((list.get(socketSession)).getJSONObject(0)).get("yage");
                                String t6 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationX")));
                                String t7 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationY")));

                                //나이맞는대상모음
                                JSONArray jsonArray2 = new JSONArray();
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("from", t1);
                                jsonObject2.put("tsex", t2);
                                jsonObject2.put("ysex", t3);
                                jsonObject2.put("inchat", "");
                                jsonObject2.put("yname", t4);
                                jsonObject2.put("yage", t5);
                                jsonObject2.put("locationX", t6);
                                jsonObject2.put("locationY", t7);
                                jsonArray2.put(jsonObject2);
                                list1.put(socketSession, jsonArray2);

                            }else{                                                                                                      //나이틀릴시
                                //상대방 아이디
                                String t1 = (String) ((list.get(socketSession)).getJSONObject(0)).get("from");
                                //상대방 타겟성별
                                String t2 = (String) ((list.get(socketSession)).getJSONObject(0)).get("tsex");
                                //상대방 자신성별
                                String t3 = (String) ((list.get(socketSession)).getJSONObject(0)).get("ysex");
                                //상대방 닉네임
                                String t4 = (String) ((list.get(socketSession)).getJSONObject(0)).get("yname");
                                //상대방 나이
                                String t5 = (String) ((list.get(socketSession)).getJSONObject(0)).get("yage");
                                String t6 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationX")));
                                String t7 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationY")));

                                //나이맞는대상모음
                                JSONArray jsonArray2 = new JSONArray();
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("from", t1);
                                jsonObject2.put("tsex", t2);
                                jsonObject2.put("ysex", t3);
                                jsonObject2.put("inchat", "");
                                jsonObject2.put("yname", t4);
                                jsonObject2.put("yage", t5);
                                jsonObject2.put("locationX", t6);
                                jsonObject2.put("locationY", t7);
                                jsonArray2.put(jsonObject2);
                                list2.put(socketSession, jsonArray2);
                                //System.out.println(list2);
                            }
                        }
                    }
                }
            }else{// 자신이 채팅방에 들어가있다면
                int index99 = 0;
                for(WebSocketSession socketSession99 : list.keySet()) {
                    if (((String) list.get(socketSession99).getJSONObject(0).get("from")).equals(continue_target)) {
                        socketSession99.sendMessage(message);
                        break;
                    }else{
                        index99++;
                    }
                    if(index99==list.size()){       //상대방이 나갔을때
                        CharSequence alert2 = "상대방이 나갔습니다.";
                        TextMessage message_2u = new TextMessage(alert2);
                        session.sendMessage(message_2u);
                        break;
                    }
                }
                theEnd =1;
            }

            Map< WebSocketSession , JSONArray> list3 = new HashMap<>();     //나이맞는, 성별맞는 애들모음
            Map< WebSocketSession , JSONArray> list4 = new HashMap<>();     //나이맞는, 성별틀린 애들모음
            Map< WebSocketSession , JSONArray> list5 = new HashMap<>();     //나이틀린, 나이증가후 나이맞는, 성별맞는 애들모음
            Map< WebSocketSession , JSONArray> list6 = new HashMap<>();     //나이틀린, 나이증가후 나이맞는, 성별틀린 애들모음
                                                                            //나이틀린, 나이증가후 나이안맞는 애들은 알람띄우기



            //나이맞는 놈들 중에서
            if(list1.size()!=0) {           //나이맞는놈 한명이라도 있다면
                //System.out.println(list1);
                if(list1.size()!=0) {
                    for (WebSocketSession socketSession : list1.keySet()) {
                        if (((String) (((list1.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {     //성별 맞다면
                            //상대방 아이디
                            String t1 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("from");
                            //상대방 타겟성별
                            String t2 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("tsex");
                            //상대방 자신성별
                            String t3 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("ysex");
                            //상대방 닉네임
                            String t4 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("yname");
                            //상대방 나이
                            String t5 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("yage");
                            String t6 = ((String) (((list1.get(socketSession)).getJSONObject(0)).get("locationX")));
                            String t7 = ((String) (((list1.get(socketSession)).getJSONObject(0)).get("locationY")));

                            //나이맞는대상모음
                            JSONArray jsonArray2 = new JSONArray();
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("from", t1);
                            jsonObject2.put("tsex", t2);
                            jsonObject2.put("ysex", t3);
                            jsonObject2.put("inchat", "");
                            jsonObject2.put("yname", t4);
                            jsonObject2.put("yage", t5);
                            jsonObject2.put("locationX", t6);
                            jsonObject2.put("locationY", t7);
                            jsonArray2.put(jsonObject2);
                            list3.put(socketSession, jsonArray2);

                        } else {                                                                                                    //성별 안맞다면
                            //System.out.println(socketSession);
                            //상대방 아이디
                            String t1 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("from");
                            //상대방 타겟성별
                            String t2 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("tsex");
                            //상대방 자신성별
                            String t3 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("ysex");
                            //상대방 닉네임
                            String t4 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("yname");
                            //상대방 나이
                            String t5 = (String) ((list1.get(socketSession)).getJSONObject(0)).get("yage");
                            String t6 = ((String) (((list1.get(socketSession)).getJSONObject(0)).get("locationX")));
                            String t7 = ((String) (((list1.get(socketSession)).getJSONObject(0)).get("locationY")));

                            //나이맞는대상모음
                            JSONArray jsonArray2 = new JSONArray();
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("from", t1);
                            jsonObject2.put("tsex", t2);
                            jsonObject2.put("ysex", t3);
                            jsonObject2.put("inchat", "");
                            jsonObject2.put("yname", t4);
                            jsonObject2.put("yage", t5);
                            jsonObject2.put("locationX", t6);
                            jsonObject2.put("locationY", t7);
                            jsonArray2.put(jsonObject2);
                            list4.put(socketSession, jsonArray2);
                        }
                    }
                }
            }else{  //나이 맞는 놈들 아예 없을시
                //System.out.println(list2);
                if(list2.size()!=0) {
                    for (WebSocketSession socketSession : list2.keySet()) {
                        int qwer = 0;
                        int index_age2 = Integer.parseInt((String) (((list2.get(socketSession)).getJSONObject(0)).get("yage")));  //상대 나이 가져오기
                        if (index_age2 <= Integer.parseInt(continue_yage) + 2 && index_age2 >= Integer.parseInt(continue_yage) - 2) { //나이증가 --- 나이맞을시
                            if (((String) (((list2.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {     //성별 맞다면
                                //상대방 아이디
                                String t1 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("from");
                                //상대방 타겟성별
                                String t2 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("tsex");
                                //상대방 자신성별
                                String t3 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("ysex");
                                //상대방 닉네임
                                String t4 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("yname");
                                //상대방 나이
                                String t5 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("yage");
                                String t6 = ((String) (((list2.get(socketSession)).getJSONObject(0)).get("locationX")));
                                String t7 = ((String) (((list2.get(socketSession)).getJSONObject(0)).get("locationY")));

                                //나이맞는대상모음
                                JSONArray jsonArray2 = new JSONArray();
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("from", t1);
                                jsonObject2.put("tsex", t2);
                                jsonObject2.put("ysex", t3);
                                jsonObject2.put("inchat", "");
                                jsonObject2.put("yname", t4);
                                jsonObject2.put("yage", t5);
                                jsonObject2.put("locationX", t6);
                                jsonObject2.put("locationY", t7);
                                jsonArray2.put(jsonObject2);
                                list5.put(socketSession, jsonArray2);
                            } else {                                                                                                          //성별틀릴시
                                //상대방 아이디
                                String t1 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("from");
                                //상대방 타겟성별
                                String t2 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("tsex");
                                //상대방 자신성별
                                String t3 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("ysex");
                                //상대방 닉네임
                                String t4 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("yname");
                                //상대방 나이
                                String t5 = (String) ((list2.get(socketSession)).getJSONObject(0)).get("yage");
                                String t6 = ((String) (((list2.get(socketSession)).getJSONObject(0)).get("locationX")));
                                String t7 = ((String) (((list2.get(socketSession)).getJSONObject(0)).get("locationY")));

                                //나이맞는대상모음
                                JSONArray jsonArray2 = new JSONArray();
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("from", t1);
                                jsonObject2.put("tsex", t2);
                                jsonObject2.put("ysex", t3);
                                jsonObject2.put("inchat", "");
                                jsonObject2.put("yname", t4);
                                jsonObject2.put("yage", t5);
                                jsonObject2.put("locationX", t6);
                                jsonObject2.put("locationY", t7);
                                jsonArray2.put(jsonObject2);
                                list6.put(socketSession, jsonArray2);
                            }
                        } else {                                      //나이증가 --- 나이안맞을시
                            qwer++;
                        }
                        if (qwer == list2.size()) {                 //나이증가해도 전부 안 맞을시
                            if (theEnd == 0) {
                                CharSequence alert2_4 = "찾는 사람이 없습니다";
                                TextMessage message_2u_4 = new TextMessage(alert2_4);
                                session.sendMessage(message_2u_4);
                                theEnd = 1;
                                break;
                            }
                        }
                    }
                }
            }

            Map< WebSocketSession , JSONArray> list99_1 = new HashMap<>();     //거리-1
            Map< WebSocketSession , JSONArray> list99_1_n = new HashMap<>();     //거리-1틀린
            Map< WebSocketSession , JSONArray> list99_2 = new HashMap<>();     //거리-2
            Map< WebSocketSession , JSONArray> list99_2_n = new HashMap<>();     //거리-2틀린
            Map< WebSocketSession , JSONArray> list99_3 = new HashMap<>();     //거리-3
            Map< WebSocketSession , JSONArray> list99_3_n = new HashMap<>();     //거리-3틀린
            Map< WebSocketSession , JSONArray> list99_4 = new HashMap<>();     //거리-4
            Map< WebSocketSession , JSONArray> list99_4_n = new HashMap<>();     //거리-4틀린

            if(theEnd==0) {//걸렸으면 나가삼
                //나이맞는, 성별맞는, 거리맞는 애들은 바로 매치

                //나이맞는, 성별맞는, 거리틀린 애들 거리증가후 거리맞은 애들 매치
                //나이맞는, 성별맞는, 거리틀린 애들 거리증가후 거리맞은 애들 알람

                if (list3.size() != 0) {           //나이맞는,성별맞는 한명이라도 있다면
                    //System.out.println("qwe");
                    for (WebSocketSession socketSession : list3.keySet()) {
                        double qq1 = Double.parseDouble(continue_locationX);
                        double qq2 = Double.parseDouble(continue_locationY);
                        double qq3 = Double.parseDouble(((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationX"))));
                        double qq4 = Double.parseDouble(((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationY"))));
                        double qqq = mapService.distance(qq1, qq2, qq3, qq4);

                        if (qqq < 1) {                 //나이맞는, 성별맞는, 거리맞는 애들 -> 바로 매치
                            if(theEnd==0) {
                                //상대방 아이디
                                String t1 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("from");
                                //상대방 타겟성별
                                String t2 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("tsex");
                                //상대방 자신성별
                                String t3 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("ysex");
                                //상대방 닉네임
                                String t4 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yname");
                                //상대방 나이
                                String t5 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yage");
                                String t6 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationX")));
                                String t7 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationY")));

                                //나이맞는대상모음
                                JSONArray jsonArray2 = new JSONArray();
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("from", t1);
                                jsonObject2.put("tsex", t2);
                                jsonObject2.put("ysex", t3);
                                jsonObject2.put("inchat", "");
                                jsonObject2.put("yname", t4);
                                jsonObject2.put("yage", t5);
                                jsonObject2.put("locationX", t6);
                                jsonObject2.put("locationY", t7);
                                jsonArray2.put(jsonObject2);
                                list99_1.put(socketSession, jsonArray2);










//                                //상대방 아이디
//                                String t1 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("from");
//                                //상대방 타겟성별
//                                String t2 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("tsex");
//                                //상대방 자신성별
//                                String t3 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("ysex");
//                                //상대방 닉네임
//                                String t4 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yname");
//                                //상대방 나이
//                                String t5 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yage");
//                                String t6 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                String t7 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                //자기 아이디
//                                String y1 = (String) object.get("from");
//                                //타겟 성별
//                                String y2 = continue_tsex;
//                                //자기 성별
//                                String y3 = continue_ysex;
//                                //자기 닉네임
//                                String y4 = continue_yname;
//                                //자기 나이
//                                String y5 = continue_yage;
//                                String y6 = continue_locationX;
//                                String y7 = continue_locationY;
//
//                                //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                JSONArray jsonArray2 = new JSONArray();
//                                JSONObject jsonObject2 = new JSONObject();
//                                jsonObject2.put("from", t1);
//                                jsonObject2.put("tsex", t2);
//                                jsonObject2.put("ysex", t3);
//                                jsonObject2.put("inchat", y1);
//                                jsonObject2.put("yname", t4);
//                                jsonObject2.put("yage", t5);
//                                jsonObject2.put("locationX", t6);
//                                jsonObject2.put("locationY", t7);
//                                jsonArray2.put(jsonObject2);
//                                list.put(socketSession, jsonArray2);
//
//                                //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                JSONArray jsonArray3 = new JSONArray();
//                                JSONObject jsonObject3 = new JSONObject();
//                                jsonObject3.put("from", y1);
//                                jsonObject3.put("tsex", y2);
//                                jsonObject3.put("ysex", y3);
//                                jsonObject3.put("inchat", t1);
//                                jsonObject3.put("yname", y4);
//                                jsonObject3.put("yage", y5);
//                                jsonObject3.put("locationX", y6);
//                                jsonObject3.put("locationY", y7);
//                                jsonArray3.put(jsonObject3);
//                                list.put(session, jsonArray3);
//
//                                //메세지 보내기
//                                socketSession.sendMessage(message);
//                                theEnd = 1;
//                                break;
                            }
                        } else {                      //나이맞는, 성별맞는, 거리틀린 애들
                            if (qqq < 100) {
                                if(theEnd==0) {

                                    //상대방 아이디
                                    String t1 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("from");
                                    //상대방 타겟성별
                                    String t2 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("tsex");
                                    //상대방 자신성별
                                    String t3 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("ysex");
                                    //상대방 닉네임
                                    String t4 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yname");
                                    //상대방 나이
                                    String t5 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yage");
                                    String t6 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationX")));
                                    String t7 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationY")));

                                    //나이맞는대상모음
                                    JSONArray jsonArray2 = new JSONArray();
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("from", t1);
                                    jsonObject2.put("tsex", t2);
                                    jsonObject2.put("ysex", t3);
                                    jsonObject2.put("inchat", "");
                                    jsonObject2.put("yname", t4);
                                    jsonObject2.put("yage", t5);
                                    jsonObject2.put("locationX", t6);
                                    jsonObject2.put("locationY", t7);
                                    jsonArray2.put(jsonObject2);
                                    list99_1_n.put(socketSession, jsonArray2);







//                                    //상대방 아이디
//                                    String t1 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("from");
//                                    //상대방 타겟성별
//                                    String t2 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("tsex");
//                                    //상대방 자신성별
//                                    String t3 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("ysex");
//                                    //상대방 닉네임
//                                    String t4 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yname");
//                                    //상대방 나이
//                                    String t5 = (String) ((list3.get(socketSession)).getJSONObject(0)).get("yage");
//                                    String t6 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                    String t7 = ((String) (((list3.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                    //자기 아이디
//                                    String y1 = (String) object.get("from");
//                                    //타겟 성별
//                                    String y2 = continue_tsex;
//                                    //자기 성별
//                                    String y3 = continue_ysex;
//                                    //자기 닉네임
//                                    String y4 = continue_yname;
//                                    //자기 나이
//                                    String y5 = continue_yage;
//                                    String y6 = continue_locationX;
//                                    String y7 = continue_locationY;
//
//                                    //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                    JSONArray jsonArray2 = new JSONArray();
//                                    JSONObject jsonObject2 = new JSONObject();
//                                    jsonObject2.put("from", t1);
//                                    jsonObject2.put("tsex", t2);
//                                    jsonObject2.put("ysex", t3);
//                                    jsonObject2.put("inchat", y1);
//                                    jsonObject2.put("yname", t4);
//                                    jsonObject2.put("yage", t5);
//                                    jsonObject2.put("locationX", t6);
//                                    jsonObject2.put("locationY", t7);
//                                    jsonArray2.put(jsonObject2);
//                                    list.put(socketSession, jsonArray2);
//
//                                    //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                    JSONArray jsonArray3 = new JSONArray();
//                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("from", y1);
//                                    jsonObject3.put("tsex", y2);
//                                    jsonObject3.put("ysex", y3);
//                                    jsonObject3.put("inchat", t1);
//                                    jsonObject3.put("yname", y4);
//                                    jsonObject3.put("yage", y5);
//                                    jsonObject3.put("locationX", y6);
//                                    jsonObject3.put("locationY", y7);
//                                    jsonArray3.put(jsonObject3);
//                                    list.put(session, jsonArray3);
//
//                                    //메세지 보내기
//                                    socketSession.sendMessage(message);
//                                    theEnd = 1;
//                                    break;
                                }
                            }else{
                                if(theEnd==0) {
                                    CharSequence alert2_4 = "찾는 사람이 없습니다";
                                    TextMessage message_2u_4 = new TextMessage(alert2_4);
                                    session.sendMessage(message_2u_4);
                                    theEnd = 1;
                                    break;
                                }
                            }
                        }
                    }
                } else {                  //나이맞는,성별맞는 아예 없을시
                    //System.out.println("2323");
                    //System.out.println(list4);
                    if(list4.size()!=0) {
                        for (WebSocketSession socketSession : list4.keySet()) {
                            System.out.println(socketSession);
                            //성별무상으로 검사x후 거리검색
                            double qq1 = Double.parseDouble(continue_locationX);
                            double qq2 = Double.parseDouble(continue_locationY);
                            double qq3 = Double.parseDouble(((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationX"))));
                            double qq4 = Double.parseDouble(((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationY"))));
                            double qqq = mapService.distance(qq1, qq2, qq3, qq4);
                            //나이맞는 성별없앤 거리맞는
                            if (qqq < 1) {                                                                      //거리맞을시 바로매치
                                if (theEnd == 0) {


                                    //상대방 아이디
                                    String t1 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("from");
                                    //상대방 타겟성별
                                    String t2 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("tsex");
                                    //상대방 자신성별
                                    String t3 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("ysex");
                                    //상대방 닉네임
                                    String t4 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yname");
                                    //상대방 나이
                                    String t5 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yage");
                                    String t6 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationX")));
                                    String t7 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationY")));

                                    //나이맞는대상모음
                                    JSONArray jsonArray2 = new JSONArray();
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("from", t1);
                                    jsonObject2.put("tsex", t2);
                                    jsonObject2.put("ysex", t3);
                                    jsonObject2.put("inchat", "");
                                    jsonObject2.put("yname", t4);
                                    jsonObject2.put("yage", t5);
                                    jsonObject2.put("locationX", t6);
                                    jsonObject2.put("locationY", t7);
                                    jsonArray2.put(jsonObject2);
                                    list99_2.put(socketSession, jsonArray2);


//                                    //상대방 아이디
//                                    String t1 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("from");
//                                    //상대방 타겟성별
//                                    String t2 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("tsex");
//                                    //상대방 자신성별
//                                    String t3 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("ysex");
//                                    //상대방 닉네임
//                                    String t4 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yname");
//                                    //상대방 나이
//                                    String t5 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yage");
//                                    String t6 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                    String t7 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                    //자기 아이디
//                                    String y1 = (String) object.get("from");
//                                    //타겟 성별
//                                    String y2 = continue_tsex;
//                                    //자기 성별
//                                    String y3 = continue_ysex;
//                                    //자기 닉네임
//                                    String y4 = continue_yname;
//                                    //자기 나이
//                                    String y5 = continue_yage;
//                                    String y6 = continue_locationX;
//                                    String y7 = continue_locationY;
//
//                                    //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                    JSONArray jsonArray2 = new JSONArray();
//                                    JSONObject jsonObject2 = new JSONObject();
//                                    jsonObject2.put("from", t1);
//                                    jsonObject2.put("tsex", t2);
//                                    jsonObject2.put("ysex", t3);
//                                    jsonObject2.put("inchat", y1);
//                                    jsonObject2.put("yname", t4);
//                                    jsonObject2.put("yage", t5);
//                                    jsonObject2.put("locationX", t6);
//                                    jsonObject2.put("locationY", t7);
//                                    jsonArray2.put(jsonObject2);
//                                    list.put(socketSession, jsonArray2);
//
//                                    //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                    JSONArray jsonArray3 = new JSONArray();
//                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("from", y1);
//                                    jsonObject3.put("tsex", y2);
//                                    jsonObject3.put("ysex", y3);
//                                    jsonObject3.put("inchat", t1);
//                                    jsonObject3.put("yname", y4);
//                                    jsonObject3.put("yage", y5);
//                                    jsonObject3.put("locationX", y6);
//                                    jsonObject3.put("locationY", y7);
//                                    jsonArray3.put(jsonObject3);
//                                    list.put(session, jsonArray3);
//
//                                    //메세지 보내기
//                                    socketSession.sendMessage(message);
//                                    theEnd = 1;
//                                    break;
                                }
                            } else {                                                                        //거리틀릴시 거리증가후 검사
                                if (qqq < 100) {                                                                     //거리틀릴시 거리증가후 거리맞을시     //나이맞는 성별없앤 거리틀린
                                    if (theEnd == 0) {

                                        //상대방 아이디
                                        String t1 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("from");
                                        //상대방 타겟성별
                                        String t2 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("tsex");
                                        //상대방 자신성별
                                        String t3 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("ysex");
                                        //상대방 닉네임
                                        String t4 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yname");
                                        //상대방 나이
                                        String t5 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yage");
                                        String t6 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationX")));
                                        String t7 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationY")));

                                        //나이맞는대상모음
                                        JSONArray jsonArray2 = new JSONArray();
                                        JSONObject jsonObject2 = new JSONObject();
                                        jsonObject2.put("from", t1);
                                        jsonObject2.put("tsex", t2);
                                        jsonObject2.put("ysex", t3);
                                        jsonObject2.put("inchat", "");
                                        jsonObject2.put("yname", t4);
                                        jsonObject2.put("yage", t5);
                                        jsonObject2.put("locationX", t6);
                                        jsonObject2.put("locationY", t7);
                                        jsonArray2.put(jsonObject2);
                                        list99_2_n.put(socketSession, jsonArray2);

//                                        //상대방 아이디
//                                        String t1 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("from");
//                                        //상대방 타겟성별
//                                        String t2 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("tsex");
//                                        //상대방 자신성별
//                                        String t3 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("ysex");
//                                        //상대방 닉네임
//                                        String t4 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yname");
//                                        //상대방 나이
//                                        String t5 = (String) ((list4.get(socketSession)).getJSONObject(0)).get("yage");
//                                        String t6 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                        String t7 = ((String) (((list4.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                        //자기 아이디
//                                        String y1 = (String) object.get("from");
//                                        //타겟 성별
//                                        String y2 = continue_tsex;
//                                        //자기 성별
//                                        String y3 = continue_ysex;
//                                        //자기 닉네임
//                                        String y4 = continue_yname;
//                                        //자기 나이
//                                        String y5 = continue_yage;
//                                        String y6 = continue_locationX;
//                                        String y7 = continue_locationY;
//
//                                        //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                        JSONArray jsonArray2 = new JSONArray();
//                                        JSONObject jsonObject2 = new JSONObject();
//                                        jsonObject2.put("from", t1);
//                                        jsonObject2.put("tsex", t2);
//                                        jsonObject2.put("ysex", t3);
//                                        jsonObject2.put("inchat", y1);
//                                        jsonObject2.put("yname", t4);
//                                        jsonObject2.put("yage", t5);
//                                        jsonObject2.put("locationX", t6);
//                                        jsonObject2.put("locationY", t7);
//                                        jsonArray2.put(jsonObject2);
//                                        list.put(socketSession, jsonArray2);
//
//                                        //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                        JSONArray jsonArray3 = new JSONArray();
//                                        JSONObject jsonObject3 = new JSONObject();
//                                        jsonObject3.put("from", y1);
//                                        jsonObject3.put("tsex", y2);
//                                        jsonObject3.put("ysex", y3);
//                                        jsonObject3.put("inchat", t1);
//                                        jsonObject3.put("yname", y4);
//                                        jsonObject3.put("yage", y5);
//                                        jsonObject3.put("locationX", y6);
//                                        jsonObject3.put("locationY", y7);
//                                        jsonArray3.put(jsonObject3);
//                                        list.put(session, jsonArray3);
//
//                                        //메세지 보내기
//                                        socketSession.sendMessage(message);
//                                        theEnd = 1;
//                                        break;
                                    }
                                } else {                                                          //거리틀릴시 거리증가후 거리틀릴시
                                    if (theEnd == 0) {
                                        CharSequence alert2_4 = "찾는 사람이 없습니다";
                                        TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        session.sendMessage(message_2u_4);
                                        theEnd = 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }


            if(theEnd==0){//걸렸으면 나가삼
                if(list5.size()!=0){                                                    //나이틀린, 나이증가후 나이맞는, 성별맞는 애들 한명이라도 있다면
                    //System.out.println(list5);
                    if(list5.size()!=0) {
                        for (WebSocketSession socketSession : list5.keySet()) {
                            //System.out.println(socketSession);
                            double qq1 = Double.parseDouble(continue_locationX);
                            double qq2 = Double.parseDouble(continue_locationY);
                            double qq3 = Double.parseDouble(((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationX"))));
                            double qq4 = Double.parseDouble(((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationY"))));
                            double qqq = mapService.distance(qq1, qq2, qq3, qq4);
                            if (qqq < 1) {                                                      //나이틀린, 나이증가후 나이맞는, 성별맞는, 거리맞는
                                if (theEnd == 0) {

                                    //상대방 아이디
                                    String t1 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("from");
                                    //상대방 타겟성별
                                    String t2 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("tsex");
                                    //상대방 자신성별
                                    String t3 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("ysex");
                                    //상대방 닉네임
                                    String t4 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yname");
                                    //상대방 나이
                                    String t5 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yage");
                                    String t6 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationX")));
                                    String t7 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationY")));

                                    //나이맞는대상모음
                                    JSONArray jsonArray2 = new JSONArray();
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("from", t1);
                                    jsonObject2.put("tsex", t2);
                                    jsonObject2.put("ysex", t3);
                                    jsonObject2.put("inchat", "");
                                    jsonObject2.put("yname", t4);
                                    jsonObject2.put("yage", t5);
                                    jsonObject2.put("locationX", t6);
                                    jsonObject2.put("locationY", t7);
                                    jsonArray2.put(jsonObject2);
                                    list99_3.put(socketSession, jsonArray2);

//                                    //상대방 아이디
//                                    String t1 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("from");
//                                    //상대방 타겟성별
//                                    String t2 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("tsex");
//                                    //상대방 자신성별
//                                    String t3 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("ysex");
//                                    //상대방 닉네임
//                                    String t4 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yname");
//                                    //상대방 나이
//                                    String t5 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yage");
//                                    String t6 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                    String t7 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                    //자기 아이디
//                                    String y1 = (String) object.get("from");
//                                    //타겟 성별
//                                    String y2 = continue_tsex;
//                                    //자기 성별
//                                    String y3 = continue_ysex;
//                                    //자기 닉네임
//                                    String y4 = continue_yname;
//                                    //자기 나이
//                                    String y5 = continue_yage;
//                                    String y6 = continue_locationX;
//                                    String y7 = continue_locationY;
//
//                                    //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                    JSONArray jsonArray2 = new JSONArray();
//                                    JSONObject jsonObject2 = new JSONObject();
//                                    jsonObject2.put("from", t1);
//                                    jsonObject2.put("tsex", t2);
//                                    jsonObject2.put("ysex", t3);
//                                    jsonObject2.put("inchat", y1);
//                                    jsonObject2.put("yname", t4);
//                                    jsonObject2.put("yage", t5);
//                                    jsonObject2.put("locationX", t6);
//                                    jsonObject2.put("locationY", t7);
//                                    jsonArray2.put(jsonObject2);
//                                    list.put(socketSession, jsonArray2);
//
//                                    //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                    JSONArray jsonArray3 = new JSONArray();
//                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("from", y1);
//                                    jsonObject3.put("tsex", y2);
//                                    jsonObject3.put("ysex", y3);
//                                    jsonObject3.put("inchat", t1);
//                                    jsonObject3.put("yname", y4);
//                                    jsonObject3.put("yage", y5);
//                                    jsonObject3.put("locationX", y6);
//                                    jsonObject3.put("locationY", y7);
//                                    jsonArray3.put(jsonObject3);
//                                    list.put(session, jsonArray3);
//
//                                    //메세지 보내기
//                                    socketSession.sendMessage(message);
//                                    theEnd = 1;
//                                    break;
                                }
                            } else {
                                if (qqq < 100) {                                                  //나이틀린, 나이증가후 나이맞는, 성별맞는, 거리틀린, 거리증가후 거리맞는
                                    if (theEnd == 0) {
                                        //상대방 아이디
                                        String t1 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("from");
                                        //상대방 타겟성별
                                        String t2 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("tsex");
                                        //상대방 자신성별
                                        String t3 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("ysex");
                                        //상대방 닉네임
                                        String t4 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yname");
                                        //상대방 나이
                                        String t5 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yage");
                                        String t6 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationX")));
                                        String t7 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationY")));

                                        //나이맞는대상모음
                                        JSONArray jsonArray2 = new JSONArray();
                                        JSONObject jsonObject2 = new JSONObject();
                                        jsonObject2.put("from", t1);
                                        jsonObject2.put("tsex", t2);
                                        jsonObject2.put("ysex", t3);
                                        jsonObject2.put("inchat", "");
                                        jsonObject2.put("yname", t4);
                                        jsonObject2.put("yage", t5);
                                        jsonObject2.put("locationX", t6);
                                        jsonObject2.put("locationY", t7);
                                        jsonArray2.put(jsonObject2);
                                        list99_3_n.put(socketSession, jsonArray2);


//                                        //상대방 아이디
//                                        String t1 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("from");
//                                        //상대방 타겟성별
//                                        String t2 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("tsex");
//                                        //상대방 자신성별
//                                        String t3 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("ysex");
//                                        //상대방 닉네임
//                                        String t4 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yname");
//                                        //상대방 나이
//                                        String t5 = (String) ((list5.get(socketSession)).getJSONObject(0)).get("yage");
//                                        String t6 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                        String t7 = ((String) (((list5.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                        //자기 아이디
//                                        String y1 = (String) object.get("from");
//                                        //타겟 성별
//                                        String y2 = continue_tsex;
//                                        //자기 성별
//                                        String y3 = continue_ysex;
//                                        //자기 닉네임
//                                        String y4 = continue_yname;
//                                        //자기 나이
//                                        String y5 = continue_yage;
//                                        String y6 = continue_locationX;
//                                        String y7 = continue_locationY;
//
//                                        //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                        JSONArray jsonArray2 = new JSONArray();
//                                        JSONObject jsonObject2 = new JSONObject();
//                                        jsonObject2.put("from", t1);
//                                        jsonObject2.put("tsex", t2);
//                                        jsonObject2.put("ysex", t3);
//                                        jsonObject2.put("inchat", y1);
//                                        jsonObject2.put("yname", t4);
//                                        jsonObject2.put("yage", t5);
//                                        jsonObject2.put("locationX", t6);
//                                        jsonObject2.put("locationY", t7);
//                                        jsonArray2.put(jsonObject2);
//                                        list.put(socketSession, jsonArray2);
//
//                                        //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                        JSONArray jsonArray3 = new JSONArray();
//                                        JSONObject jsonObject3 = new JSONObject();
//                                        jsonObject3.put("from", y1);
//                                        jsonObject3.put("tsex", y2);
//                                        jsonObject3.put("ysex", y3);
//                                        jsonObject3.put("inchat", t1);
//                                        jsonObject3.put("yname", y4);
//                                        jsonObject3.put("yage", y5);
//                                        jsonObject3.put("locationX", y6);
//                                        jsonObject3.put("locationY", y7);
//                                        jsonArray3.put(jsonObject3);
//                                        list.put(session, jsonArray3);
//
//                                        //메세지 보내기
//                                        socketSession.sendMessage(message);
//                                        theEnd = 1;
//                                        break;
                                    }
                                } else {                                                      //나이틀린, 나이증가후 나이맞는, 성별맞는, 거리틀린, 거리증가후 거리틀린 -> 알람띄우기
                                    if (theEnd == 0) {
                                        CharSequence alert2_4 = "찾는 사람이 없습니다";
                                        TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        session.sendMessage(message_2u_4);
                                        theEnd = 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }else{                                                                  //나이틀린, 나이증가후 나이맞는, 성별맞는 애들 없다면 --> 성별무상으로 거리검색
                    if(list6.size()!=0) {
                        for (WebSocketSession socketSession : list6.keySet()) {
                            double qq1 = Double.parseDouble(continue_locationX);
                            double qq2 = Double.parseDouble(continue_locationY);
                            double qq3 = Double.parseDouble(((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationX"))));
                            double qq4 = Double.parseDouble(((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationY"))));
                            double qqq = mapService.distance(qq1, qq2, qq3, qq4);
                            if (qqq < 1) {                                  //나이틀린, 나이증가후 나이맞는, 성별틀린, 성별무상후 거리맞는 애들 매칭
                                if (theEnd == 0) {
                                    //상대방 아이디
                                    String t1 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("from");
                                    //상대방 타겟성별
                                    String t2 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("tsex");
                                    //상대방 자신성별
                                    String t3 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("ysex");
                                    //상대방 닉네임
                                    String t4 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yname");
                                    //상대방 나이
                                    String t5 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yage");
                                    String t6 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationX")));
                                    String t7 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationY")));

                                    //나이맞는대상모음
                                    JSONArray jsonArray2 = new JSONArray();
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("from", t1);
                                    jsonObject2.put("tsex", t2);
                                    jsonObject2.put("ysex", t3);
                                    jsonObject2.put("inchat", "");
                                    jsonObject2.put("yname", t4);
                                    jsonObject2.put("yage", t5);
                                    jsonObject2.put("locationX", t6);
                                    jsonObject2.put("locationY", t7);
                                    jsonArray2.put(jsonObject2);
                                    list99_4.put(socketSession, jsonArray2);

//                                    //상대방 아이디
//                                    String t1 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("from");
//                                    //상대방 타겟성별
//                                    String t2 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("tsex");
//                                    //상대방 자신성별
//                                    String t3 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("ysex");
//                                    //상대방 닉네임
//                                    String t4 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yname");
//                                    //상대방 나이
//                                    String t5 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yage");
//                                    String t6 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                    String t7 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                    //자기 아이디
//                                    String y1 = (String) object.get("from");
//                                    //타겟 성별
//                                    String y2 = continue_tsex;
//                                    //자기 성별
//                                    String y3 = continue_ysex;
//                                    //자기 닉네임
//                                    String y4 = continue_yname;
//                                    //자기 나이
//                                    String y5 = continue_yage;
//                                    String y6 = continue_locationX;
//                                    String y7 = continue_locationY;
//
//                                    //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                    JSONArray jsonArray2 = new JSONArray();
//                                    JSONObject jsonObject2 = new JSONObject();
//                                    jsonObject2.put("from", t1);
//                                    jsonObject2.put("tsex", t2);
//                                    jsonObject2.put("ysex", t3);
//                                    jsonObject2.put("inchat", y1);
//                                    jsonObject2.put("yname", t4);
//                                    jsonObject2.put("yage", t5);
//                                    jsonObject2.put("locationX", t6);
//                                    jsonObject2.put("locationY", t7);
//                                    jsonArray2.put(jsonObject2);
//                                    list.put(socketSession, jsonArray2);
//
//                                    //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                    JSONArray jsonArray3 = new JSONArray();
//                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("from", y1);
//                                    jsonObject3.put("tsex", y2);
//                                    jsonObject3.put("ysex", y3);
//                                    jsonObject3.put("inchat", t1);
//                                    jsonObject3.put("yname", y4);
//                                    jsonObject3.put("yage", y5);
//                                    jsonObject3.put("locationX", y6);
//                                    jsonObject3.put("locationY", y7);
//                                    jsonArray3.put(jsonObject3);
//                                    list.put(session, jsonArray3);
//
//                                    //메세지 보내기
//                                    socketSession.sendMessage(message);
//                                    theEnd = 1;
//                                    break;
                                }
                            } else {
                                if (qqq < 100) {
                                    if (theEnd == 0) {                                 //나이틀린, 나이증가후 나이맞는, 성별틀린, 성별무상후 거리틀린, 거리증가후 거리맞는 애들 매칭

                                        //상대방 아이디
                                        String t1 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("from");
                                        //상대방 타겟성별
                                        String t2 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("tsex");
                                        //상대방 자신성별
                                        String t3 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("ysex");
                                        //상대방 닉네임
                                        String t4 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yname");
                                        //상대방 나이
                                        String t5 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yage");
                                        String t6 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationX")));
                                        String t7 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationY")));

                                        //나이맞는대상모음
                                        JSONArray jsonArray2 = new JSONArray();
                                        JSONObject jsonObject2 = new JSONObject();
                                        jsonObject2.put("from", t1);
                                        jsonObject2.put("tsex", t2);
                                        jsonObject2.put("ysex", t3);
                                        jsonObject2.put("inchat", "");
                                        jsonObject2.put("yname", t4);
                                        jsonObject2.put("yage", t5);
                                        jsonObject2.put("locationX", t6);
                                        jsonObject2.put("locationY", t7);
                                        jsonArray2.put(jsonObject2);
                                        list99_4_n.put(socketSession, jsonArray2);

//                                        //상대방 아이디
//                                        String t1 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("from");
//                                        //상대방 타겟성별
//                                        String t2 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("tsex");
//                                        //상대방 자신성별
//                                        String t3 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("ysex");
//                                        //상대방 닉네임
//                                        String t4 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yname");
//                                        //상대방 나이
//                                        String t5 = (String) ((list6.get(socketSession)).getJSONObject(0)).get("yage");
//                                        String t6 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationX")));
//                                        String t7 = ((String) (((list6.get(socketSession)).getJSONObject(0)).get("locationY")));
//
//                                        //자기 아이디
//                                        String y1 = (String) object.get("from");
//                                        //타겟 성별
//                                        String y2 = continue_tsex;
//                                        //자기 성별
//                                        String y3 = continue_ysex;
//                                        //자기 닉네임
//                                        String y4 = continue_yname;
//                                        //자기 나이
//                                        String y5 = continue_yage;
//                                        String y6 = continue_locationX;
//                                        String y7 = continue_locationY;
//
//                                        //상대방에 상대방정보+채팅방에 자신 아이디 입력
//                                        JSONArray jsonArray2 = new JSONArray();
//                                        JSONObject jsonObject2 = new JSONObject();
//                                        jsonObject2.put("from", t1);
//                                        jsonObject2.put("tsex", t2);
//                                        jsonObject2.put("ysex", t3);
//                                        jsonObject2.put("inchat", y1);
//                                        jsonObject2.put("yname", t4);
//                                        jsonObject2.put("yage", t5);
//                                        jsonObject2.put("locationX", t6);
//                                        jsonObject2.put("locationY", t7);
//                                        jsonArray2.put(jsonObject2);
//                                        list.put(socketSession, jsonArray2);
//
//                                        //자신정보에 자신정보+채팅방에 상대 아이디 입력
//                                        JSONArray jsonArray3 = new JSONArray();
//                                        JSONObject jsonObject3 = new JSONObject();
//                                        jsonObject3.put("from", y1);
//                                        jsonObject3.put("tsex", y2);
//                                        jsonObject3.put("ysex", y3);
//                                        jsonObject3.put("inchat", t1);
//                                        jsonObject3.put("yname", y4);
//                                        jsonObject3.put("yage", y5);
//                                        jsonObject3.put("locationX", y6);
//                                        jsonObject3.put("locationY", y7);
//                                        jsonArray3.put(jsonObject3);
//                                        list.put(session, jsonArray3);
//
//                                        //메세지 보내기
//                                        socketSession.sendMessage(message);
//                                        theEnd = 1;
//                                        break;
                                    }
                                } else {
                                    if (theEnd == 0) {                                //나이틀린, 나이증가후 나이맞는, 성별틀린, 성별무상후 거리틀린, 거리증가후 거리틀린 ->알람 띄우기
                                        CharSequence alert2_4 = "찾는 사람이 없습니다";
                                        TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        session.sendMessage(message_2u_4);
                                        theEnd = 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for(int m=1; m<=4;m++) {
                if (theEnd == 0) {
                    Map< WebSocketSession , JSONArray> list100 = new HashMap<>();
                    Map< WebSocketSession , JSONArray> list100_n = new HashMap<>();
                    if(m==1) {
                        list100 = list99_1;
                        list100_n = list99_1_n;
                    }else if(m==2){
                        list100 = list99_2;
                        list100_n = list99_2_n;
                    }else if(m==3){
                        list100 = list99_3;
                        list100_n = list99_3_n;
                    }else if(m==4){
                        list100 = list99_4;
                        list100_n = list99_4_n;
                    }
                    if (list100.size() != 0) {
                        for (WebSocketSession socketSession : list100.keySet()) {
                            //상대방 아이디
                            String t1 = (String) ((list100.get(socketSession)).getJSONObject(0)).get("from");
                            //상대방 타겟성별
                            String t2 = (String) ((list100.get(socketSession)).getJSONObject(0)).get("tsex");
                            //상대방 자신성별
                            String t3 = (String) ((list100.get(socketSession)).getJSONObject(0)).get("ysex");
                            //상대방 닉네임
                            String t4 = (String) ((list100.get(socketSession)).getJSONObject(0)).get("yname");
                            //상대방 나이
                            String t5 = (String) ((list100.get(socketSession)).getJSONObject(0)).get("yage");
                            String t6 = ((String) (((list100.get(socketSession)).getJSONObject(0)).get("locationX")));
                            String t7 = ((String) (((list100.get(socketSession)).getJSONObject(0)).get("locationY")));

                            //자기 아이디
                            String y1 = (String) object.get("from");
                            //타겟 성별
                            String y2 = continue_tsex;
                            //자기 성별
                            String y3 = continue_ysex;
                            //자기 닉네임
                            String y4 = continue_yname;
                            //자기 나이
                            String y5 = continue_yage;
                            String y6 = continue_locationX;
                            String y7 = continue_locationY;

                            //상대방에 상대방정보+채팅방에 자신 아이디 입력
                            JSONArray jsonArray2 = new JSONArray();
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("from", t1);
                            jsonObject2.put("tsex", t2);
                            jsonObject2.put("ysex", t3);
                            jsonObject2.put("inchat", y1);
                            jsonObject2.put("yname", t4);
                            jsonObject2.put("yage", t5);
                            jsonObject2.put("locationX", t6);
                            jsonObject2.put("locationY", t7);
                            jsonArray2.put(jsonObject2);
                            list.put(socketSession, jsonArray2);

                            //자신정보에 자신정보+채팅방에 상대 아이디 입력
                            JSONArray jsonArray3 = new JSONArray();
                            JSONObject jsonObject3 = new JSONObject();
                            jsonObject3.put("from", y1);
                            jsonObject3.put("tsex", y2);
                            jsonObject3.put("ysex", y3);
                            jsonObject3.put("inchat", t1);
                            jsonObject3.put("yname", y4);
                            jsonObject3.put("yage", y5);
                            jsonObject3.put("locationX", y6);
                            jsonObject3.put("locationY", y7);
                            jsonArray3.put(jsonObject3);
                            list.put(session, jsonArray3);

                            //메세지 보내기
                            socketSession.sendMessage(message);
                            theEnd = 1;
                            break;
                        }
                    } else if (list100_n.size() != 0) {
                        for (WebSocketSession socketSession : list100_n.keySet()) {
                            //상대방 아이디
                            String t1 = (String) ((list100_n.get(socketSession)).getJSONObject(0)).get("from");
                            //상대방 타겟성별
                            String t2 = (String) ((list100_n.get(socketSession)).getJSONObject(0)).get("tsex");
                            //상대방 자신성별
                            String t3 = (String) ((list100_n.get(socketSession)).getJSONObject(0)).get("ysex");
                            //상대방 닉네임
                            String t4 = (String) ((list100_n.get(socketSession)).getJSONObject(0)).get("yname");
                            //상대방 나이
                            String t5 = (String) ((list100_n.get(socketSession)).getJSONObject(0)).get("yage");
                            String t6 = ((String) (((list100_n.get(socketSession)).getJSONObject(0)).get("locationX")));
                            String t7 = ((String) (((list100_n.get(socketSession)).getJSONObject(0)).get("locationY")));

                            //자기 아이디
                            String y1 = (String) object.get("from");
                            //타겟 성별
                            String y2 = continue_tsex;
                            //자기 성별
                            String y3 = continue_ysex;
                            //자기 닉네임
                            String y4 = continue_yname;
                            //자기 나이
                            String y5 = continue_yage;
                            String y6 = continue_locationX;
                            String y7 = continue_locationY;

                            //상대방에 상대방정보+채팅방에 자신 아이디 입력
                            JSONArray jsonArray2 = new JSONArray();
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("from", t1);
                            jsonObject2.put("tsex", t2);
                            jsonObject2.put("ysex", t3);
                            jsonObject2.put("inchat", y1);
                            jsonObject2.put("yname", t4);
                            jsonObject2.put("yage", t5);
                            jsonObject2.put("locationX", t6);
                            jsonObject2.put("locationY", t7);
                            jsonArray2.put(jsonObject2);
                            list.put(socketSession, jsonArray2);

                            //자신정보에 자신정보+채팅방에 상대 아이디 입력
                            JSONArray jsonArray3 = new JSONArray();
                            JSONObject jsonObject3 = new JSONObject();
                            jsonObject3.put("from", y1);
                            jsonObject3.put("tsex", y2);
                            jsonObject3.put("ysex", y3);
                            jsonObject3.put("inchat", t1);
                            jsonObject3.put("yname", y4);
                            jsonObject3.put("yage", y5);
                            jsonObject3.put("locationX", y6);
                            jsonObject3.put("locationY", y7);
                            jsonArray3.put(jsonObject3);
                            list.put(session, jsonArray3);

                            //메세지 보내기
                            socketSession.sendMessage(message);
                            theEnd = 1;
                            break;
                        }
                    }

                }
            }
            if(theEnd==0){
                CharSequence alert2_4 = "찾는 사람이 없습니다";
                TextMessage message_2u_4 = new TextMessage(alert2_4);
                session.sendMessage(message_2u_4);
                theEnd = 1;
            }
        }
    }

}