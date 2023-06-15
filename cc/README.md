# Node.js Express App

This is a Node.js Express application that requires the following environment variables to be set before running the app.

## Prerequisites

Before running the application, make sure you have the following:

- Node.js installed on your machine
- Firebase service account credentials (credentials.json)

## Installation

1. Clone the repository:

   ```shell
   git clone <repository_url>

   ```

2. Install the dependencies:

   ```shell
   yarn

   ```

3. Create a copy of the .env.example file and name it .env:

   ```shell
   cp .env.example .env

   ```

4. Open the .env file and fill in the required environment variables:

   ```makefile
   GOOGLE_PROJECT_ID=<your_google_project_id>
   GOOGLE_PRIVATE_KEY=<your_google_private_key>
   GOOGLE_CLIENT_EMAIL=<your_google_client_email>

   ```

5. Start the application locally
   ```shell
   yarn dev
   ```

Now you can access the application at http://localhost:3000.

Make sure to replace the environment variable values with your actual credentials.
