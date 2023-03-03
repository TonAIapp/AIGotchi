const { Markup, Composer, Scenes } = require('telegraf')

const getTaskStep = new Composer()

getTaskStep.on('text', async (ctx) => {
  try {
    await ctx.replyWithHTML(
      'Complete tasks to get food to keep your pet alive'
      + '\n\nCorrect answer will give your AIGotchi + 10 health points'
      + '\n\nPlease press button <b>Get task</b> to get task'
      + '\n\nYou can check AIGotchi health points by running'
      + '\ncommand <code>/healthpoints</code>',
      Markup.keyboard([
        'Get task',
        'Remove keyboard'
      ]).oneTime().resize()
    )
    return ctx.scene.leave()
  } catch (e) {
    console.log('task error', e)
  }
})

const taskScene = new Scenes.WizardScene('taskWizard', getTaskStep)

module.exports = taskScene
