# Food Quality Detector, Bangkit 2023 Final Capstone Project

Mobile app that detects whether a food is fresh or stale using Stratified CNN model.

### Team ID : C23-PC717

### Team Members

- M040DSY0374 – Tamara Dhia'ussururi – Institut Teknologi Sumatera - ML
- M181DSX0400 – Kenneth Jonathan – Universitas Indonesia - ML
- C206DSX0800 – Sahdan Wira – Universitas Jambi - CC
- C306DSX0715 – Muhammad Dzaki – Universitas Pembangunan Nasional Veteran Yogyakarta - CC
- C282DSY1666 – Afaf Nur Salsabillah Firdausiah – Universitas Negeri Malang - CC
- A181DKX4442 – Steven – Universitas Indonesia - MD

Theme : Food Accessibility, Agribusiness, and Food Security

## Installation (MD)

### Prerequisites

Make sure you have the following software installed on your machine:

- Android Studio
- Java Development Kit (JDK)

### 1. Clone the Repository

Clone this repository to your local machine using Git:

```shell
$ git clone https://github.com/steven2801/C23-PC717.git
```

Navigate to `/md`

```shell
$ cd C23-PC717/md
```

### 2. Open the Project in Android Studio

1. Launch Android Studio.
2. Click on "Open an Existing Project" or "Import Project."
3. Navigate to the cloned repository directory and select it.
4. Click "OK" to open the project.

### 3. Build and Run the Project

1. Once the project is opened in Android Studio, wait for it to finish syncing and indexing.
2. If necessary, update your SDK and build tools to match the project requirements.
3. Click on the "Run" button in the toolbar or use the shortcut Shift + F10 to run the project.
4. Select the desired emulator or physical device to run the app.
5. Wait for the build process to complete, and the app will be installed and launched on the selected device.

## Installation (CC)

## Prerequisites

Before running the application, make sure you have the following:

- Node.js installed on your machine
- Firebase service account credentials (credentials.json)

## Installation

### 1. Clone the Repository

Clone this repository to your local machine using Git:

```shell
$ git clone https://github.com/steven2801/C23-PC717.git
```

Navigate to `/cc`

```shell
$ cd C23-PC717/cc
```

### 2. Install the dependencies:

```shell
yarn

```

### 3. Create a copy of the .env.example file and name it .env:

```shell
cp .env.example .env

```

### 4. Open the .env file and fill in the required environment variables:

```makefile
GOOGLE_PROJECT_ID=<your_google_project_id>
GOOGLE_PRIVATE_KEY=<your_google_private_key>
GOOGLE_CLIENT_EMAIL=<your_google_client_email>

```

### 5. Start the application locally

```shell
yarn dev
```

Now you can access the application at http://localhost:3000.

Make sure to replace the environment variable values with your actual credentials.

Check [`src/index.ts`](https://github.com/steven2801/C23-PC717/blob/main/cc/src/index.ts#LL16C1-L24C52) for available API endpoints.

## Installation (ML)

### 1. Clone the Repository

Clone this repository to your local machine using Git:

```shell
$ git clone https://github.com/steven2801/C23-PC717.git
```

Navigate to `/ml`

```shell
$ cd C23-PC717/ml
```

### 2. Install the dependencies:

```shell
pip install -r requirements.txt

```

### 3. Start the application locally

```shell
python main.py
```

Now you can access the application at http://localhost:8080.

To predict an image, send a POST HTTP Request to the `/predict_image` endpoint with image file (jpeg/png) as request body.
