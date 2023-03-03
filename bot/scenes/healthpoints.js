const { Markup, Composer, Scenes } = require('telegraf');

const getHealthPointsStep = new Composer()

const hpFactory = () => {
  let hp = 100
  return (value) => {
    if (value) {
      hp += value
    }
    return hp
  }
}

const hp1 = hpFactory()

getHealthPointsStep.on('text', async (ctx) => {
  try {
    await ctx.replyWithHTML(
      'Info about your AIGotchi'
      + `\nhealth points: ${hp1()}`
    )
    return ctx.scene.leave()
  } catch (e) {
    console.log('health points error', e)
  }
})

const healthPointsScene = new Scenes.WizardScene('healthPointsWizard', getHealthPointsStep)

module.exports = { healthPointsScene, hp1 }
