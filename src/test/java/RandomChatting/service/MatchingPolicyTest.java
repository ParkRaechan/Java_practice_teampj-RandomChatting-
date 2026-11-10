package RandomChatting.service;

import RandomChatting.model.ChatUser;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class MatchingPolicyTest {
    private final MatchingPolicy policy = new MatchingPolicy((a,b) -> b.latitude);

    private ChatUser user(String id, int age, String sex, String preferred, double distance) {
        return new ChatUser(id, preferred, sex, id, age, distance, 0);
    }

    @Test void ageOutranksPreferenceAndDistance() {
        ChatUser me = user("me",25,"남","여",0);
        assertEquals("same", policy.choose(me, Arrays.asList(
            user("young",24,"여","남",0.2), user("same",25,"남","남",4))).get().id);
    }

    @Test void mutualPreferenceOutranksOneSidedPreference() {
        ChatUser me = user("me",25,"남","여",0);
        assertEquals("mutual", policy.choose(me, Arrays.asList(
            user("one",25,"여","여",0.5), user("mutual",25,"여","남",2))).get().id);
    }

    @Test void rejectsTooOldTooFarAndEmpty() {
        ChatUser me = user("me",25,"남","여",0);
        assertFalse(policy.choose(me, Arrays.asList(
            user("old",28,"여","남",0.5), user("far",25,"여","남",5.1))).isPresent());
        assertFalse(policy.choose(me, Collections.emptyList()).isPresent());
    }

    @Test void tiesAreDeterministic() {
        ChatUser me = user("me",25,"남","여",0);
        assertEquals("a", policy.choose(me, Arrays.asList(
            user("z",25,"여","남",1), user("a",25,"여","남",1))).get().id);
    }

    @Test void distanceUsesLatitudeThenLongitude() {
        ChatUser a = new ChatUser("a","여","남","a",25,37.3156,126.8386);
        ChatUser b = new ChatUser("b","남","여","b",25,37.3156,126.8386);
        assertEquals(0, MatchingPolicy.haversineKm(a,b), 1e-9);
    }
}
