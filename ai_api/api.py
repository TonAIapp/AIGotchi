from fastapi import FastAPI, Response
from fastapi.middleware.cors import CORSMiddleware
from starlette.responses import JSONResponse
import uvicorn
import openai
import os

from task_functions import *
from reproduce_functions import *


app=FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_credentials=True,
    allow_origins=['*'],
    allow_methods=['*'],
    allow_headers=['*']
)


class Args():
  def __init__(self):
    self.completion_model='text-davinci-003'
    self.completion_model='gpt-3.5-turbo'
    self.img_url='https://lh3.googleusercontent.com/50j438LCbc3FqjSVC9RHCl9owgZ1F6qRP6lrh_XIOcl3adI7GESLHBB7JlBw_C3N_RI4bmp3DPk3HM826v8cH7PUOJBDb4oDIIHZv7U6LZyn9XKzIoy1jJwYXleECsGoZHJrkPzdRCjMKyoxigEH4bytS4_o2MsAuacawu3bCZROP7wcla_x3s065hnRjW1LLk61daLxMV8mSsjaEjcEgozX0wpV-L45Bq2N7HI_mL1KD2AE16jGVQ6Ml8ed5gUlB7D5gOIZYFU69-zjqvttq3AqW6d43m9wFqr2NDfzv2U8jdAEaCiQevfrMHciB_9QeHWh7LRF_2Kuuv5hlcpTvCjuFiw-wpPXa92lbtYjifk_ikniEUBHgox7NDl2vKMckxNx93R79hKx28dweOLA2Lju0Wk-ImcepNtnyEeCbb6CeA8ST5vE6Ha-WNQG_IT4J2Wv7wzfRdeL3rrsk14RF0ZhMJydn0YiRx2Id1eE-dQYjNHQQccTK6Gd5aWKRcY4jqNZQW-1b1-p06OjXf7d3BrNcIvwyO5aPj54m9X7efbVp1f5FgQWrOIoDCPuK3PF0CqcuM4nCSEAqMxlneMO4zcmMDRekKddT2kwhLefTyOeS7kVbFX_1hmVH42-c6vum1J0sN4Qs27vc_PevCKz_UuYp0vm3fQmTmGgbvJChkuMm6vcVzmUr7OlN7ajvsC01_mskaObmELKeZg-B8AExSTcvnWQ9e184KUN7BGAkTlKbKCfWRhTlXmi1BSeFFMORcqOObkhhQ2jxH_nCD3c1lgpGblShKjtcrWHdRZ62lZ9kgKnmvtyIe9w0_UXHBFTfITSBJ8F3VYAznVrFL9Z9NM1EomC-XvwukXdidwVjg0j6nECAnKXMhNMZZraWeRR3kRYF5GsCw85ikpjRePPkbVFV7U5q9LM9SmrU2aLDBU8Tol-pEOiXyN72uiRmuZ_aIWeudsYON9RHr9S6rFeK12VGY67sYYVdlt5OWrS0AewOiibyjU=s512-no?authuser=0'
    self.prompt='a single round fluffy cute tamagotchi animal caracter with colorful fur, realistic, 3D, solid color background, cartoon'

args=Args()

@app.get('/task-generation/', response_class=JSONResponse)
def generate_task(subject:str, level:int):
  messages = [
  {"role": "system", "content": "You are a kind helpful assistant."},]
  prompt=generate_task_prompt(subject, level)
  messages.append({"role": "user", "content": prompt})
  response=openai.ChatCompletion.create(
      messages=messages,
      model=args.completion_model,
  ).choices[0].message.content
  print(response)
  question, variants, answer = question_answer(response)
  return JSONResponse([{'question' : question, 'variants':variants, 'answer' : answer}])

@app.get('/generate-avatar/')
def generate_avatar():
  response=openai.Image.create(
    prompt=args.prompt,
    n=1,
    size='256x256'
  ).data[0].url
  return Response(response, media_type='text/plain')

@app.get('/reproduce/')
def reproduce(father_url:str, mother_url:str):
  print('Generating description of a first image...')
  father_description=generate_description(father_url)
  print('Generating a description of a second image...')
  mother_description=generate_description(mother_url)
  
  prompt=f'{args.prompt}. {father_description} {mother_description}'
  print(prompt)

  response=openai.Image.create(
      prompt=prompt,
      # image=img, 
      n=1,
      size='256x256'
  ).data[0].url
  os.remove('/data/img.png')
  return Response(response, media_type='text/plain')

@app.get('/check-solution/')
def check_solution(correct_answer:str, solution:str):
  if solution==correct_answer or correct_answer==solution[:-1]:
    response=f'Greate job! {solution} is the correct answer'
  else:
    response='Try again'
  return Response(response, media_type='text/plain')
