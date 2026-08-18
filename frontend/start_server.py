#!/usr/bin/env python3
import argparse
import functools
import os
import sys
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer


class FrontendRequestHandler(SimpleHTTPRequestHandler):
    def end_headers(self) -> None:
        self.send_header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
        self.send_header("Pragma", "no-cache")
        self.send_header("Expires", "0")
        super().end_headers()

    def do_GET(self) -> None:
        if self.path in {"/", "/index.html"}:
            self.send_response(302)
            self.send_header("Location", "/html/index.html")
            self.end_headers()
            return
        super().do_GET()


def main() -> None:
    parser = argparse.ArgumentParser(description="Serve the frontend with a simple HTTP server")
    parser.add_argument("--host", default="127.0.0.1", help="Host address to bind to (default: 127.0.0.1)")
    parser.add_argument("--port", type=int, default=3000, help="Port to listen on (default: 3000)")
    args = parser.parse_args()

    frontend_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(frontend_dir)

    handler = functools.partial(FrontendRequestHandler, directory=frontend_dir)

    try:
        with ThreadingHTTPServer((args.host, args.port), handler) as httpd:
            print(f"Serving {frontend_dir}")
            print(f"Open http://{args.host}:{args.port}/")
            print("Press Ctrl+C to stop.")
            httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped.")
    except OSError as exc:
        print(f"Error starting server: {exc}", file=sys.stderr)
        raise SystemExit(1)


if __name__ == "__main__":
    main()
