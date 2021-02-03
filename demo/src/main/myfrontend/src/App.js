import React, { useState, useEffect, useCallback } from "react"
// import logo from './logo.svg';
import './App.css';
import axios from "axios"
import { useDropzone } from 'react-dropzone'


const UserProfiles = () => {

  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.log(res);

      setUserProfiles(res.data);

    });
  }
  useEffect(() => {
    fetchUserProfiles();
  }, []);
  return userProfiles.map((userProfile, index) => {
    return (
      <div key={index}>
        {userProfile.userProfileId ? (
          <img 
            src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`} 
            />
            ) : null}
        <br />
        <br />
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone userProfileId={userProfile.userProfileId} />
        <br />
      </div>)
  })
}; // This is closer of UserProfile you earlier put Dropzone function here

function Dropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    const file = acceptedFiles[0];
    console.log(file);
    // Logic to upload file to the database
    const formData = new FormData();
    formData.append("file", file);
    // This file is the same as UserProfileController.java class
    // @RequestParam("file") part of 
    // public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
    // @RequestParam("file") MultipartFile file)
    //     {
    //     userProfileService.uploadUserProfileImage(userProfileId, file);
    //     }

    axios.post(
      `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }
    ).then(() => {
      console.log("Yeah file upload is successful");
    }).catch(err => {
      console.log(err);
    })
  }, [])
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop some picture here, or click to select profile image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">

      <UserProfiles />
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React Akash
        </a>
      </header> */}
    </div>
  );
}

export default App;
