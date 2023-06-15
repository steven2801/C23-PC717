"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.uploadHandler = void 0;
const axios_1 = __importDefault(require("axios"));
const firebaseAdminConfig_1 = __importDefault(require("../firebaseAdminConfig"));
const form_data_1 = __importDefault(require("form-data"));
const getLabelsAndResults_1 = require("../utils/getLabelsAndResults");
const uploadHandler = (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const { title, firebaseToken } = req.body;
    const image = req.file;
    // Check if image is defined
    if (!image) {
        return res.status(400).json({ error: "No image file provided" });
    }
    // Decode the Firebase token to get the userId
    let userId;
    try {
        const decodedToken = yield firebaseAdminConfig_1.default.auth().verifyIdToken(firebaseToken);
        userId = decodedToken.uid;
    }
    catch (error) {
        console.log(error);
        return res.status(401).json({ error: "Invalid Firebase token" });
    }
    // Reference to Firebase Storage bucket
    const bucket = firebaseAdminConfig_1.default.storage().bucket();
    // Create a unique filename for the image
    const filename = `${userId}/${Date.now()}_${image.originalname}`;
    // Create a reference to the file in Firebase Storage
    const file = bucket.file(filename);
    // Define the file metadata
    const metadata = {
        contentType: image.mimetype,
    };
    // Upload the image file to Firebase Storage
    file.save(image.buffer, { metadata }, (error) => __awaiter(void 0, void 0, void 0, function* () {
        if (error) {
            console.error("Error uploading image:", error);
            return res.status(500).json({ error: "Failed to upload image" });
        }
        // Get the public URL of the uploaded image
        file.getSignedUrl({
            action: "read",
            expires: "12-12-2099", // Adjust the expiry date as needed
        }, (error, imageUrl) => __awaiter(void 0, void 0, void 0, function* () {
            if (error) {
                console.error("Error getting image URL:", error);
                return res.status(500).json({ error: "Failed to get image URL" });
            }
            const db = firebaseAdminConfig_1.default.database();
            const ref = db.ref("users").child(userId);
            const id = ref.push().key;
            try {
                // Create a new FormData object
                const formData = new form_data_1.default();
                // Append the uploaded file to the FormData object
                formData.append("uploaded_file", image.buffer, image.originalname);
                // Make a POST request to the ML API using Axios
                const mlApiUrl = process.env.ML_API_URL || "http://model:8080";
                const response = yield axios_1.default.post(mlApiUrl + "/predict_image", formData, {
                    headers: formData.getHeaders(),
                });
                const { labels, percentages, category } = (0, getLabelsAndResults_1.getLabelsPercentagesAndCategory)(response);
                yield ref.child(id).set({
                    id,
                    createdAt: new Date().toISOString(),
                    title,
                    imageUrl,
                    category,
                    labels,
                    percentages,
                });
                res.status(200).json({ id, success: "Image uploaded successfully" });
            }
            catch (error) {
                ref.child(id).update({
                    predicting: "no",
                    error: "Failed to detect food.",
                });
                res.status(500).json({ error });
            }
        }));
    }));
});
exports.uploadHandler = uploadHandler;
