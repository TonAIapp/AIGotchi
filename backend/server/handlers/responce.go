package handlers

import (
	"encoding/json"
	"net/http"
	"synthia/pkg/logger"
)

type errorResponse struct {
	Message string `json:"message"`
}

func newErrorResponse(w http.ResponseWriter, log logger.Logger, statusCode int, msg string, err error) {
	log.Error(msg, err)
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(errorResponse{Message: err.Error()})
}
