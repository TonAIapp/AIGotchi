package handlers

import (
	"encoding/json"
	"fmt"
	"github.com/zeebo/errs"
	"net/http"
	"synthia"
	"synthia/ai"
	"synthia/bucket"
	"synthia/pkg/logger"
	"synthia/repository"
)

var (
	// handlersErr is an internal error type for handlers.
	handlersErr = errs.Class("synthia handler error")
)

type Handlers struct {
	log  logger.Logger
	rep  repository.Repository
	ai   *ai.Service
	buck *bucket.Service
}

func NewHandlers(log logger.Logger, rep repository.Repository, ai *ai.Service, buck *bucket.Service) *Handlers {
	return &Handlers{
		log:  log,
		rep:  rep,
		ai:   ai,
		buck: buck,
	}
}

func (h *Handlers) Onboard(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var user synthia.User

	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		newErrorResponse(w, h.log, http.StatusBadRequest, "message decoding", handlersErr.Wrap(err))
		return
	}

	data, err := h.ai.GenerateImg()
	if err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "img generating", handlersErr.Wrap(err))
		return
	}

	img, err := h.buck.SaveFile(data, fmt.Sprintf("%d.png", user.TelegramId))
	if err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "img saving", handlersErr.Wrap(err))
		return
	}

	user.HP = 100
	user.Img = img

	if err := h.rep.AddUser(r.Context(), user); err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "user adding", handlersErr.Wrap(err))
		return
	}

	if err := json.NewEncoder(w).Encode(user); err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "response encoding", handlersErr.Wrap(err))
		return
	}

	w.WriteHeader(http.StatusOK)
}

func (h *Handlers) SetSteps(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var user synthia.User

	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		newErrorResponse(w, h.log, http.StatusBadRequest, "message decoding", handlersErr.Wrap(err))
		return
	}

	if err := h.rep.SetSteps(r.Context(), user); err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "steps setting", handlersErr.Wrap(err))
		return
	}

	w.WriteHeader(http.StatusOK)
}

func (h *Handlers) GetUser(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var user synthia.User

	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		newErrorResponse(w, h.log, http.StatusBadRequest, "message decoding", handlersErr.Wrap(err))
		return
	}

	user, err := h.rep.GetUser(r.Context(), user)
	if err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "user getting", handlersErr.Wrap(err))
		return
	}

	if err := json.NewEncoder(w).Encode(user); err != nil {
		newErrorResponse(w, h.log, http.StatusInternalServerError, "response encoding", handlersErr.Wrap(err))
		return
	}

	w.WriteHeader(http.StatusOK)
}
