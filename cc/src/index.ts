import express from "express";
import multer from "multer";
const cors = require("cors");

import { uploadHandler } from "./handler/upload";
import { itemsHandler } from "./handler/items";
import { uploadNoTokenHandler } from "./handler/upload-no-token";

const app = express();
app.use(express.json());
app.use(cors());

const storage = multer.memoryStorage();
const upload = multer({ storage });

app.get("/", (req, res) => {
  res.json({ message: "Welcome" });
});

app.post("/api/upload-no-token", upload.single("image"), uploadNoTokenHandler);

app.post("/api/upload", upload.single("image"), uploadHandler);

app.get("/api/items/:firebaseToken", itemsHandler);

require("dotenv").config;

// Use PORT provided in environment or default to 3000
const port = process.env.PORT || 3000;

// Listen on `port` and 0.0.0.0
app.listen(port as number, "0.0.0.0", function () {
  console.log("Server is running on port 3000");
});
