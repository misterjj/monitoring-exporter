# monitoring-exporter

## run

```bash
sbt run
```

## Youtrack

go to http://127.0.0.1:8080/youtrack/metrics?q=<QUERY>

### Environment variables

| name           | require | description               |
|----------------|:-------:|:--------------------------|
| YOUTRACK_HOST  |  false  | Url to your youtrack      |
| YOUTRACK_TOKEN |  true   | Bearer token youtrack API |

## Gitlab

go to http://127.0.0.1:8080/gitlab/environments?project=<PROJECT_ID>

### Environment variables

| name         | require | description             |
|--------------|:-------:|:------------------------|
| GITLAB_HOST  |  false  | Url to your gitlab      |
| GITLAB_TOKEN |  true   | Bearer token gitlab API |
