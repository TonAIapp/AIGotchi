package server

import (
	"context"
	"errors"
	"github.com/gorilla/mux"
	"github.com/zeebo/errs"
	"golang.org/x/sync/errgroup"
	"net"
	"net/http"
	"synthia/pkg/logger"
	"synthia/server/handlers"
)

var (
	// Error is an error class that indicates internal http server error.
	Error = errs.Class("web server error")
)

type Config struct {
	//Port                   int    `json:"port"`
	//AddTransactionEndpoint string `json:"add_transaction_endpoint"`
	//ErrorEndpoint          string `json:"error_endpoint"`
}

// Server represents console web server.
type Server struct {
	log logger.Logger

	listener net.Listener
	server   http.Server
}

// NewServer is a constructor for console web server.
func NewServer(log logger.Logger, listener net.Listener, handlers *handlers.Handlers) *Server {
	server := &Server{
		log:      log,
		listener: listener,
	}

	router := mux.NewRouter()

	router.HandleFunc("/onboard", handlers.Onboard).Methods(http.MethodPost)
	router.HandleFunc("/get_user", handlers.GetUser).Methods(http.MethodPost)
	router.HandleFunc("/set_steps", handlers.SetSteps).Methods(http.MethodPost)
	server.server = http.Server{
		Handler: router,
	}

	return server
}

// Run starts the server that host api endpoint.
func (server *Server) Run(ctx context.Context) (err error) {
	ctx, cancel := context.WithCancel(ctx)
	var group errgroup.Group
	group.Go(func() error {
		<-ctx.Done()
		return Error.Wrap(server.server.Shutdown(context.Background()))
	})
	group.Go(func() error {
		defer cancel()
		err := server.server.Serve(server.listener)
		isCancelled := errs.IsFunc(err, func(err error) bool { return errors.Is(err, context.Canceled) })
		if isCancelled || errors.Is(err, http.ErrServerClosed) {
			err = nil
		}
		return Error.Wrap(err)
	})

	return Error.Wrap(group.Wait())
}

// Close closes server and underlying listener.
func (server *Server) Close() error {
	return Error.Wrap(server.server.Close())
}
