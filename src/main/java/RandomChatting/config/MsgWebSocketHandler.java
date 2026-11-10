package RandomChatting.config;

import RandomChatting.model.ChatUser;
import RandomChatting.service.MatchingService;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/** Protocol adapter; matching decisions and mutable state belong to MatchingService. */
@Component
public class MsgWebSocketHandler extends TextWebSocketHandler {
    private final MatchingService matching;

    public MsgWebSocketHandler(MatchingService matching) {
        this.matching = matching;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        Optional<WebSocketSession> peer = matching.disconnected(session);
        if (peer.isPresent()) send(peer.get(), "상대방이 나갔습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject json;
        try {
            json = new JSONObject(message.getPayload());
        } catch (RuntimeException e) {
            send(session, "잘못된 메시지 형식입니다");
            return;
        }

        // The existing browser sends an eight-key profile immediately on connection.
        if (json.has("tsex") && json.has("ysex") && json.has("yage") &&
            json.has("locationX") && json.has("locationY") && json.has("yname")) {
            try {
                matching.register(session, profile(session, json));
            } catch (RuntimeException e) {
                send(session, "잘못된 사용자 정보입니다");
            }
            return;
        }

        Optional<WebSocketSession> peer = matching.partnerOf(session);
        if (peer.isPresent()) {
            if (peer.get().isOpen()) {
                send(peer.get(), message.getPayload());
            } else {
                matching.disconnected(peer.get());
                send(session, "상대방이 나갔습니다.");
            }
            return;
        }

        Optional<WebSocketSession> selected = matching.match(session);
        if (selected.isPresent()) send(selected.get(), message.getPayload());
        else send(session, "찾는 사람이 없습니다");
    }

    private ChatUser profile(WebSocketSession session, JSONObject json) {
        URI uri = session.getUri();
        if (uri == null || uri.getPath() == null) throw new IllegalArgumentException("URI");
        String path = uri.getPath();
        String id = path.substring(path.lastIndexOf('/') + 1);
        // Client fields locationX/locationY mean longitude/latitude (Kakao Maps).
        return new ChatUser(id, json.getString("tsex"), json.getString("ysex"),
                json.getString("yname"), Integer.parseInt(json.get("yage").toString()),
                Double.parseDouble(json.get("locationY").toString()),
                Double.parseDouble(json.get("locationX").toString()));
    }

    private void send(WebSocketSession session, String body) throws IOException {
        if (session.isOpen()) {
            synchronized (session) {
                if (session.isOpen()) session.sendMessage(new TextMessage(body));
            }
        }
    }
}
