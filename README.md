# http_load_balancer
This repo contains implementation for a custom load balancer with a feature of timely health checkup


Step1:

Return the request headers received from the request itself

Step2:

Add Round robin logic to select the server from the pool of the servers using multithreading env

Step 3:

Added multi-threading to perform health checkup on the listed server and recheck the down server
