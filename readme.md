# Initial implementation for the assignment

## Used stack :Kotlin, Spring (faster to use compared to Quarkus), Postgres, Hibernate, Docker

Known outstanding problems:
 - Absence of unit/integration testing
 - No dedicated API for webhook (with current state same api method can be used to send and receive msgs, by swapping "me" header and destination)

# Steps to test
Start the db:
docker-compose up -d postgres

Start the main in the application: com.digiscorp.superchat.SuperchatApplicationKt

Run the requests:

### Create contacts
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"name": "Max", "channelType" : "EMAIL", "channelId": "max@mail.com"}' 'http://127.0.0.1:8080/contacts'
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"name": "Alex", "channelType" : "SMS", "channelId": "+123456"}' 'http://127.0.0.1:8080/contacts'
curl -X POST -H 'Content-Type: application/json' -H 'me: +123456' -d '{"name": "Igor", "channelType" : "EMAIL", "channelId": "igor@mail.com"}' 'http://127.0.0.1:8080/contacts'

### List contacts
curl -X GET -H 'me: igor@mail.com' 'http://127.0.0.1:8080/contacts'

### Send msgs with placeholders
curl -X POST -H 'Content-Type: application/json' -H 'me: +123456' -d '{"dst": "igor@mail.com", "ts": 1655149223937, "content": "Hi @name!"}' 'http://127.0.0.1:8080/msgs'

curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"dst": "+123456", "ts": 1655149223938, "content": "Nice to see you @name"}' 'http://127.0.0.1:8080/msgs'

curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"dst": "max@mail.com", "ts": 1655149223939, "content": "Hey @name, BTC now costs @price!"}' 'http://127.0.0.1:8080/msgs'

### Get conversations
curl -X GET -H 'me: igor@mail.com' 'http://127.0.0.1:8080/msgs'