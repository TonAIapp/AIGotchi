const { Telegraf, Markup, Scenes, session } = require('telegraf')
require('dotenv').config()
const startScene = require('../scenes/start')
const taskScene = require('../scenes/completeTask')
const { healthPointsScene } = require('../scenes/healthpoints')
const checkSolutionScene = require('../scenes/checkSolution')

const bot = new Telegraf(process.env.TELEGRAM_BOT_TOKEN);

const stage = new Scenes.Stage([startScene, taskScene, healthPointsScene, checkSolutionScene]);
bot.use(session())
bot.use(stage.middleware())

// inline command buttons
bot.hears('Enter your age', (ctx) => {
  ctx.scene.enter('startWizard')
})

bot.hears('Get task', (ctx) => {
  ctx.scene.enter('checkSolutionWizard')
})

bot.hears('Play once more', (ctx) => {
  ctx.scene.enter('checkSolutionWizard')
})

bot.hears('Remove keyboard', async (ctx) => {
  await bot.telegram.sendMessage(ctx.chat.id, "Remove keyboard",
  {
    reply_markup: {
      remove_keyboard: true
    }
  })
})

// main bot buttons (start with /)
bot.hears('/completetask', (ctx) => {
  ctx.scene.enter('taskWizard')
})

bot.hears('/healthpoints', (ctx) => {
  ctx.scene.enter('healthPointsWizard')
})

bot.hears('/getandroidstepapp', (ctx) => {
  ctx.replyWithHTML('Connect below', Markup.inlineKeyboard([{text: 'Connect', url: `${process.env.ANDROID_STEP_APP}${ctx.chat.id}`}]))
})

bot.start( async (ctx) => {
  try {
    await ctx.replyWithHTML(
      'Welcome to AIGotchi, @'
        + ctx.from.username
        + '.\n I am glad to see you here)'
        + '\n\n<b>AIGotchi</b> is Telegram bot, which is a Tamagotchi game where the user has to perform certain actions in real life to support the Tamagotchi.'
        + '\n\nBefore we start, we need to know how old you are.'
        + '\nSo please click the button below (<b>Enter your age</b>).',
        Markup.keyboard([
          {text: 'Enter your age'},
          {text: 'Remove keyboard'}
        ]).oneTime().resize()
    )
  } catch (e) {
    console.log('error', e);
  }
})
bot.on('sticker', (ctx) => ctx.reply('👍'))

bot.launch()

