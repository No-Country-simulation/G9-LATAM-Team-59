#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
python3 start_server.py --port 3000 "$@"
