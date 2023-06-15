import axios from "axios";
import { Request, Response } from "express";
import FormData from "form-data";
import { getLabelsPercentagesAndCategory } from "../utils/getLabelsAndResults";

export const uploadNoTokenHandler = async (req: Request, res: Response) => {
  const { title, firebaseToken } = req.body;
  const image = req.file;

  // Check if image is defined
  if (!image) {
    return res.status(400).json({ error: "No image file provided" });
  }

  try {
    // Create a new FormData object
    const formData = new FormData();

    // Append the uploaded file to the FormData object
    formData.append("uploaded_file", image.buffer, image.originalname);

    // Make a POST request to the ML API using Axios
    const mlApiUrl = process.env.ML_API_URL || "http://model:8080";
    const response = await axios.post(mlApiUrl + "/predict_image", formData, {
      headers: formData.getHeaders(),
    });

    const { labels, percentages, category } = getLabelsPercentagesAndCategory(response);

    res.status(200).json({ labels, percentages, category });
  } catch (error) {
    res.status(500).json({ error });
  }
};
