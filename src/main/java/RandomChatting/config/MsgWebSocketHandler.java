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
        //System.out.println(jsonArray);
        // 세션과 아이디 같이 저장
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
        if(object.length()==4){
            for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출
                //System.out.println( );
                //System.out.println(object.get("from"));
                if( (((list.get( socketSession)).getJSONObject(0)).get("from")).equals( object.get("from")  ) ){
                    //    //System.out.println("성공");
                    //    //System.out.println(object.get("from"));
                    //    //System.out.println(object.get("tsex"));
                    //    //System.out.println(object.get("ysex"));
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from",object.get("from"));
                    jsonObject.put("tsex",object.get("tsex"));
                    jsonObject.put("ysex",object.get("ysex"));

                    //jsonObject.put("inchat",((list.get( socketSession)).getJSONObject(0)).get("from"));
                    jsonObject.put("inchat","");
                    jsonArray.put(jsonObject);
                    //list.remove( session );
                    //    //System.out.println(list.get( socketSession));
                    list.put( session , jsonArray );
                    System.out.println(list.get( socketSession));
                }
            }
        }else{// 현재 접속된 세션들중에 받는사람(to) 와 같은경우 소켓 메시지 전달
            //System.out.println("전송1");
            for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출
                //System.out.println(list.get( socketSession));
                //System.out.println(object);
                //System.out.println(object.get("inchat"));
                if(object.get("inchat").equals("")) {
                    if ((((list.get(socketSession)).getJSONObject(0)).get("ysex")).equals(object.get("tsex")) &&   //타겟성별이 있을때
                            (((list.get(socketSession)).getJSONObject(0)).get("inchat")).equals("")) {              //채팅방에 안들어가 있는 놈들중
                        //상대방 아이디
                        String t1 = (String) ((list.get(socketSession)).getJSONObject(0)).get("from");
                        //상대방 타겟성별
                        String t2 = (String) ((list.get(socketSession)).getJSONObject(0)).get("tsex");
                        //상대방 자신성별
                        String t3 = (String) ((list.get(socketSession)).getJSONObject(0)).get("ysex");
                        System.out.println(t1+"qwe"+t2+"qwe"+t3);
                        //자기 아이디
                        String y1 = (String) object.get("from");
                        //타겟 성별
                        String y2 = (String) object.get("tsex");
                        //자기 성별
                        String y3 = (String) object.get("ysex");

                        //상대방에 상대방정보+채팅방에 자신 아이디 입력
                        JSONArray jsonArray2 = new JSONArray();
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("from", t1);
                        jsonObject2.put("tsex", t2);
                        jsonObject2.put("ysex", t3);
                        jsonObject2.put("inchat", y1);
                        jsonArray2.put(jsonObject2);
                        list.put(socketSession, jsonArray2);
                        System.out.println(list.get( socketSession));

                        //자신정보에 자신정보+채팅방에 상대 아이디 입력
                        JSONArray jsonArray3 = new JSONArray();
                        JSONObject jsonObject3 = new JSONObject();
                        jsonObject3.put("from", y1);
                        jsonObject3.put("tsex", y2);
                        jsonObject3.put("ysex", y3);
                        jsonObject3.put("inchat", t1);
                        jsonArray3.put(jsonObject3);
                        list.put(session, jsonArray3);
                        System.out.println(list.get( session));

                        //메세지 보내기
                        socketSession.sendMessage( message );
                    }
                }else{
                    System.out.println("통신");
                    if(list.get( socketSession).getJSONObject(0).get("from").equals(object.get("inchat"))){
                        System.out.println(object.get("inchat"));
                        socketSession.sendMessage( message );
                    }
                }
            }
        }
    }




}
