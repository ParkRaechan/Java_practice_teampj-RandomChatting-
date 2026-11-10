package RandomChatting.service;

import RandomChatting.model.ChatUser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Thread-safe in-memory session registry; no database or chat history. */
@Service
public class MatchingService {
    private final Map<WebSocketSession, ChatUser> users = new HashMap<>();
    private final Map<WebSocketSession, WebSocketSession> partners = new HashMap<>();
    private final MatchingPolicy policy = new MatchingPolicy(MatchingPolicy::haversineKm);

    public synchronized void register(WebSocketSession session, ChatUser user) {
        if (!session.isOpen()) return;
        if (partners.containsKey(session)) throw new IllegalStateException("already matched");
        users.put(session, user);
    }

    public synchronized Optional<WebSocketSession> partnerOf(WebSocketSession session) {
        return Optional.ofNullable(partners.get(session));
    }

    public synchronized Optional<WebSocketSession> match(WebSocketSession requester) {
        ChatUser me = users.get(requester);
        if (me == null || !requester.isOpen() || partners.containsKey(requester))
            return Optional.empty();

        Map<WebSocketSession, ChatUser> available = users.entrySet().stream()
                .filter(e -> e.getKey() != requester && e.getKey().isOpen() &&
                        !partners.containsKey(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Optional<ChatUser> best = policy.choose(me, available.values());
        if (!best.isPresent()) return Optional.empty();
        WebSocketSession peer = available.entrySet().stream()
                .filter(e -> e.getValue() == best.get()).findFirst().get().getKey();
        partners.put(requester, peer);
        partners.put(peer, requester);
        return Optional.of(peer);
    }

    public synchronized Optional<WebSocketSession> disconnected(WebSocketSession session) {
        users.remove(session);
        WebSocketSession peer = partners.remove(session);
        if (peer != null) partners.remove(peer);
        return Optional.ofNullable(peer);
    }
}
