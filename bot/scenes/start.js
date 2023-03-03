const { Markup, Composer, Scenes } = require('telegraf')
const { default: axios } = require('axios');

const ageStep = new Composer()

ageStep.on('text', async (ctx) => {
  try {
    ctx.wizard.state.data = {}
    ctx.wizard.state.data.userName = ctx.message.from.username
    ctx.wizard.state.data.userId = ctx.message.chat.id
    ctx.wizard.state.data.age = ctx.message.text
    await ctx.replyWithHTML('Please enter your real age, because it will be using for complexity of generating tasks')
    return ctx.wizard.next()
  } catch (e) {
    console.log('age error', e)
  }
})

const nameStep = new Composer()

nameStep.on('text', async (ctx) => {
  try {
    console.log('age', +ctx.wizard.state.data.age)
    if (+ctx.wizard.state.data.age < 0 || +ctx.wizard.state.data.age > 100) {
      ctx.wizard.selectStep(0)
    }
    ctx.wizard.state.data.age = ctx.message.text;
    await ctx.replyWithHTML('Please enter name for your own AIGotchi')
    return ctx.wizard.next()
  } catch (e) {
    console.log('name error', e)
  }
})


const endStep = new Composer()

endStep.on('text', async (ctx) => {
  try {
    ctx.wizard.state.data.name = ctx.message.text
    const { name, userId, age } = ctx.wizard.state.data
    const avatarResponse = await axios.get(`${process.env.API_BASE_URL}generate-avatar`);
    await ctx.replyWithPhoto(
      {
        url : avatarResponse.data,
      },
      {
        caption:
        'Nice job!'
        + `\n<b>${name}</b> sounds good)`,
        parse_mode: "HTML",
      },
    )
    return ctx.scene.enter('taskWizard')
  } catch (e) {
    console.log('start end step error', e);
  }
})

const startScene = new Scenes.WizardScene('startWizard', ageStep, nameStep, endStep)

module.exports = startScene
