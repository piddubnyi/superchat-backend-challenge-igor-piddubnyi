# Initial implementation for the assignment

## Used stack :Kotlin, Spring (faster to use compared to Quarkus), Postgres, Hibernate, Docker

Known outstanding problems (assignment already is big enough, making a "perfect" solution will take more time that I'm willing to spend for unpaid assignment):
 - Absence of unit/integration testing
 - Absence of the incoming/outgoing message indication in the conversations.
 - Bitcoin price is generated as random value (if the expectation was to connect some external price provider, this should be clearly specified)
 - No dedicated API for webhook (with current state same api method can be used to send and receive msgs, by swapping "me" header and destanation)

# Steps to test
Start the db:
docker-compose up -d postgres

Start the main in the application: com.digiscorp.superchat.SuperchatApplicationKt

Run the requests:

### Create contacts (me created implicitly)
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"name": "Max", "email": "max@mail.com"}' 'http://127.0.0.1:8080/contacts'
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"name": "Alex", "email": "alex@mail.com"}' 'http://127.0.0.1:8080/contacts'

### List contacts
curl -X GET -H 'me: igor@mail.com' 'http://127.0.0.1:8080/contacts'

### Send msgs with placeholders
curl -X POST -H 'Content-Type: application/json' -H 'me: alex@mail.com' -d '{"dst": "igor@mail.com", "ts": 1655149223937, "content": "Hi @name!"}' 'http://127.0.0.1:8080/msgs'
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"dst": "alex@mail.com", "ts": 1655149223938, "content": "Nice to see you @name"}' 'http://127.0.0.1:8080/msgs'
curl -X POST -H 'Content-Type: application/json' -H 'me: igor@mail.com' -d '{"dst": "max@mail.com", "ts": 1655149223939, "content": "Hey @name, BTC now costs @price!"}' 'http://127.0.0.1:8080/msgs'

### Get conversations
curl -X GET -H 'me: igor@mail.com' 'http://127.0.0.1:8080/msgs'
curl -X GET -H 'me: alex@mail.com' 'http://127.0.0.1:8080/msgs'