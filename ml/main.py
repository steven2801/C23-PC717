# README
# Hello everyone, in here I (Kaenova | Bangkit Mentor ML-20)
# will give you some headstart on createing ML API.
# Please read every lines and comments carefully.
#
# I give you a headstart on text based input and image based input API.
# To run this server, don't forget to install all the libraries in the
# requirements.txt simply by "pip install -r requirements.txt"
# and then use "python main.py" to run it
#
# For ML:
# Please prepare your model either in .h5 or saved model format.
# Put your model in the same folder as this main.py file.
# You will load your model down the line into this code.
# There are 2 option I give you, either your model image based input
# or text based input. You need to finish functions "def predict_text" or "def predict_image"
#
# For CC:
# You can check the endpoint that ML being used, eiter it's /predict_text or
# /predict_image. For /predict_text you need a JSON {"text": "your text"},
# and for /predict_image you need to send an multipart-form with a "uploaded_file"
# field. you can see this api documentation when running this server and go into /docs
# I also prepared the Dockerfile so you can easily modify and create a container iamge
# The default port is 8080, but you can inject PORT environement variable.
#
# If you want to have consultation with me
# just chat me through Discord (kaenova#2859) and arrange the consultation time
#
# Share your capstone application with me! 🥳
# Instagram @kaenovama
# Twitter @kaenovama
# LinkedIn /in/kaenova

## Start your code here! ##

import json
import os
import numpy as np
import uvicorn
import traceback
import tensorflow as tf
from PIL import Image

from pydantic import BaseModel
from urllib.request import Request
from fastapi import FastAPI, Response, UploadFile
from utils import load_image_into_numpy_array

load_options = tf.saved_model.LoadOptions(
    experimental_io_device='/job:localhost')

model = tf.saved_model.load("./my_model_folder", options=load_options)

app = FastAPI()


@app.get("/")
def index():
    return "Hello world from ML endpoint!"


with open('./my_model_folder/labels', 'r') as file:
    file_content = file.read()

lines = file_content.split('\n')
lines = [line.replace('_', ' ') for line in lines if line.strip()]
labels = lines


@app.post("/predict_image")
def predict_image(uploaded_file: UploadFile, response: Response):
    try:
        if uploaded_file.content_type not in ["image/jpeg", "image/png"]:
            response.status_code = 400
            return "File is Not an Image"

        img = load_image_into_numpy_array(uploaded_file.file.read())
        # image = np.resize(img, (480, 480, 3)) / 255.0

        img = tf.io.decode_png(img, channels=3)
        # Resize the image to the desired size
        image = tf.image.resize(img, [480, 480])

        input_data = tf.constant([image], dtype=tf.float32)

        result = model(input_data)
        tensor = tf.constant(result)

        results_array = tensor.numpy()[0].tolist()

        max_value = max(results_array)
        max_index = results_array.index(max_value)

        max_result = (labels[max_index], max_value)

        sorted_list = sorted(zip(labels, results_array),
                             key=lambda x: x[1], reverse=True)
        sorted_labels, sorted_results = zip(*sorted_list)

        print(json.dumps({
            'labels': list(sorted_labels),
            'results': list(sorted_results),
            'max': max_result
        }, indent=2))

        return json.dumps({
            'labels': list(sorted_labels),
            'results': list(sorted_results),
            'max': max_result
        })
    except Exception as e:
        traceback.print_exc()
        response.status_code = 500
        return "Internal Server Error"


port = os.environ.get("PORT", 8080)
print(f"Listening to http://0.0.0.0:{port}")
uvicorn.run(app, host='0.0.0.0', port=port)
