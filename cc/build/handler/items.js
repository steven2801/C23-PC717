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
exports.itemsHandler = void 0;
const firebaseAdminConfig_1 = __importDefault(require("../firebaseAdminConfig"));
const itemsHandler = (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const { firebaseToken } = req.params;
    try {
        const decodedToken = yield firebaseAdminConfig_1.default.auth().verifyIdToken(firebaseToken);
        const userId = decodedToken.uid;
        const db = firebaseAdminConfig_1.default.database();
        const itemsRef = db.ref(`users/${userId}`);
        itemsRef.once("value", (snapshot) => {
            const items = snapshot.val();
            if (items) {
                const modifiedItems = Object.entries(items).map(([_, value]) => value);
                res.status(200).json(modifiedItems);
            }
            else {
                res.status(404).json({ error: "User items not found" });
            }
        });
    }
    catch (error) {
        console.error("Error decoding token:", error);
        res.status(401).json({ error: "Invalid Firebase token" });
    }
});
exports.itemsHandler = itemsHandler;
