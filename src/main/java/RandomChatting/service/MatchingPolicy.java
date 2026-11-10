package RandomChatting.service;

import RandomChatting.model.ChatUser;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToDoubleBiFunction;

/** Pure matching policy. Larger score is better; -1 denotes an ineligible candidate. */
public final class MatchingPolicy {
    private final ToDoubleBiFunction<ChatUser, ChatUser> distanceKm;

    public MatchingPolicy(ToDoubleBiFunction<ChatUser, ChatUser> distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int score(ChatUser me, ChatUser other) {
        if (me.id.equals(other.id)) return -1;
        int ageDiff = Math.abs(me.age - other.age);
        if (ageDiff > 2) return -1;
        double km = distanceKm.applyAsDouble(me, other);
        if (!Double.isFinite(km) || km < 0 || km > 5) return -1;
        int mutual = me.preferredSex.equals(other.sex) && other.preferredSex.equals(me.sex) ? 2 :
                (me.preferredSex.equals(other.sex) ? 1 : 0);
        int distance = km <= 1 ? 3 : km <= 2 ? 2 : 1;
        return (3 - ageDiff) * 100 + mutual * 10 + distance;
    }

    public Optional<ChatUser> choose(ChatUser me, Collection<ChatUser> candidates) {
        return candidates.stream()
                .filter(u -> score(me, u) >= 0)
                .max(Comparator.comparingInt((ChatUser u) -> score(me, u))
                        .thenComparing(u -> u.id, Comparator.reverseOrder()));
    }

    /** Haversine distance. Inputs are latitude followed by longitude, in degrees. */
    public static double haversineKm(ChatUser a, ChatUser b) {
        double lat = Math.toRadians(b.latitude - a.latitude);
        double lon = Math.toRadians(b.longitude - a.longitude);
        double h = Math.pow(Math.sin(lat / 2), 2) +
                Math.cos(Math.toRadians(a.latitude)) * Math.cos(Math.toRadians(b.latitude)) *
                Math.pow(Math.sin(lon / 2), 2);
        return 6371.0088 * 2 * Math.asin(Math.min(1, Math.sqrt(h)));
    }
}
