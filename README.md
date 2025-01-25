
# Audio Storage and Retrieval Application

This project is a Spring Boot-based application designed to store and retrieve audio files. It uses PostgreSQL as the database and runs within a Dockerized environment for seamless deployment.

---

## Prerequisites

Before running the application, ensure you have the following installed on your system:

- **Java 17 or later**: [Install Java](https://adoptopenjdk.net/)
- **Maven**: [Install Maven](https://maven.apache.org/install.html)
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)

---

## Setup

### Step 1: Clone or Download the Repository

Download this project repository, which contains the necessary `docker-compose.yml` and other configuration files.

### Step 2: Build the Spring Boot Application

Open a terminal in the project directory and run the following command:

```bash
mvn clean package

```


### Step 3: Run the Application

After building the application, you can run it using the following command:

```bash
docker-compose up --build
```


### Step 4: Access the Application

Once the application is running, you can access it at the following URL:

```bash 
http://127.0.0.1:8080
```
## Application health and metrics
```
Health Endpoint: http://localhost:8080/actuator/health
Metrics Endpoint: http://localhost:8080/actuator/metrics
```

### Upload audio
example audio files you can use for conversion: https://drive.google.com/drive/folders/1tN_0H6Sx6SiT1nZp5pm4mOBtYdxy69lc?usp=sharing

### Postman curl

```bash
curl --location 'http://127.0.0.1:8080/audio/user/1/phrase/1' \
--form 'audio_file=@"/C:/Users/username/Downloads/file_example_MP3_700KB.mp3"'
```

```
curl --location 'http://localhost:8080/audio/user/1/phrase/1/mp3'
```