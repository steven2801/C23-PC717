"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getTimeAgo = void 0;
const javascript_time_ago_1 = __importDefault(require("javascript-time-ago"));
// English.
const en_1 = __importDefault(require("javascript-time-ago/locale/en"));
javascript_time_ago_1.default.addDefaultLocale(en_1.default);
// Create formatter (English).
const timeAgo = new javascript_time_ago_1.default("en-US");
const getTimeAgo = (date) => {
    return timeAgo.format(date);
};
exports.getTimeAgo = getTimeAgo;
