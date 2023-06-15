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
exports.uploadNoTokenHandler = void 0;
const axios_1 = __importDefault(require("axios"));
const form_data_1 = __importDefault(require("form-data"));
const getLabelsAndResults_1 = require("../utils/getLabelsAndResults");
const uploadNoTokenHandler = (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const { title, firebaseToken } = req.body;
    const image = req.file;
    // Check if image is defined
    if (!image) {
        return res.status(400).json({ error: "No image file provided" });
    }
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
        res.status(200).json({ labels, percentages, category });
    }
    catch (error) {
        res.status(500).json({ error });
    }
});
exports.uploadNoTokenHandler = uploadNoTokenHandler;
