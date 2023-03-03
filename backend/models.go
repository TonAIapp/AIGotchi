package synthia

type User struct {
	TelegramId     int64  `json:"telegram_id"`
	Age            int    `json:"age"`
	TamagotchiName string `json:"tamagotchi_name"`
	Img            string `json:"img"`
	HP             int    `json:"hp"`
	Steps          int    `json:"steps"`
}
