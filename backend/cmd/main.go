package main

import (
	"bytes"
	"context"
	"encoding/json"
	"github.com/zeebo/errs"
	"net"
	"os"
	"synthia/ai"
	"synthia/bucket"
	"synthia/pkg/logger"
	"synthia/pkg/logger/zaplog"
	"synthia/repository/redis"
	"synthia/server"
	"synthia/server/handlers"
)

var defaultConfigPath = "/app/config/config.json"

type Config struct {
	S3    bucket.Config `json:"s3"`
	AI    ai.Config     `json:"ai"`
	Redis redis.Config  `json:"redis"`
}

func NewConfig(log logger.Logger, pathFile string) (conf Config) {
	buf, err := os.ReadFile(pathFile)
	if err != nil {
		log.Fatal("Reading config", err)
	}

	if err := json.NewDecoder(bytes.NewReader(buf)).Decode(&conf); err != nil {
		log.Fatal("Parsing config", err)
	}
	return
}

func init() {
	if os.Getenv("CONFIG_PATH") != "" {
		defaultConfigPath = os.Getenv("CONFIG_PATH")
	}
}

func main() {
	log := zaplog.NewLog()
	ctx := context.Background()

	conf := NewConfig(log, defaultConfigPath)

	red := redis.New(ctx, log, conf.Redis)
	aiClient := ai.New(conf.AI)
	buck := bucket.New(log, conf.S3)
	hand := handlers.NewHandlers(log, red, aiClient, buck)
	listener, err := net.Listen("tcp", ":8080")
	if err != nil {
		log.Fatal("creating a listener", err)
	}

	serv := server.NewServer(log, listener, hand)
	runErr := serv.Run(ctx)
	closeErr := serv.Close()
	redErr := red.Close()

	if runErr != nil || closeErr != nil || redErr != nil {
		log.Error("server error", errs.Combine(runErr, closeErr, redErr))
	}
}
