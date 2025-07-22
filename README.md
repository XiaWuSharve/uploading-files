# File Uploading Service

## Requirements

- [Java runtime environment](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) >= 17
- [maven](https://maven.apache.org/)
- [config server](https://github.com/XiaWuSharve/config-server) on http://localhost:8888

## Usage

```bash
cd uploading-files
mvn spring-boot:run
```

For the build version:

```bash
mvn clean package
java -jar target/uploading-files-0.0.1-SNAPSHOT.jar
```

#### Usage

##### List all files

```bash
# On linux
curl --location 'http://localhost:8080/files'
```

return response body:

```json
{
  "_embedded": {
    "filenameDTOList": [
      {
        "filename": "file1",
        "_links": {
          "self": {
            "href": "http://localhost:8080/files/file1"
          },
          "files": { "href": "http://localhost:8080/files" }
        }
      },
      {
        "filename": "file2",
        "_links": {
          "self": {
            "href": "http://localhost:8080/files/file2"
          },
          "files": { "href": "http://localhost:8080/files" }
        }
      }
    ]
  },
  "_links": { "self": { "href": "http://localhost:8080/files" } }
}
```

##### Upload

```bash
# On linux
curl --location 'http://localhost:8080/files' \
--form 'file=@"/path/to/uploading/file"'
```

return response body:

```json
{
  "filename": "file",
  "_links": {
    "self": {
      "href": "http://localhost:8080/files/file"
    },
    "files": { "href": "http://localhost:8080/files" }
  }
}
```

##### Download

```bash
# On linux
curl 'http://localhost:8080/files/file'
```

return response body: `file content`
