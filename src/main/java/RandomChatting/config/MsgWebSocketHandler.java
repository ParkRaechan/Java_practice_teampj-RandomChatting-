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
                    System.out.println(list.get( socketSession));
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

            //int index2=0;   int index3=0;
            int age_range=1;                //나이 범위
            int age_range_index = 0;        //break되었을시 1로
            //int index1_count=0;

            if(continue_target.equals("")) {
                int algorizm_index = 0;     //나이 알고리즘 얼마나 작동했는지
                int alone_index = 0;
                //for(age_range=1;age_range<=2;age_range++) {
                    for (WebSocketSession socketSession : list.keySet()) {    // 모든 키값 호출
                        algorizm_index++;
                        String qq_index1 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationX")));
                        String qq_index2 = ((String) (((list.get(socketSession)).getJSONObject(0)).get("locationY")));
                        int index_age = Integer.parseInt((String) (((list.get(socketSession)).getJSONObject(0)).get("yage")));
                        if (index_age <= Integer.parseInt(continue_yage) + age_range && index_age >= Integer.parseInt(continue_yage) - age_range) {          //타겟 나이 있을때


                            if (!((String) (((list.get(socketSession)).getJSONObject(0)).get("from"))).equals(object.get("from"))) {     //자신제외
                                if (((String) (((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별이 있을때

                                    if (((String) (((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중
                                        double qq1 = Double.parseDouble(continue_locationX);
                                        double qq2 = Double.parseDouble(continue_locationY);
                                        double qq3 = Double.parseDouble(((String) (((list.get(socketSession)).getJSONObject(0)).get("locationX"))));
                                        double qq4 = Double.parseDouble(((String) (((list.get(socketSession)).getJSONObject(0)).get("locationY"))));
                                        double qqq = mapService.distance(qq1,qq2,qq3,qq4);
                                        System.out.println("q12");System.out.println(qqq);
                                        int q_index=1;
                                        if(qqq<q_index) {
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
                                            String t6 = qq_index1;
                                            String t7 = qq_index2;

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
                                            age_range_index = 1;
                                            break;
                                        }else{
                                            q_index++;
                                            if(qqq<q_index) {
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
                                                String t6 = qq_index1;
                                                String t7 = qq_index2;

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
                                                age_range_index = 1;
                                                break;
                                            }else{
                                                if(age_range_index == 0){
                                                    CharSequence alert2_4 = "찾는 사람이 없습니다";
                                                    TextMessage message_2u_4 = new TextMessage(alert2_4);
                                                    session.sendMessage(message_2u_4);
                                                    age_range_index = 1;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {//성별에 해당하는 사람 아닐시
                                    //넘어감 - break 없음
                                }
                            } else {                  //상대방이 없고 서버에 혼자 일때
                                if(alone_index==0) {
                                    alone_index = 1;
                                    if (list.size() == 1) {
                                        CharSequence alert2_4 = "찾을 수 있는 사람이 없습니다";
                                        TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        session.sendMessage(message_2u_4);
                                        age_range_index = 1;
                                        break;
                                    }
                                }
                            }
                        }else{
                            age_range++;
                            if (!((String) (((list.get(socketSession)).getJSONObject(0)).get("from"))).equals(object.get("from"))) {     //자신제외
                                if (((String) (((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별이 있을때

                                    if (((String) (((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중
                                        double qq1 = Double.parseDouble(continue_locationX);
                                        double qq2 = Double.parseDouble(continue_locationY);
                                        double qq3 = Double.parseDouble(((String) (((list.get(socketSession)).getJSONObject(0)).get("locationX"))));
                                        double qq4 = Double.parseDouble(((String) (((list.get(socketSession)).getJSONObject(0)).get("locationY"))));
                                        double qqq = mapService.distance(qq1,qq2,qq3,qq4);
                                        System.out.println("q12");System.out.println(qqq);
                                        int q_index=1;
                                        if(qqq<q_index) {
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
                                            String t6 = qq_index1;
                                            String t7 = qq_index2;

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
                                            age_range_index = 1;
                                            break;
                                        }else{
                                            q_index++;
                                            if(qqq<q_index) {
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
                                                String t6 = qq_index1;
                                                String t7 = qq_index2;

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
                                                age_range_index = 1;
                                                break;
                                            }else{
                                                if(age_range_index == 0){
                                                    CharSequence alert2_4 = "찾는 사람이 없습니다";
                                                    TextMessage message_2u_4 = new TextMessage(alert2_4);
                                                    session.sendMessage(message_2u_4);
                                                    age_range_index = 1;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {//성별에 해당하는 사람 아닐시
                                    //넘어감 - break 없음
                                }
                            } else {                  //상대방이 없고 서버에 혼자 일때
                                if(alone_index==0) {
                                    alone_index = 1;
                                    if (list.size() == 1) {
                                        CharSequence alert2_4 = "찾을 수 있는 사람이 없습니다";
                                        TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        session.sendMessage(message_2u_4);
                                        age_range_index = 1;
                                        break;
                                    }
                                }
                            }
                        }
                        if (algorizm_index == list.size()) {        //break가 안된 상태 == 성별 없이 나이올려 검색하는 2단계 알고리즘으로 넘어감
                            ///////////////////////////////////////////////
                            for (WebSocketSession socketSession999 : list.keySet()) {    // 모든 키값 호출
                                int index_age_2 = Integer.parseInt((String) (((list.get(socketSession999)).getJSONObject(0)).get("yage")));
                                age_range=1;
                                if (index_age_2 <= Integer.parseInt(continue_yage) + age_range && index_age_2 >= Integer.parseInt(continue_yage) - age_range) {          //타겟 나이 있을때


                                    if (!((String) (((list.get(socketSession999)).getJSONObject(0)).get("from"))).equals(object.get("from"))) {     //자신제외

                                        if (((String) (((list.get(socketSession999)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중

                                            //상대방 아이디
                                            String t1_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("from");
                                            //상대방 타겟성별
                                            String t2_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("tsex");
                                            //상대방 자신성별
                                            String t3_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("ysex");
                                            //상대방 닉네임
                                            String t4_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("yname");
                                            //상대방 나이
                                            String t5_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("yage");
                                            String t6 = qq_index1;
                                            String t7 = qq_index2;

                                            //자기 아이디
                                            String y1_2 = (String) object.get("from");
                                            //타겟 성별
                                            String y2_2 = continue_tsex;
                                            //자기 성별
                                            String y3_2 = continue_ysex;
                                            //자기 닉네임
                                            String y4_2 = continue_yname;
                                            //자기 나이
                                            String y5_2 = continue_yage;
                                            String y6 = continue_locationX;
                                            String y7 = continue_locationY;
                                            //상대방에 상대방정보+채팅방에 자신 아이디 입력
                                            JSONArray jsonArray2_2 = new JSONArray();
                                            JSONObject jsonObject2_2 = new JSONObject();
                                            jsonObject2_2.put("from", t1_2);
                                            jsonObject2_2.put("tsex", t2_2);
                                            jsonObject2_2.put("ysex", t3_2);
                                            jsonObject2_2.put("inchat", y1_2);
                                            jsonObject2_2.put("yname", t4_2);
                                            jsonObject2_2.put("yage", t5_2);
                                            jsonObject2_2.put("locationX", t6);
                                            jsonObject2_2.put("locationY", t7);
                                            jsonArray2_2.put(jsonObject2_2);
                                            list.put(socketSession999, jsonArray2_2);

                                            //자신정보에 자신정보+채팅방에 상대 아이디 입력
                                            JSONArray jsonArray3_2 = new JSONArray();
                                            JSONObject jsonObject3_2 = new JSONObject();
                                            jsonObject3_2.put("from", y1_2);
                                            jsonObject3_2.put("tsex", y2_2);
                                            jsonObject3_2.put("ysex", y3_2);
                                            jsonObject3_2.put("inchat", t1_2);
                                            jsonObject3_2.put("yname", y4_2);
                                            jsonObject3_2.put("yage", y5_2);
                                            jsonObject3_2.put("locationX", y6);
                                            jsonObject3_2.put("locationY", y7);
                                            jsonArray3_2.put(jsonObject3_2);
                                            list.put(session, jsonArray3_2);

                                            //메세지 보내기
                                            socketSession999.sendMessage(message);
                                            age_range_index = 1;
                                            break;
                                        }

                                    }

                                }else{
                                    age_range++;
                                    if (index_age_2 <= Integer.parseInt(continue_yage) + age_range && index_age_2 >= Integer.parseInt(continue_yage) - age_range) {          //타겟 나이 있을때


                                        if (!((String) (((list.get(socketSession999)).getJSONObject(0)).get("from"))).equals(object.get("from"))) {     //자신제외

                                            if (((String) (((list.get(socketSession999)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중

                                                //상대방 아이디
                                                String t1_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("from");
                                                //상대방 타겟성별
                                                String t2_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("tsex");
                                                //상대방 자신성별
                                                String t3_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("ysex");
                                                //상대방 닉네임
                                                String t4_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("yname");
                                                //상대방 나이
                                                String t5_2 = (String) ((list.get(socketSession999)).getJSONObject(0)).get("yage");
                                                String t6 = qq_index1;
                                                String t7 = qq_index2;

                                                //자기 아이디
                                                String y1_2 = (String) object.get("from");
                                                //타겟 성별
                                                String y2_2 = continue_tsex;
                                                //자기 성별
                                                String y3_2 = continue_ysex;
                                                //자기 닉네임
                                                String y4_2 = continue_yname;
                                                //자기 나이
                                                String y5_2 = continue_yage;
                                                String y6 = continue_locationX;
                                                String y7 = continue_locationY;
                                                //상대방에 상대방정보+채팅방에 자신 아이디 입력
                                                JSONArray jsonArray2_2 = new JSONArray();
                                                JSONObject jsonObject2_2 = new JSONObject();
                                                jsonObject2_2.put("from", t1_2);
                                                jsonObject2_2.put("tsex", t2_2);
                                                jsonObject2_2.put("ysex", t3_2);
                                                jsonObject2_2.put("inchat", y1_2);
                                                jsonObject2_2.put("yname", t4_2);
                                                jsonObject2_2.put("yage", t5_2);
                                                jsonObject2_2.put("locationX", t6);
                                                jsonObject2_2.put("locationY", t7);
                                                jsonArray2_2.put(jsonObject2_2);
                                                list.put(socketSession999, jsonArray2_2);

                                                //자신정보에 자신정보+채팅방에 상대 아이디 입력
                                                JSONArray jsonArray3_2 = new JSONArray();
                                                JSONObject jsonObject3_2 = new JSONObject();
                                                jsonObject3_2.put("from", y1_2);
                                                jsonObject3_2.put("tsex", y2_2);
                                                jsonObject3_2.put("ysex", y3_2);
                                                jsonObject3_2.put("inchat", t1_2);
                                                jsonObject3_2.put("yname", y4_2);
                                                jsonObject3_2.put("yage", y5_2);
                                                jsonObject3_2.put("locationX", y6);
                                                jsonObject3_2.put("locationY", y7);
                                                jsonArray3_2.put(jsonObject3_2);
                                                list.put(session, jsonArray3_2);

                                                //메세지 보내기
                                                socketSession999.sendMessage(message);
                                                age_range_index = 1;
                                                break;
                                            }

                                        } //else {
                                        //  System.out.println("q7");
                                        //  if (list.size() == 1) {     //상대방이 없고 서버에 혼자 일때
                                        //      System.out.println("q6");
                                        //      CharSequence alert2_4 = "..........2";
                                        //      TextMessage message_2u_4 = new TextMessage(alert2_4);
                                        //      session.sendMessage(message_2u_4);
                                        //      age_range_index = 1;
                                        //      break;
                                        //  }
                                        //}
                                    }
                                }
                            }
                            //System.out.println("q4");
                            if(age_range_index == 0){
                                CharSequence alert2_4 = "찾는 사람이 없습니다";
                                TextMessage message_2u_4 = new TextMessage(alert2_4);
                                session.sendMessage(message_2u_4);
                                age_range_index = 1;
                                //break;
                            }
                            ///////////////////////////////////////////////
                            break;
                        }
                        if(age_range_index == 1){
                            //System.out.println("q1");
                            break;
                        }
                    }
//                    if( age_range == 2){
//                        //System.out.println("q2");
//                        break;
//                    }
                    //System.out.println("q3");
                //}
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
            }
        }
    }

}