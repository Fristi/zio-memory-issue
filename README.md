# How to run

1. `docker-compose up` to start kafka
2. when kafka is started, `sbt rc21/run`
3. you should see `Received record`...
4. Profile the application, memory will be leaking and you should get an OOM within 10 minutes


With rc20/run the program will run just fine