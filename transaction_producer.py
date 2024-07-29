import random
import time
import json
import sys
from typing import List, Set

from confluent_kafka import Producer

def produce_event(account_nums: List[str]) -> dict:
    return {
        "acct_num": random.choice(account_nums),
        "amt": random.randrange(5, 2000) + random.randrange(100) / 100,
        "unix_time": int(time.time()),
    }

def main():
    if len(sys.argv) != 2:
        print(f"usage: {sys.argv[0]} kafka_address", file=sys.stderr)
        return 1

    kafka_address = sys.argv[1]

    def delivery_report(err, msg):
        if err is not None:
            print(f"ERROR Event delivery failed: {err}")

    prod = Producer({
        "bootstrap.servers": kafka_address,
    })

    while True:
        prod.poll(0)
        event = produce_event(ACCOUNT_NUMBERS)
        payload = json.dumps(event)
        prod.produce("transaction", payload, callback=delivery_report)
        print("Produced:", payload)
        time.sleep(1)


ACCOUNT_NUMBERS = [
    "EBJD80665876768751", "SILR40552966043177", "KHRR06076148752117", "HDUX66137794293020",
    "PXOI05719965765594", "TXDV90346759064601", "TITM60905880380935", "QNBW17933045649241",
    "PFBJ80070367382970", "PYKG98596882638670", "KBTS74463050372661", "IEUJ86247588090772",
    "NHYT01831061669858", "YVCV56500100273531", "ADTS97594758575321", "PEYX51049619817961",
    "VIBB29526958059342", "WAXU30991609138496", "UKFH75714629958700", "MSBB82041147805148",
    "XZGN79830428482917", "EAYR66924909796259", "BNVQ22985437460897", "QRQP56813768247223",
    "PXGJ37956777738414", "AKQJ50498452256371", "YYYI34714723164260", "CBTB36606688240351",
]


if __name__ == '__main__':
    main()