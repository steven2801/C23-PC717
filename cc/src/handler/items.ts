import admin from "../firebaseAdminConfig";
import { Request, Response } from "express";

export const itemsHandler = async (req: Request, res: Response) => {
  const { firebaseToken } = req.params;

  try {
    const decodedToken = await admin.auth().verifyIdToken(firebaseToken);
    const userId = decodedToken.uid;

    const db = admin.database();
    const itemsRef = db.ref(`users/${userId}`);

    itemsRef.once("value", (snapshot) => {
      const items = snapshot.val();
      if (items) {
        const modifiedItems = Object.entries(items).map(([_, value]) => value);
        res.status(200).json(modifiedItems);
      } else {
        res.status(404).json({ error: "User items not found" });
      }
    });
  } catch (error) {
    console.error("Error decoding token:", error);
    res.status(401).json({ error: "Invalid Firebase token" });
  }
};
