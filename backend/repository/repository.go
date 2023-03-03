package repository

import (
	"context"
	"synthia"
)

type Repository interface {
	AddUser(ctx context.Context, user synthia.User) error
	SetSteps(_ context.Context, newUserSteps synthia.User) error
	GetUser(_ context.Context, user synthia.User) (synthia.User, error)
}
