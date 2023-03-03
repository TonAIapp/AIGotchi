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
    self.completion_model='gpt-3.5-turbo'
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
