# Feeder

Feeder is a clickmetric software. That means it analyzes and logs where users are clicking in your website.
It will be accompanied by a frontend lightweight javascript file to measure clicks, navigations, and more.

- [ ] WebSocket support on the server
  - [x] Initial Javalin setup
  - [x] Test webSocketChannel handler
- [x] Apache Kafka API
  - [x] An API to make working with Kafka producers/consumers easier.. maybe annotation based(I.E eventbus system)
- [x] Sentry API
  - [x] An API to make working with Sentry easier for error logging
- [x] Grafana Integration
  - [ ] Better statistical overview of frequency of navigation
- [x] Navigation metric(from where $\to$ what)
- [x] Click metric(most often clicked elements on each page)
  - This is mostly handled by the frontend.
- [x] Retention metric(how long the user stayed on a page)
- [x] Page view metric(how many times a page was viewed)
- [ ] Handle bad payloads and close the session via some validation on JSON payloads.


---
Copyright &copy; 2024 Idriz Pelaj