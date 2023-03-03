package redis

import (
	"context"
	"encoding/json"
	"github.com/go-redis/redis"
	"github.com/zeebo/errs"
	"synthia"
	"synthia/pkg/logger"
)

var (
	// redisErr is an internal error type for redis db.
	redisErr = errs.Class("redis db error")
)

type Config struct {
	Addr     string `json:"addr"`
	Password string `json:"password"`
}

type DB struct {
	client *redis.Client
}

func New(ctx context.Context, log logger.Logger, cfg Config) *DB {
	client := redis.NewClient(&redis.Options{
		Addr:     cfg.Addr,
		Password: cfg.Password,
		DB:       0,
	})
	client = client.WithContext(ctx)

	if err := client.Ping().Err(); err != nil {
		log.Fatal("connecting to Redis:", redisErr.Wrap(err))
	}

	return &DB{client: client}
}

func (db *DB) Close() error {
	return db.client.Close()
}

func (db *DB) AddUser(_ context.Context, user synthia.User) error {
	json, err := json.Marshal(user)
	if err != nil {
		return redisErr.Wrap(err)
	}

	err = db.client.Set(string(user.TelegramId), json, 0).Err()
	if err != nil {
		return redisErr.Wrap(err)
	}

	return nil
}

func (db *DB) SetSteps(_ context.Context, newUserSteps synthia.User) error {
	oldUserSteps := synthia.User{}

	if err := db.client.Get(string(newUserSteps.TelegramId)).Scan(&oldUserSteps); err != nil {
		return redisErr.Wrap(err)
	}

	oldUserSteps.Steps = newUserSteps.Steps

	json, err := json.Marshal(oldUserSteps)
	if err != nil {
		return redisErr.Wrap(err)
	}

	if err = db.client.Set(string(oldUserSteps.TelegramId), json, 0).Err(); err != nil {
		return redisErr.Wrap(err)
	}

	return nil
}

func (db *DB) GetUser(_ context.Context, user synthia.User) (synthia.User, error) {
	if err := db.client.Get(string(user.TelegramId)).Scan(&user); err != nil {
		return user, redisErr.Wrap(err)
	}

	return user, nil
}
