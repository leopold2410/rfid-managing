# rfid-managing
Microservice to manage Rfids and their times, when they were encountered

This is one of three microservices, which form a system for managing competitors an their race time for small running competitions.

The three microservices are 
- competition-managing: Each competitor can be assinged personal data, a starting time, a starting number and an rfid tag for measuring the race time. The competition-managing microservice is the main endpoint for providing the rest api for the client 
- rfid-reading: Reads the rfid tags and measures the times they were encountered. This data is published to a queue
- rfid-managing: Reads the data from the queue and writes it to a database for managing rfid tags and the times they were encountered

The competion-managing microservice communicates with rfid-managing by an async request-response messaging, for getting the information which tags were at at which time.

client <-> competition managing <- "rfid-and-times-requests" queue -> rfid-managing -> "rfid-times" queue <- rfid-reading
