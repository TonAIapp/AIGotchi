package ai

import (
	"errors"
	"github.com/zeebo/errs"
	"io"
	"net/http"
)

var (
	// aiErr is an internal error type for ai.
	aiErr = errs.Class("ai error")
)

type Config struct {
	GenerateImgUrl string `json:"generate_img_url"`
}

type Service struct {
	generateImgUrl string
}

func New(cfg Config) *Service {
	return &Service{}
}

func (s *Service) GenerateImg() (io.Reader, error) {
	resp, err := http.Get(s.generateImgUrl)
	if err != nil {
		return nil, aiErr.Wrap(err)
	}

	buf, _ := io.ReadAll(resp.Body)

	resp, err = http.Get(string(buf))
	if err != nil {
		return nil, aiErr.Wrap(err)
	}
	if resp.StatusCode != 200 {
		return nil, aiErr.Wrap(errors.New("received non 200 response code"))
	}

	return resp.Body, nil
}
