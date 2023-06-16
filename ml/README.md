## Installation Guide

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
