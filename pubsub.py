#!/usr/bin/env python

# Copyright 2019 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import argparse
import random

from google.cloud import pubsub_v1

project_id = "solution-challenge-308122"
topic_id = "hello_topic"
subscription_id = "sub_one"
male_female = 1

def pub():
    """Publishes a message to a Pub/Sub topic."""
    # Initialize a Publisher client.
    client = pubsub_v1.PublisherClient()
    # Create a fully qualified identifier of form `projects/{project_id}/topics/{topic_id}`
    topic_path = client.topic_path(project_id, topic_id)

    # Data sent to Cloud Pub/Sub must be a bytestring.
    data = ""
    
    #api_future = client.publish(topic_path, data.encode('utf-8'),P_BOT="BOT-fecc8d1e-a315-4c04-a4bc-11ba58d84769",female_angry = ''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), female_calm=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), female_fearful=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), 
    #female_happy = ''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),female_sad=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),
    #male_angry=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_calm=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_fearful=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_happy=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_sad=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]))

    # When you publish a message, the client returns a future.
    #api_future = client.publish(topic_path, data.encode('utf-8'),P_BOT="BOT-fecc8d1e-a315-4c04-a4bc-11ba58d84769",female_angry = ''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), female_calm=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), female_fearful=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]), 
     #female_happy = ''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),female_sad=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),
     #male_angry="0",male_calm="0",male_fearful="0",male_happy="0",male_sad="0")
    
      #global male_female
    api_future = client.publish(topic_path, data.encode('utf-8'),P_BOT="BOT-fecc8d1e-a315-4c04-a4bc-11ba58d84769",female_angry = "0", female_calm="0", female_fearful="0", 
     female_happy = "0",female_sad="0",
     male_angry=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_calm=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_fearful=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_happy=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]),male_sad=''.join(["{}".format(random.randint(0, 9)) for num in range(0, 2)]))
    
    message_id = api_future.result()

    #print(f"Published {data} to {topic_path}: {message_id}")
