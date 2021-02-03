package com.example.demo.profile;

import com.example.demo.bucket.BucketName;
import com.example.demo.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {
    // All the business logic happens here
    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore)
    {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }
    List<UserProfile> getUserProfiles()
    {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        isTheFileEmpty(file);

        // 2. If file is an image

        isTheFileImage(file);

        // 3. The user exists in our database
        UserProfile user = getUserOrThrow(userProfileId);


        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);


        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

//        fileStore.save(path,filename,Optional.of(metadata), file,getInputStream());

        try {
            // Saving the image to S3 bucket
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserOrThrow(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());
        // Study streams from amigos code
        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path,key))
                .orElse(new byte[0]);
//        return fileStore.download(fullpath);

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserOrThrow(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }

    private void isTheFileImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("The File should be an image [" + file.getContentType() + "]");
        }
    }

    private void isTheFileEmpty(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException("Cant upload empty file [" + file.getSize() +"]");
        }
    }


}
