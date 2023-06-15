FROM node:16-alpine

WORKDIR /server

COPY package*.json .
RUN yarn
COPY . .

ENV HOST 0.0.0.0

EXPOSE 3000

CMD ["node", "build/index.js"]