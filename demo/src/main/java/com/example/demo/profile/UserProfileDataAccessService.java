package com.example.demo.profile;

import com.example.demo.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService {
    private final FakeUserProfileDataStore fakeUserProfileDataStore;
    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore){
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }
    List<UserProfile> getUserProfiles()
    {
        return fakeUserProfileDataStore.getUserProfiles();
    }
}
