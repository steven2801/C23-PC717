import admin from "firebase-admin";

require("dotenv").config();

admin.initializeApp({
  credential: admin.credential.cert({
    projectId: process.env.GOOGLE_PROJECT_ID,
    clientEmail: process.env.GOOGLE_CLIENT_EMAIL,
    privateKey: process.env.GOOGLE_PRIVATE_KEY ? process.env.GOOGLE_PRIVATE_KEY.replace(/\\n/gm, "\n") : undefined,
  }),
  databaseURL: "https://auth-15f80-default-rtdb.asia-southeast1.firebasedatabase.app/",
  storageBucket: "gs://auth-15f80.appspot.com",
});

export default admin;
