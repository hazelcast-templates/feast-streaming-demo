#! /bin/bash

# start the event producer
$HOME/.venv/bin/python3 $HOME/transaction_producer.py kafka:19092 &

# start the Jupyter lab
jupyter lab --ip 0.0.0.0 --no-browser --ServerApp.token ""
