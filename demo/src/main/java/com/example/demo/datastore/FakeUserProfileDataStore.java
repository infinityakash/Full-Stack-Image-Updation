package com.example.demo.datastore;


import com.example.demo.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();
    static {
//        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "ramlal", null));
//        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "rishab", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("6109c3dd-166f-42fc-8723-feb757077a95"), "ramlal", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("2c37cb53-7b05-46b3-a630-72259cc1e7c9"), "rishab", null));
    }

    public List<UserProfile> getUserProfiles()
    {
        return USER_PROFILES;
    }


}
