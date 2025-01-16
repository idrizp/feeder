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

To run the project, you may use the docker-compose.yml file by running the command:

```bash
EXPORT SENTRY_DSN=your_sentry_dsn docker-compose up -d
```

To run the client, you may go into the client directory and run the following:

```bash
npm install
npm run dev
```

---
Copyright &copy; 2024 Idriz Pelaj