"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const multer_1 = __importDefault(require("multer"));
const cors = require("cors");
const upload_1 = require("./handler/upload");
const items_1 = require("./handler/items");
const upload_no_token_1 = require("./handler/upload-no-token");
const app = (0, express_1.default)();
app.use(express_1.default.json());
app.use(cors());
const storage = multer_1.default.memoryStorage();
const upload = (0, multer_1.default)({ storage });
app.get("/", (req, res) => {
    res.json({ message: "Welcome" });
});
app.post("/api/upload-no-token", upload.single("image"), upload_no_token_1.uploadNoTokenHandler);
app.post("/api/upload", upload.single("image"), upload_1.uploadHandler);
app.get("/api/items/:firebaseToken", items_1.itemsHandler);
require("dotenv").config;
// Use PORT provided in environment or default to 3000
const port = process.env.PORT || 3000;
// Listen on `port` and 0.0.0.0
app.listen(port, "0.0.0.0", function () {
    console.log("Server is running on port 3000");
});
