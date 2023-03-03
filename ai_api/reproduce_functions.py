import requests
from io import BytesIO
from PIL import Image
import os
import replicate


model=replicate.models.get("rmokady/clip_prefix_caption")
version=model.versions.get("9a34a6339872a03f45236f114321fb51fc7aa8269d38ae0ce5334969981e4cd8")

def get_image_from_url(img_url:str):
  img_raw=requests.get(img_url)
  img=BytesIO(img_raw.content)
  return img

def generate_description(img_url:str):
  img=Image.open(get_image_from_url(img_url)).convert('RGB')
  os.makedirs('/data/', exist_ok=True)
  img.save('/data/img.png')
  inputs = {
    'image' : open('/data/img.png', 'rb'),
    'model': "coco",
    'use_beam_search': False,
  }
  output = version.predict(**inputs)
  return output