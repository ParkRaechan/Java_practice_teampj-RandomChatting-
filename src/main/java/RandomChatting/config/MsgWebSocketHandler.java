package RandomChatting.config;

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
                    //System.out.println(jsonObject);
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
                    //continue_target = (String) list.get(socketSession99).getJSONObject(0).get("inchat");
                    continue_tsex = (String) list.get(socketSession99).getJSONObject(0).get("tsex");
                    continue_ysex = (String) list.get(socketSession99).getJSONObject(0).get("ysex");
                    //맞는 사람들끼리 채팅방 만들기 알고리즘에 필요한 값들 받아오기
                    //continue_locationX = (String) list.get(socketSession99).getJSONObject(0).get("locationX");
                    //continue_locationY = (String) list.get(socketSession99).getJSONObject(0).get("locationY");
                    continue_yname = (String) list.get(socketSession99).getJSONObject(0).get("yname");
                    continue_yage = (String) list.get(socketSession99).getJSONObject(0).get("yage");
                }
            }


            int index2=0;   int index3=0;       //인덱스

            int trx = 0;//나이2번올려도 검색 안될때
            int algorizm_index =0;
                for (WebSocketSession socketSession : list.keySet()) {    // 모든 키값 호출
                    //System.out.println(list.size());
                    if (((String) (object.get("inchat"))).equals("")) {

                        //System.out.println(continue_tsex);
                        //System.out.println((((list.get(socketSession)).getJSONObject(0)).get("ysex")));
                        int index_age = Integer.parseInt((String) (((list.get(socketSession)).getJSONObject(0)).get("yage")));

                        if (((String) (((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중
                            if(!((String) (((list.get(socketSession)).getJSONObject(0)).get("from"))).equals(object.get("from")))
                            {

                                for (int age_range = 1; age_range <= 3; age_range++) {
                                    if (trx == 0) {//나이 2번 올리기
                                        if (age_range == 3) {
                                            //나이 2번 올려도 찾아지지 않을때 해당 경우로 이동(성별상관없이 나이올리며 찾기)
                                            trx = 1;
//                                            CharSequence alert2 = "현재 찾을 수 있는 사람이 없습니다.";
//                                            TextMessage message_2u = new TextMessage(alert2);
//                                            session.sendMessage(message_2u);
//                                            break;
                                        }
                                        //지도 검색 코드
                                        //for(){    if(~){~~}   }
                                        if (index_age <= Integer.parseInt(continue_yage) + age_range && index_age >= Integer.parseInt(continue_yage) - age_range) {          //타겟 나이 있을때
                                            //System.out.println(index_age);
                                            //System.out.println(Integer.parseInt(continue_yage));

                                            if (((String) (((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별일때

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

                                                //상대방에 상대방정보+채팅방에 자신 아이디 입력
                                                JSONArray jsonArray2 = new JSONArray();
                                                JSONObject jsonObject2 = new JSONObject();
                                                jsonObject2.put("from", t1);
                                                jsonObject2.put("tsex", t2);
                                                jsonObject2.put("ysex", t3);
                                                jsonObject2.put("inchat", y1);
                                                jsonObject2.put("yname", t4);
                                                jsonObject2.put("yage", t5);
                                                jsonArray2.put(jsonObject2);
                                                //System.out.println(jsonObject2);
                                                list.put(socketSession, jsonArray2);
                                                System.out.println(list.get(socketSession));

                                                //자신정보에 자신정보+채팅방에 상대 아이디 입력
                                                JSONArray jsonArray3 = new JSONArray();
                                                JSONObject jsonObject3 = new JSONObject();
                                                jsonObject3.put("from", y1);
                                                jsonObject3.put("tsex", y2);
                                                jsonObject3.put("ysex", y3);
                                                jsonObject3.put("inchat", t1);
                                                jsonObject3.put("yname", y4);
                                                jsonObject3.put("yage", y5);
                                                jsonArray3.put(jsonObject3);
                                                //System.out.println(jsonObject3);
                                                list.put(session, jsonArray3);
                                                System.out.println(list.get(session));

                                                //나이 범위 원래대로 되돌리기
                                                //age_range = 2;
                                                //메세지 보내기
                                                socketSession.sendMessage(message);
                                                break;
                                            } else {//성별에 해당하는 사람 아닐시
                                                //그냥 진행 -> 나이 2번 올려짐
                                            }

                                        } else {//나이 범위에 맞는 사람 아닐시
                                            //그냥 진행 -> 나이 2번 올려짐
                                        }


                                    } else if (trx == 1) {       //나이 2번 올려도 찾아지지 않을 경우(성별상관없이 나이올리며 찾기)
                                        if(algorizm_index==0){
                                            age_range = 1;
                                            algorizm_index = 1;
                                        }
                                        if (age_range == 3) {       //나이범위가 3이 되었을때 --- 못 찾았을 경우로 취급
                                            CharSequence alert2 = "현재 찾을 수 있는 사람이 없습니다.";
                                            TextMessage message_2u = new TextMessage(alert2);
                                            session.sendMessage(message_2u);
                                            break;
                                        }

                                        //지도 검색 코드
                                        //for(){    if(~){~~}   }
                                        if (index_age <= Integer.parseInt(continue_yage) + age_range && index_age >= Integer.parseInt(continue_yage) - age_range) {          //타겟 나이 있을때
                                            //System.out.println(index_age);
                                            //System.out.println(Integer.parseInt(continue_yage));

                                            //if (((String) (((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별상관없음

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

                                            //상대방에 상대방정보+채팅방에 자신 아이디 입력
                                            JSONArray jsonArray2 = new JSONArray();
                                            JSONObject jsonObject2 = new JSONObject();
                                            jsonObject2.put("from", t1);
                                            jsonObject2.put("tsex", t2);
                                            jsonObject2.put("ysex", t3);
                                            jsonObject2.put("inchat", y1);
                                            jsonObject2.put("yname", t4);
                                            jsonObject2.put("yage", t5);
                                            jsonArray2.put(jsonObject2);
                                            //System.out.println(jsonObject2);
                                            list.put(socketSession, jsonArray2);
                                            System.out.println(list.get(socketSession));

                                            //자신정보에 자신정보+채팅방에 상대 아이디 입력
                                            JSONArray jsonArray3 = new JSONArray();
                                            JSONObject jsonObject3 = new JSONObject();
                                            jsonObject3.put("from", y1);
                                            jsonObject3.put("tsex", y2);
                                            jsonObject3.put("ysex", y3);
                                            jsonObject3.put("inchat", t1);
                                            jsonObject3.put("yname", y4);
                                            jsonObject3.put("yage", y5);
                                            jsonArray3.put(jsonObject3);
                                            //System.out.println(jsonObject3);
                                            list.put(session, jsonArray3);
                                            System.out.println(list.get(session));

                                            //나이 범위 원래대로 되돌리기
                                            //age_range = 2;
                                            //메세지 보내기
                                            socketSession.sendMessage(message);
                                            break;
                                            //} else {//성별에 해당하는 사람 아닐시
                                            //그냥 진행 -> 나이 2번 올려짐
                                            //}

                                        } else {//나이 범위에 맞는 사람 아닐시
                                            //그냥 진행 -> 나이 2번 올려짐
                                        }
                                    }
                                }
                            }
                        } else {// 자신이 채팅방에 들어가있다면(상대방이 먼저 채팅을 쳐서 연결된 상태)
                            //System.out.println("123123");
                            if (index3 == 0) {
                                //System.out.println(index3);
                                for (WebSocketSession socketSession3 : list.keySet()) {
                                    if (((String) list.get(socketSession3).getJSONObject(0).get("from")).equals(object.get("from"))) {
                                        continue_target = (String) list.get(socketSession3).getJSONObject(0).get("inchat");
                                        //System.out.println(continue_target);
                                    }
                                }
                                index3++;
                            }
                            //System.out.println(continue_target);
                            //System.out.println(list.get( socketSession).getJSONObject(0).get("from"));
                            if (((String) list.get(socketSession).getJSONObject(0).get("from")).equals(continue_target)) {

                                //System.out.println("qwe");
                                socketSession.sendMessage(message);
                            }
                        }



                    } else {
                        //System.out.println("통신");
                        if (index2 == 0) {
                            //System.out.println(index2);
                            for (WebSocketSession socketSession2 : list.keySet()) {
                                if (((String) list.get(socketSession2).getJSONObject(0).get("from")).equals(object.get("from"))) {
                                    continue_target = (String) list.get(socketSession2).getJSONObject(0).get("inchat");
                                    //continue_target이란? inchat변수에 적혀있는 채팅방에 연결된 상대 이름
                                    //System.out.println(continue_target);
                                }
                            }
                            index2++;
                        }
                        //System.out.println(continue_target);
                        //System.out.println(((String)list.get( socketSession).getJSONObject(0).get("from")));
                        if (((String) list.get(socketSession).getJSONObject(0).get("from")).equals(continue_target)) {
                            //System.out.println("qwe");
                            socketSession.sendMessage(message);
                            //System.out.println(message);
                        }
                    }


                }

        }
    }

}
