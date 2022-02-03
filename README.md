# youtrack-issue-exporter

## run

```bash
sbt run
```

go to http://127.0.0.1:8080/metrics?q=<QUERY>

## Environment variables

| name           | require | description               |
|----------------|:-------:|:--------------------------|
| YOUTRACK_HOST  |  false  | Url to your youtrack      |
| YOUTRACK_TOKEN |  true   | Bearer token youtrack API |
