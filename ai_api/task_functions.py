 
def generate_task_prompt(subject:str, level:int):
  return f'generate a simple test question for children who study {subject} with 4 answer variants and choose an answer to it from provided variants. Format the result in the form of "Question: " "Answer:”'

def question_answer(response:str):
  print(response)
  response=response.split(': ')
  question=response[1]
  print(question)
  variants=[x for x in question.split('\n') if x!='']
  variants=variants[1:5]
  var={}
  i=0
  for variant in variants:
    var[i]=variant
    i+=1

  question=question.split('\n')[0]
  question=question.replace('\"', "'")
  answer=response[-1]
  return (question, var, answer)