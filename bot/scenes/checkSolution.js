const { Markup, Composer, Scenes } = require('telegraf');
const axios = require('axios')
require('dotenv').config()
let { hp1 } = require('./healthpoints')

const checkSolutionStep = new Composer()
const subjectStep = new Composer()

let counter = 0
let tryCounter = 0

subjectStep.on('text', async (ctx) => {
  try {
    await ctx.reply(
      'Choose subject from list below',
      Markup.keyboard(['Geography', 'English', 'Remove keyboard']).oneTime().resize()
    )
    ctx.wizard.next()
  } catch (e) {
    console.log('check subject error', e)
  }
})

checkSolutionStep.on('text', async (ctx) => {
  try {
    const subject = ctx.message.text
    console.log('subject', subject)
    const response = await axios.get(`${process.env.API_BASE_URL}task-generation/?subject=${subject}&level=1`)
    console.log('response', response.data)
    question = response.data[0].question
    variants = response.data[0].variants
    correct_answer = response.data[0].answer
    await ctx.replyWithHTML(
      response.data[0].question,
      Markup.keyboard([Object.values(variants).map((variant) => variant.trimRight())])
    )
    return ctx.wizard.next()
  } catch (e) {
    console.log('check solution error', e)
  }
})

const endStep = new Composer()

endStep.on('text', async (ctx) => {
  try {
    const response = await axios.get(`${process.env.API_BASE_URL}check-solution/?correct_answer=${JSON.stringify(correct_answer)}&solution=${JSON.stringify(ctx.message.text)}`)
    console.log('checking', response.data)
    if (counter >= 2) {
      await ctx.reply('you used all your tries. Try again with different task')
      counter = 0
      return ctx.scene.leave()
    }
    if (response.data.includes('correct answer')) {
      await ctx.reply(`${response.data}\n\nGood job!\nYou've earned 10 hp\n\nPlay more!`, Markup.keyboard(['Play once more','Remove keyboard']))
      hp1(10)
      counter = 0
      return ctx.scene.leave()
    } else {
      await ctx.reply('Incorrect answer. Try once more')
      counter++
    }
  } catch (e) {
    console.log('check solution end step error', e)
  }
})


const checkSolutionScene = new Scenes.WizardScene('checkSolutionWizard', subjectStep, checkSolutionStep, endStep)

module.exports = checkSolutionScene
