package RandomChatting.model;

import java.util.Objects;

/** Immutable profile held in memory only for the lifetime of a WebSocket session. */
public final class ChatUser {
    public final String id, preferredSex, sex, name;
    public final int age;
    public final double latitude, longitude;

    public ChatUser(String id, String preferredSex, String sex, String name,
                    int age, double latitude, double longitude) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("id");
        this.id = id;
        this.preferredSex = Objects.requireNonNull(preferredSex);
        this.sex = Objects.requireNonNull(sex);
        this.name = Objects.requireNonNull(name);
        if (age < 1 || age > 120) throw new IllegalArgumentException("age");
        if (!Double.isFinite(latitude) || Math.abs(latitude) > 90 ||
            !Double.isFinite(longitude) || Math.abs(longitude) > 180)
            throw new IllegalArgumentException("coordinates");
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
