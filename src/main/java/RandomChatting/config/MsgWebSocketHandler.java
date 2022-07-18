package RandomChatting.config;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

            int index2=0;   int index3=0;
            for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출

                if(object.get("inchat").equals("")) {
                    for (WebSocketSession socketSession99 : list.keySet()) {
                        if (list.get(socketSession99).getJSONObject(0).get("from").equals(object.get("from"))) {
                            //continue_target = (String) list.get(socketSession99).getJSONObject(0).get("inchat");
                            continue_tsex = (String) list.get(socketSession99).getJSONObject(0).get("tsex");
                            continue_ysex = (String) list.get(socketSession99).getJSONObject(0).get("ysex");
                            //맞는 사람들끼리 채팅방 만들기 알고리즘에 필요한 값들 받아오기
                            //continue_locationX = (String) list.get(socketSession99).getJSONObject(0).get("locationX");
                            //continue_locationY = (String) list.get(socketSession99).getJSONObject(0).get("locationY");
                            //continue_yname = (String) list.get(socketSession99).getJSONObject(0).get("yname");
                            //continue_yage = (String) list.get(socketSession99).getJSONObject(0).get("yage");
                        }
                    }
                    System.out.println(continue_tsex);
                    System.out.println((((list.get(socketSession)).getJSONObject(0)).get("ysex")));
                    if (((String)(((list.get(socketSession)).getJSONObject(0)).get("ysex"))).equals(continue_tsex)) {   //타겟성별이 있을때

                        if( ((String)(((list.get(socketSession)).getJSONObject(0)).get("inchat"))).equals("")) {              //채팅방에 안들어가 있는 놈들중
                            //상대방 아이디
                            String t1 = (String) ((list.get(socketSession)).getJSONObject(0)).get("from");
                            //상대방 타겟성별
                            String t2 = (String) ((list.get(socketSession)).getJSONObject(0)).get("tsex");
                            //상대방 자신성별
                            String t3 = (String) ((list.get(socketSession)).getJSONObject(0)).get("ysex");
                            //System.out.println(t1 + "qwe" + t2 + "qwe" + t3);
                            //자기 아이디
                            String y1 = (String) object.get("from");
                            //타겟 성별
                            String y2 = continue_tsex;
                            //자기 성별
                            String y3 = continue_ysex;

                            //상대방에 상대방정보+채팅방에 자신 아이디 입력
                            JSONArray jsonArray2 = new JSONArray();
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("from", t1);
                            jsonObject2.put("tsex", t2);
                            jsonObject2.put("ysex", t3);
                            jsonObject2.put("inchat", y1);
                            jsonArray2.put(jsonObject2);
                            //System.out.println(jsonObject2);
                            list.put(socketSession, jsonArray2);
                            //System.out.println(list.get(socketSession));

                            //자신정보에 자신정보+채팅방에 상대 아이디 입력
                            JSONArray jsonArray3 = new JSONArray();
                            JSONObject jsonObject3 = new JSONObject();
                            jsonObject3.put("from", y1);
                            jsonObject3.put("tsex", y2);
                            jsonObject3.put("ysex", y3);
                            jsonObject3.put("inchat", t1);
                            jsonArray3.put(jsonObject3);
                            //System.out.println(jsonObject3);
                            list.put(session, jsonArray3);
                            //System.out.println(list.get(session));

                            //메세지 보내기
                            socketSession.sendMessage(message);
                        }else{// 자신이 채팅방에 들어가있다면
                            //System.out.println("123123");
                            if(index3==0) {
                                System.out.println(index3);
                                for (WebSocketSession socketSession3 : list.keySet()) {
                                    if (((String)list.get(socketSession3).getJSONObject(0).get("from")).equals(object.get("from"))) {
                                        continue_target = (String) list.get(socketSession3).getJSONObject(0).get("inchat");
                                        //System.out.println(continue_target);
                                    }
                                }
                                index3++;
                            }
                            //System.out.println(continue_target);
                            //System.out.println(list.get( socketSession).getJSONObject(0).get("from"));
                            if(((String)list.get( socketSession).getJSONObject(0).get("from")).equals(continue_target)){
                                //System.out.println("qwe");
                                socketSession.sendMessage( message );
                                //System.out.println(message);
                            }
                        }
                    }else {//성별없을시
                        System.out.println("해당성별 없음");
                    }
                }else{
                    //System.out.println("통신");
                    if(index2==0) {
                        //System.out.println(index2);
                        for (WebSocketSession socketSession2 : list.keySet()) {
                            if (((String)list.get(socketSession2).getJSONObject(0).get("from")).equals(object.get("from"))) {
                                continue_target = (String) list.get(socketSession2).getJSONObject(0).get("inchat");
                                //System.out.println(continue_target);
                            }
                        }
                        index2++;
                    }
                    //System.out.println(continue_target);
                    //System.out.println(((String)list.get( socketSession).getJSONObject(0).get("from")));
                    if(((String)list.get( socketSession).getJSONObject(0).get("from")).equals(continue_target)){
                        //System.out.println("qwe");
                        socketSession.sendMessage( message );
                        //System.out.println(message);
                    }
                }

            }
        }
    }

}
