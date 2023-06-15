import axios from "axios";
import admin from "../firebaseAdminConfig";
import { Request, Response } from "express";
import FormData from "form-data";
import { getLabelsPercentagesAndCategory } from "../utils/getLabelsAndResults";

export const uploadHandler = async (req: Request, res: Response) => {
  const { title, firebaseToken } = req.body;
  const image = req.file;

  // Check if image is defined
  if (!image) {
    return res.status(400).json({ error: "No image file provided" });
  }

  // Decode the Firebase token to get the userId
  let userId: string;
  try {
    const decodedToken = await admin.auth().verifyIdToken(firebaseToken);
    userId = decodedToken.uid;
  } catch (error) {
    console.log(error);
    return res.status(401).json({ error: "Invalid Firebase token" });
  }

  // Reference to Firebase Storage bucket
  const bucket = admin.storage().bucket();

  // Create a unique filename for the image
  const filename = `${userId}/${Date.now()}_${image.originalname}`;

  // Create a reference to the file in Firebase Storage
  const file = bucket.file(filename);

  // Define the file metadata
  const metadata = {
    contentType: image.mimetype,
  };

  // Upload the image file to Firebase Storage
  file.save(image.buffer, { metadata }, async (error) => {
    if (error) {
      console.error("Error uploading image:", error);
      return res.status(500).json({ error: "Failed to upload image" });
    }

    // Get the public URL of the uploaded image
    file.getSignedUrl(
      {
        action: "read",
        expires: "12-12-2099", // Adjust the expiry date as needed
      },
      async (error, imageUrl) => {
        if (error) {
          console.error("Error getting image URL:", error);
          return res.status(500).json({ error: "Failed to get image URL" });
        }

        const db = admin.database();
        const ref = db.ref("users").child(userId);

        const id = ref.push().key;

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

          await ref.child(id as string).set({
            id,
            createdAt: new Date().toISOString(),
            title,
            imageUrl,
            category,
            labels,
            percentages,
          });

          res.status(200).json({ id, success: "Image uploaded successfully" });
        } catch (error) {
          ref.child(id as string).update({
            predicting: "no",
            error: "Failed to detect food.",
          });
          res.status(500).json({ error });
        }
      }
    );
  });
};
