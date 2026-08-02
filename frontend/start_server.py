#!/usr/bin/env python3
import argparse
import functools
import os
import sys
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer


def main() -> None:
    parser = argparse.ArgumentParser(description="Serve the frontend with a simple HTTP server")
    parser.add_argument("--host", default="127.0.0.1", help="Host address to bind to (default: 127.0.0.1)")
    parser.add_argument("--port", type=int, default=3000, help="Port to listen on (default: 3000)")
    args = parser.parse_args()

    frontend_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(frontend_dir)

    handler = functools.partial(SimpleHTTPRequestHandler, directory=frontend_dir)

    try:
        with ThreadingHTTPServer((args.host, args.port), handler) as httpd:
            print(f"Serving {frontend_dir}")
            print(f"Open http://{args.host}:{args.port}/html/index.html")
            print("Press Ctrl+C to stop.")
            httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped.")
    except OSError as exc:
        print(f"Error starting server: {exc}", file=sys.stderr)
        raise SystemExit(1)


if __name__ == "__main__":
    main()
