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
                    //continue_locationX = (String) list.get(socketSession99).getJSONObject(0).get("locationX");
                    //continue_locationY = (String) list.get(socketSession99).getJSONObject(0).get("locationY");
                    continue_yname = (String) list.get(socketSession99).getJSONObject(0).get("yname");
                    continue_yage = (String) list.get(socketSession99).getJSONObject(0).get("yage");
                }
            }

            //int index2=0;   int index3=0;
            int age_range=2;
            for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출

                if(continue_target.equals("")) {

                    int index_age = Integer.parseInt((String)(((list.get(socketSession)).getJSONObject(0)).get("yage")));
                    if(index_age<=Integer.parseInt(continue_yage)+age_range && index_age>=Integer.parseInt(continue_yage)-age_range) {          //타겟 나이 있을때

                        if (((String) (((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별이 있을때

                            if (((String) (((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중
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
                                jsonArray3.put(jsonObject3);
                                list.put(session, jsonArray3);

                                //메세지 보내기
                                socketSession.sendMessage(message);
                                break;
                            } else { }
                        } else {//성별에 해당하는 사람 없을시
                            System.out.println("해당성별 없음");
                            //위치 검색 범위 1km 증가
                        }

                    }else{//나이 범위에 맞는 사람 없을시
                        System.out.println("해당 나이 없음");
                    }
                }else{// 자신이 채팅방에 들어가있다면
                    int index99 = 0;
                    for(WebSocketSession socketSession99 : list.keySet()) {
                        if (((String) list.get(socketSession99).getJSONObject(0).get("from")).equals(continue_target)) {
                            socketSession99.sendMessage(message);
                            break;
                        } else {
                            index99++;
                        }
                    }
                    if(index99==list.size()){
                        CharSequence alert2 = "상대방이 나갔습니다.";
                        TextMessage message_2u = new TextMessage(alert2);
                        session.sendMessage(message_2u);
                    }
                    break;
                }

            }
        }
    }

}