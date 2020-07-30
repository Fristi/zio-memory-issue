# How to run

1. `docker-compose up` to start kafka
2. when kafka is started, `sbt run`
3. you should see `Received record`...
4. Profile the application, memory will be leaking and you should get an OOM within 10 minutes
