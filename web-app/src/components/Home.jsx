import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../services/localStorageService";
import Header from "./header/Header";
import { Box, Card, CircularProgress, Typography } from "@mui/material";

export default function Home() {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({});

  const getUserDetails = async (accessToken) => {
    const response = await fetch(
      `http://localhost:8080/identity/users/my-info`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    const data = await response.json();
    console.log(data);
    
    setUserDetails(data.data);
  };

  const addPassword = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const newPassword = formData.get("newPassword");

    const accessToken = getToken();
    const response = await fetch(
      `http://localhost:8080/identity/users/create-password`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ password: newPassword })
      }
    );

    if (response.ok) {
      console.log("Password set successfully");
      // Refresh user details or navigate to a different page
    } else {
      console.error("Failed to set password");
    }
  };

  useEffect(() => {
    const accessToken = getToken();

    if (!accessToken) {
      navigate("/login");
    }

    getUserDetails(accessToken);
  }, [navigate]);

  return (
    <>
      <Header></Header>
      {userDetails ? (
        <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
          height="100vh"
          bgcolor={"#f0f2f5"}
        >
          <Card
            sx={{
              minWidth: 400,
              maxWidth: 500,
              boxShadow: 4,
              borderRadius: 4,
              padding: 4,
            }}
          >
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                width: "100%", // Ensure content takes full width
              }}
            >
              <p>Welcome back, {userDetails.username}!</p>
              <h1 className="name">{userDetails.firstName} {userDetails.lastName}</h1>
              <p className="email">{userDetails.email}</p>
              {userDetails.noPassword && (
                <Box sx={{ marginTop: 3, width: "100%" }}>
                  <Typography variant="body2" sx={{ marginBottom: 2 }}>
                    Please set a password for your account
                  </Typography>
                  <form>
                    <input type="password" placeholder="New password" required />
                    <button type="submit" onSubmit={addPassword}>Set Password</button>
                  </form>
                </Box>
              )}
            </Box>
          </Card>
        </Box>
      ) : (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: "30px",
            justifyContent: "center",
            alignItems: "center",
            height: "100vh",
          }}
        >
          <CircularProgress></CircularProgress>
          <Typography>Loading ...</Typography>
        </Box>
      )}
    </>
  );
}
